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


package org.investovator.ui.utils.dashboard.dataplayback;

/**
 * @author: ishan
 * @version: ${Revision}
 */

import com.vaadin.addon.timeline.Timeline;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataUpToTodayCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.UIConstants;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Should show a view like http://demo.vaadin.com/charts/#ForumTrends
 */
public abstract class BasicStockDataView extends DashboardPanel {

    VerticalLayout content;

    //charts to be shown
    protected Timeline mainChart;

    //shows the stocks list
    private ComboBox availableStocksList;
    //shows the attribute list
    private NativeSelect availableDataItems;

    //shows the already plotted stocks list
    private ComboBox plottedItemsList;

    //to keep track of the added graphs
    //stock - [attribute - data sink]
    HashMap<String,HashMap<String,IndexedContainer>> graphs;

    public BasicStockDataView() {
        this.content = new VerticalLayout();
        this.setHeight("100%");
        this.graphs=new HashMap<>();
//        content.setSizeFull();
    }


    @Override
    public void onEnter() {

        //check if a game instance exists
        if((Session.getCurrentGameInstance()==null)){
            getUI().getNavigator().navigateTo(UIConstants.USER_VIEW);
            return;
        }
        //if this is first time entering this view
        if(this.graphs==null){
            this.graphs=new HashMap<>();
        }

        content.removeAllComponents();

        content.addComponent(setUpTopBar());
        mainChart=setUpChart();
        content.addComponent(mainChart);


//        this.setSizeFull();
        this.setContent(content);
    }

    private HorizontalLayout setUpTopBar(){
        HorizontalLayout components=new HorizontalLayout();
        components.setWidth("100%");

        HorizontalLayout addChartComponents=new HorizontalLayout();
        components.addComponent(addChartComponents);

        //create the stocks drop down list
         availableStocksList =new ComboBox();
        addChartComponents.addComponent(availableStocksList);
        availableStocksList.setImmediate(true);

//        availableStocksList.setCaption("Stock");
        availableStocksList.setNullSelectionAllowed(false);
        for(String stock: DataPlaybackEngineStates.playingSymbols){
            availableStocksList.addItem(stock);
        }
        availableStocksList.select(availableStocksList.getItemIds().toArray()[0]);

        //available attributes list
        availableDataItems =new NativeSelect();
        addChartComponents.addComponent(availableDataItems);
//        availableDataItems.setCaption("Data: ");

        for(TradingDataAttribute attr:setSelectableAttributes()){
            availableDataItems.addItem(attr);
        }
        availableDataItems.setNullSelectionAllowed(false);
        availableDataItems.select(availableDataItems.getItemIds().toArray()[0]);

        availableDataItems.setImmediate(true);

        //add chart button
        Button addChartButton=new Button("Add");
        addChartComponents.addComponent(addChartButton);
        addChartButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addPlotToChart();
            }
        });

        //Remove charts section
        HorizontalLayout removeChartsComponents=new HorizontalLayout();
        components.addComponent(removeChartsComponents);

        //plotted charts list
        this.plottedItemsList=new ComboBox();
        removeChartsComponents.addComponent(this.plottedItemsList);
        this.plottedItemsList.setImmediate(true);
//        this.plottedItemsList.setCaption("Drawn Charts");
        this.plottedItemsList.setNullSelectionAllowed(true);

        //remove plot button
        Button removeChartButton=new Button("Remove");
        removeChartsComponents.addComponent(removeChartButton);
        removeChartButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                removePlotFromChart();
            }
        });

        //clear all button
        Button clearAllButton=new Button("Clear All");
        removeChartsComponents.addComponent(clearAllButton);

        clearAllButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                clearTimeline();
            }
        });

        //set the alignments
        components.setComponentAlignment(addChartComponents,Alignment.MIDDLE_CENTER);
        components.setComponentAlignment(removeChartsComponents,Alignment.MIDDLE_CENTER);
        components.setHeight(50,Unit.PIXELS);



        return components;
    }

    private Timeline setUpChart(){
        Timeline timeline=new Timeline();

        timeline.setSizeFull();
        timeline.setHeight(630,Unit.PIXELS);
//        timeline.setWidth(100,Unit.PERCENTAGE);
        timeline.setId("timeline");
        timeline.setUniformBarThicknessEnabled(true);
        timeline.setImmediate(true);

        timeline.setChartMode(Timeline.ChartMode.LINE);

        //disable bar chart
        timeline.setChartModeVisible(Timeline.ChartMode.BAR,false);

        //hide zoom levels
        timeline.setZoomLevelsVisible(false);


        IndexedContainer data;
        String stock= availableStocksList.getValue().toString();
        TradingDataAttribute attr=(TradingDataAttribute) availableDataItems.getValue();
        data = createIndexedContainer(stock,attr );
////
//        // Add data sources
        timeline.addGraphDataSource(data,Timeline.PropertyId.TIMESTAMP,Timeline.PropertyId.VALUE);
        timeline.setGraphCaption(data, stock+"-"+attr);
        Color color=createRandomColour();
        timeline.setGraphOutlineColor(data, color);
        timeline.setGraphFillColor(data, null);
        timeline.setBrowserOutlineColor(data,color);
        timeline.setBrowserFillColor(data,null);
        timeline.setVerticalAxisLegendUnit(data, "Price");

        //set the already plotted items
        this.plottedItemsList.addItem(stock+"-"+attr);

        //mark as added
        HashMap<String, IndexedContainer> map=new HashMap<>();
        map.put(attr.toString(),data);
        this.graphs.put(stock,map);

        // Set the date range
        if(DataPlaybackEngineStates.gameConfig.getPlayerType()== PlayerTypes.DAILY_SUMMARY_PLAYER){
//            timeline.setVisibleDateRange(DataPlaybackEngineStates.gameStartDate,
//                    DateUtils.incrementTimeByDays(5,DataPlaybackEngineStates.gameStartDate));

        }
        else if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.REAL_TIME_DATA_PLAYER){
            timeline.setVisibleDateRange(DataPlaybackEngineStates.gameStartDate,
                    DateUtils.incrementTimeBySeconds(60, DataPlaybackEngineStates.gameStartDate));
        }


