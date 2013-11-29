/*
 * investovator, Stock Market Gaming Framework
 *     Copyright (C) 2013  investovator
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.investovator.ui.utils.dashboard;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.analysis.exceptions.AnalysisException;
import org.investovator.analysis.exceptions.InvalidParamException;
import org.investovator.analysis.signalgen.MASignalGenFactory;
import org.investovator.analysis.signalgen.SignalGenerator;
import org.investovator.analysis.signalgen.SignalGeneratorFactory;
import org.investovator.analysis.signalgen.events.AnalysisEvent;
import org.investovator.analysis.signalgen.events.MarketClosedEvent;
import org.investovator.analysis.signalgen.timeseries.SigGenFromMA;
import org.investovator.analysis.signalgen.timeseries.SigGenMAType;
import org.investovator.analysis.signalgen.timeseries.utils.SigGenTSParams;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataPlayerCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.core.commons.events.GameEvent;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.UserAlreadyJoinedException;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.UIConstants;

import java.util.*;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public abstract class DecisionMakerPanel extends DashboardPanel implements PlaybackEventListener {

    GridLayout content;

    //indicator list
    protected ComboBox indicators;

    //table to show buy sell decisions
    protected Table stockList;

    //description

    //Decision maker
    SignalGeneratorFactory generatorFactory;
    SignalGenerator sigGen;

    //calculation lengths
    protected int slowPeriod;
    protected int quickPeriod;

    public DecisionMakerPanel() {
        this.slowPeriod=26;
        this.quickPeriod=12;

        this.content = new GridLayout(2,2);
        this.setContent(this.content);
        this.generatorFactory = new MASignalGenFactory();
    }

    @Override
    public void onEnter() {

        //check if a game instance exists
        if((Session.getCurrentGameInstance()==null)){
            getUI().getNavigator().navigateTo(UIConstants.USER_VIEW);
            return;
        }

        addAsAListener();

        setUpUI();
    }

    /**
     * Sets up the basic components
     */
    private void setUpUI(){
        ////section on indicator selection
        HorizontalLayout indicatorContainer=new HorizontalLayout();
        this.content.addComponent(indicatorContainer);
//        this.content.setColumnExpandRatio(0,1);
//        this.content.setColumnExpandRatio(1,1);

        Label indicatorName=new Label("Market Trend Indicator : ");
        indicatorContainer.addComponent(indicatorName);

        this.indicators=new ComboBox();
        indicatorContainer.addComponent(this.indicators);
        this.indicators.setNullSelectionAllowed(false);
        //add the indicators
        for(SigGenMAType type:SigGenMAType.values()){
            this.indicators.addItem(type);
        }
        this.indicators.select(this.indicators.getItemIds().toArray()[0]);
        //add an event listener
        this.indicators.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                try {
                    changeSignalGenerator();
                } catch (InvalidParamException e) {
                    e.printStackTrace();
                }
            }
        });
        //create a signal generator
        try {
            changeSignalGenerator();
        } catch (InvalidParamException e) {
            e.printStackTrace();
        }
        ////

        //section on stocks table
        VerticalLayout tableContainer=new VerticalLayout();
        this.stockList=createStocksTable();
        tableContainer.addComponent(this.stockList);
        this.content.addComponent(tableContainer,0,1);
        ////

        //section on tool description
        CssLayout descriptionContainer=createPanel(getToolDescription());
        this.content.addComponent(descriptionContainer,1,0,1,1);


    }

    /**
     * changes the signal generator. Can be used to change the decision making algorithm
     * @throws InvalidParamException
     */
    private void changeSignalGenerator() throws InvalidParamException {
        SigGenMAType type=(SigGenMAType)this.indicators.getValue();

        //create parameters
        SigGenTSParams params = new SigGenTSParams();
        params.setMaType(type);
        params.setQuickPeriod(this.quickPeriod);
        params.setSlowPeriod(this.slowPeriod);

        //add the initial data
        HashMap<String,HashMap<Date, Double>> map =getInitialData();
        for(Map.Entry<String, HashMap<Date, Double>> entry:map.entrySet()){
            List<AnalysisEvent> marketEventSet=new ArrayList<>();
            //iterate the data for this stock
            HashMap<Date, Double> stockData=entry.getValue();
            String stockId=entry.getKey();
            for(Map.Entry<Date, Double> dataEntry:stockData.entrySet()){
                AnalysisEvent event=new MarketClosedEvent(stockId,dataEntry.getKey(),dataEntry.getValue());
                marketEventSet.add(event);
            }

            params.setMarketEvents(stockId,marketEventSet);
        }


        this.sigGen =  generatorFactory.createGenerator(params);

    }

    /**
     * Creates the table for stocks
     * @return
     */
    private Table createStocksTable(){
        Table stockTable=new Table();

        //add attributes
        stockTable.addContainerProperty("Stock", String.class,  null);
        stockTable.addContainerProperty("Current Price", Double.class,  null);
        stockTable.addContainerProperty("Action", SignalGenerator.StockStatus.class,  null);

        //load the signals for all the stocks
        for(String stock:getStocksList()){
            stockTable.addItem(new Object[]{stock,Double.valueOf(0.0), SignalGenerator.StockStatus.NA},stock);
        }


        return stockTable;
    }


    /**
     * Should return the set of stocks valid for the game
     * @return
     */
    protected abstract String[] getStocksList();

    private CssLayout createPanel(Component content) {
        CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
//        panel.setSizeFull();
        //todo - set a dynamic width to the panel
        panel.setWidth(400,Unit.PIXELS);

//        Button configure = new Button();
//        configure.addStyleName("configure");
//        configure.addStyleName("icon-cog");
//        configure.addStyleName("icon-only");
//        configure.addStyleName("borderless");
//        configure.setDescription("Configure");
//        configure.addStyleName("small");
//        configure.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                Notification.show("Not implemented in this demo");
//            }
//        });
//        panel.addComponent(configure);

        panel.addComponent(content);
        return panel;
    }

    /**
     * Loads the description for a decision making algorithm
     * @return
     */
    private Component getToolDescription(){
        VerticalLayout descriptionContainer=new VerticalLayout();

        SigGenMAType type=(SigGenMAType)this.indicators.getValue();

        String description ="<p>"+SigGenMAType.getDescription(type)+"</p>";

        Label descriptionLabel=new Label();
        descriptionContainer.addComponent(descriptionLabel);
        descriptionLabel.setContentMode(ContentMode.HTML);
        descriptionLabel.setValue(description);

        return descriptionContainer;
    }

    /**
     * Should provide the set of most recent initial data
     * @return
     */
    public abstract HashMap<String,HashMap<Date, Double>> getInitialData();

    /**
     * Gets called in a stock market update. Developer can use a custom implementation.
     * Should process the event as needed and call the <b>updateStockTable</b> to update
     * the table
     * @param event
     */
    @Override
    public abstract void eventOccurred(GameEvent event);

    /**
     * Used to updates a stock table entry
     * @param stock Security ID
     * @param date  Date/time
     * @param price Current price of the stock
     */
    protected void updateStockTable(String stock,Date date, Double price){
        try {
            this.sigGen.eventOccurred(new MarketClosedEvent(stock,date,price));
        } catch (AnalysisException e) {
            e.printStackTrace();
        }
        this.stockList.removeItem(stock);

        //add the entry
        this.stockList.addItem(new Object[]{stock,price, this.sigGen.getCurrentStatus(stock)},stock);

        //push the changes
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                getUI().push();
            }
        });
    }

    /**
     * Used to add the panel as an event listener to the backend
     */
    public abstract void addAsAListener();

}
