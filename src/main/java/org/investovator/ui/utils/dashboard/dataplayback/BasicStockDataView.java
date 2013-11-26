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
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataPlayerCommand;
import org.investovator.controller.command.dataplayback.GetDataUpToTodayCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.core.data.exeptions.DataNotFoundException;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.awt.*;
import java.util.*;
import java.util.Calendar;
import java.util.List;

/**
 * Should show a view like http://demo.vaadin.com/charts/#ForumTrends
 */
public abstract class BasicStockDataView extends DashboardPanel {

    VerticalLayout content;

    //charts to be shown
    protected Timeline mainChart;

    //
    private ComboBox stocksList;
    private NativeSelect dataItems;

    public BasicStockDataView() {
        this.content = new VerticalLayout();
        this.setHeight("100%");
//        content.setSizeFull();
    }


    @Override
    public void onEnter() {
        content.removeAllComponents();

        content.addComponent(setUpTopBar());
        mainChart=setUpChart();
        content.addComponent(mainChart);

//        this.setSizeFull();
        this.setContent(content);
    }

    private HorizontalLayout setUpTopBar(){
        HorizontalLayout components=new HorizontalLayout();
//        components.setSizeFull();

        //create the stocks drop down list
         stocksList=new ComboBox();
        components.addComponent(stocksList);
        stocksList.setImmediate(true);

        stocksList.setCaption("Stock");
        stocksList.setNullSelectionAllowed(false);
        for(String stock: DataPlaybackEngineStates.playingSymbols){
            stocksList.addItem(stock);
        }
        stocksList.select(stocksList.getItemIds().toArray()[0]);

        stocksList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                updateChart();

            }
        });

        //Data items list
        dataItems=new NativeSelect();
        components.addComponent(dataItems);
        dataItems.setCaption("Data: ");

        for(TradingDataAttribute attr:setSelectableAttributes()){
            dataItems.addItem(attr);
        }
        dataItems.setNullSelectionAllowed(false);
        dataItems.select(dataItems.getItemIds().toArray()[0]);
        dataItems.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                updateChart();

            }
        });

        dataItems.setImmediate(true);

        return components;
    }

    private Timeline setUpChart(){
        Timeline timeline=new Timeline();

        timeline.setSizeFull();
        timeline.setHeight(625,Unit.PIXELS);
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
        data = createIndexedContainer();
//
        // Add data sources
        timeline.addGraphDataSource(data,Timeline.PropertyId.TIMESTAMP,Timeline.PropertyId.VALUE);
        timeline.setGraphCaption(data, "Stock");
        timeline.setGraphOutlineColor(data, new Color(0x00, 0xb4, 0xf0));
        timeline.setGraphFillColor(data, null);
//        timeline.setVerticalAxisLegendUnit(data, "Price");

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
    private IndexedContainer createIndexedContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(Timeline.PropertyId.VALUE, Float.class,
                new Float(0));
        container.addContainerProperty(Timeline.PropertyId.TIMESTAMP,
                Date.class, null);



        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();


        //just the closing price is enough for now
        attributes.add((TradingDataAttribute)dataItems.getValue());

        try {
            //todo - remove quick hack
            GameController controller= GameControllerImpl.getInstance();
            //set the data player
            GetDataUpToTodayCommand command=new GetDataUpToTodayCommand(stocksList.getValue().toString(),
                    DataPlaybackEngineStates.gameStartDate,
                    attributes);
            controller.runCommand(DataPlaybackEngineStates.gameInstance,command);

            StockTradingData stockTradingData = command.getResult();

//            StockTradingData stockTradingData= DataPlayerFacade.getInstance().getDataUpToToday(
//                    stocksList.getValue().toString(),DataPlaybackEngineStates.gameStartDate,
//                    attributes);

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
                                get(dataItems.getValue())));




            }
        }
//        catch (DataAccessException e) {
//            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
//            e.printStackTrace();
//        } catch (DataNotFoundException e) {
//            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
        catch (CommandSettingsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CommandExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return container;
    }

    public  void updateChart(){

        mainChart.removeAllGraphDataSources();

        //recreate a data source
        IndexedContainer data;
        data = createIndexedContainer();
//
        // Add data sources
        mainChart.addGraphDataSource(data,Timeline.PropertyId.TIMESTAMP,Timeline.PropertyId.VALUE);
        mainChart.setGraphCaption(data, "Stock");
        mainChart.setGraphOutlineColor(data, new Color(0x00, 0xb4, 0xf0));
        mainChart.setGraphFillColor(data, null);
        mainChart.setVerticalAxisLegendUnit(data, "Price");

//        mainChart.setImmediate(true);


    }

}