//         timeline.setSizeFull();

        return timeline;
    }

    /**
     * Override this to set the viewable data in the graphs
     * @return
     */
    public abstract TradingDataAttribute[] setSelectableAttributes();
    /**
     * Creates an indexed container with two properties: value and timestamp.
     *
     * @return a container with "value, timestamp" items.
     */
    private IndexedContainer createIndexedContainer(String stock,TradingDataAttribute attribute) {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(Timeline.PropertyId.VALUE, Float.class,
                new Float(0));
        container.addContainerProperty(Timeline.PropertyId.TIMESTAMP,
                Date.class, null);



        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();
        attributes.add(attribute);

        try {
            GameController controller= GameControllerImpl.getInstance();
            //set the data player
            GetDataUpToTodayCommand command=new GetDataUpToTodayCommand(stock,
                    DataPlaybackEngineStates.gameStartDate,
                    attributes);
            controller.runCommand(Session.getCurrentGameInstance(),command);

            StockTradingData stockTradingData = command.getResult();

            //add the data
            //sort first
            Collection<Date> unsorted = stockTradingData.getTradingData().keySet();
            List<Date> list=new ArrayList<Date>(unsorted);
            Collections.sort(list);

            for(Date date:list){
                // Create  a point in time
                Item item = container.addItem(date);

                // Set the timestamp property
                item.getItemProperty(Timeline.PropertyId.TIMESTAMP)
                        .setValue(date);


                // Set the value property
                item.getItemProperty(Timeline.PropertyId.VALUE)
                        .setValue(Float.parseFloat(stockTradingData.getTradingData().get(date).
                                get(availableDataItems.getValue())));




            }
        }
        catch (CommandSettingsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CommandExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return container;
    }


    /**
     * Adds the current selected
     */
    public void addPlotToChart(){

        //selected stock
        String stock= availableStocksList.getValue().toString();
        //selected attribute
        TradingDataAttribute attr=(TradingDataAttribute) availableDataItems.getValue();

        //do nothing if the graph is already added
        if(this.graphs.containsKey(stock) && this.graphs.get(stock).containsKey(attr.toString())){
            return;
        }

        IndexedContainer data=createIndexedContainer(stock,attr);

        mainChart.addGraphDataSource(data,Timeline.PropertyId.TIMESTAMP,Timeline.PropertyId.VALUE);
        mainChart.setGraphCaption(data, stock+"-"+attr);
        Color color=createRandomColour();
        mainChart.setGraphOutlineColor(data,color );
        mainChart.setGraphFillColor(data, null);
        mainChart.setBrowserOutlineColor(data,color);
        mainChart.setBrowserFillColor(data,null);
        mainChart.setVerticalAxisLegendUnit(data, "Price");

        //mark as added
        HashMap<String, IndexedContainer> map=new HashMap<>();
        map.put(attr.toString(),data);
        this.graphs.put(stock,map);

        //set the already plotted items
        this.plottedItemsList.addItem(stock+"-"+attr);
    }

    /**
     * removes the selected plot from timeline
     */
    public void removePlotFromChart(){
        //do nothing if no chart is selected
        if(this.plottedItemsList.getValue()==null){
            return;
        }

        //only break to two parts
        String value=this.plottedItemsList.getValue().toString();
        String selected[]=value.split("-");
        IndexedContainer container=this.graphs.get(selected[0]).get(selected[1]);

        //remove from chart
        this.mainChart.removeGraphDataSource(container);

        //remove from the map
        this.graphs.get(selected[0]).remove(selected[1]);
        //remove from list
        this.plottedItemsList.removeItem(value);


    }

    /**
     * Returns a random colour
     * @return
     */
    private Color createRandomColour(){
        Random rand = new Random();
        // Java 'Color' class takes 3 floats, from 0 to 1.
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        return new Color(r, g, b);

    }

    /**
     * clears the timeline
     */
    private void clearTimeline(){
        this.mainChart.removeAllGraphDataSources();
        this.graphs.clear();
        this.plottedItemsList.removeAllItems();
    }


}
