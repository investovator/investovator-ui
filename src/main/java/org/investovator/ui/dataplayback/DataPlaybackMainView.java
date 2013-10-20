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


package org.investovator.ui.dataplayback;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.OHLCDataPLayer;
import org.investovator.dataplaybackengine.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.events.EventManager;
import org.investovator.dataplaybackengine.events.StockEvent;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.exceptions.GameFinishedException;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.util.DataPLaybackEngineGameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.dataplayback.wizards.NewDataPlaybackGameWizard;

import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlaybackMainView extends Panel implements Observer {

    //decides the number of points shown in the ticker chart
    private static int TICKER_CHART_LENGTH = 10;

    //decides the number of points shown in the OHLC chart
    private static int OHLC_CHART_LENGTH = 10;

    OHLCDataPLayer ohlcPLayer;
    RealTimeDataPlayer realTimePlayer;

    //used in ticker data observing
    DataPlaybackMainView mySelf;

    //charts to be shown
    Chart ohlcChart;
    Chart tickerChart;

    Table stockPriceTable;

    //to store every component
    GridLayout content;

    public DataPlaybackMainView() {
        //set a link to this class
        mySelf = this;

        content = new GridLayout(3, 3);
        content.setSizeFull();

        ////for testing
        DataPlaybackEngineStates.currentGameMode = DataPLaybackEngineGameTypes.OHLC_BASED;
        this.setUpGame(true);


        //
        //
        ////

//        HorizontalLayout topBar=new HorizontalLayout();
//        HorizontalLayout topButtonContainer=new HorizontalLayout();
//        topButtonContainer.setStyleName("sidebar");
//        topBar.addComponent(topButtonContainer);
////        topBar.setSizeFull();
//        //to set the alignment of the buttons
//        topBar.setComponentAlignment(topButtonContainer, Alignment.TOP_RIGHT);
//        topButtonContainer.setSizeFull();
//
//        content.addComponent(topBar, 1, 0, 2, 0);
//        content.setComponentAlignment(topBar, Alignment.TOP_RIGHT);
//
//        HorizontalLayout chartContainer=new HorizontalLayout();
//        chartContainer.setWidth(95,Unit.PERCENTAGE);
////        chartContainer.setHeight(40,Unit.PERCENTAGE);
//
//
//        ohlcChart=buildOHLCChart();
//        tickerChart=buildTickerChart();
//
//        chartContainer.addComponent(tickerChart);
//        chartContainer.setComponentAlignment(tickerChart,Alignment.MIDDLE_CENTER);
//
//        Chart t0=QuickTest.getChart();
//
//
//
//        content.addComponent(chartContainer, 0, 1, 2, 1);
//        content.setComponentAlignment(chartContainer, Alignment.MIDDLE_CENTER);
//
//
//        //create the buttons
//        Button addGameButton=new Button("New Game");
//        Button playGameButton=new Button("Play Game");
//        Button pauseGameButton=new Button("Pause Game");
//        Button stopGameButton=new Button("Stop Game");
//        topButtonContainer.addComponent(addGameButton);
//        topButtonContainer.addComponent(playGameButton);
//        topButtonContainer.addComponent(pauseGameButton);
//        topButtonContainer.addComponent(stopGameButton);
//
//        //set alignments of the buttons
//        topButtonContainer.setComponentAlignment(addGameButton,Alignment.MIDDLE_RIGHT);
//        topButtonContainer.setComponentAlignment(playGameButton,Alignment.MIDDLE_RIGHT);
//        topButtonContainer.setComponentAlignment(pauseGameButton,Alignment.MIDDLE_RIGHT);
//        topButtonContainer.setComponentAlignment(stopGameButton,Alignment.MIDDLE_RIGHT);
//
//        //add the action listeners for buttons
//        addGameButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//
//                 startAddGameWizard();
//            }
//        });
//
//        playGameButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//
//                    //if an OHLC based game
//                    if(DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.OHLC_BASED){
//                        //TODO - what if there were multiple serieses?
//                        DataSeries series=(DataSeries)ohlcChart.getConfiguration().getSeries().get(0);
//                        try {
//                            series.add(new DataSeriesItem(ohlcPLayer.getToday(),ohlcPLayer.startGame()[0].
//                                    getData().get(TradingDataAttribute.PRICE)));
//                        } catch (GameAlreadyStartedException e) {
//                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                        }
//
//                    }
//                    //if a ticker based game
//                    else if(DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.TICKER_BASED){
//                        //TODO - what if there were multiple serieses?
//                        DataSeries series=(DataSeries)tickerChart.getConfiguration().getSeries().get(0);
//                        //TODO - how to set resolution?
//                            realTimePlayer.startPlayback(1);
//
//                    }
//
//
//
//            }
//        });
//
//          //TODO- implement this??
////        stopGameButton.addClickListener(new Button.ClickListener() {
////            @Override
////            public void buttonClick(Button.ClickEvent clickEvent) {
////                player.stopPlayback();
////            }
////        });
//
//
//
//
//        Button nextDayB=new Button("Next day");
//        nextDayB.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                DataSeries series=(DataSeries)ohlcChart.getConfiguration().getSeries().get(0);
//
//                    try {
//                        //TODO - what if there were multiple serieses?
//
//                        if (series.getData().size() > OHLC_CHART_LENGTH) {
//
//                            series.add(new DataSeriesItem(ohlcPLayer.getToday(),ohlcPLayer.playNextDay()[0].
//                                    getData().get(TradingDataAttribute.PRICE)),true,true);
//                        } else {
//                            series.add(new DataSeriesItem(ohlcPLayer.getToday(),ohlcPLayer.playNextDay()[0].
//                                    getData().get(TradingDataAttribute.PRICE)));
//                        }
//
//                        ohlcChart.setImmediate(true);
//
//
//
//                    } catch (GameFinishedException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//
//
//
//            }
//        });
//
//        //TODO
//        content.addComponent(nextDayB, 1, 2);
//        content.setComponentAlignment(nextDayB, Alignment.MIDDLE_CENTER);
//
//
//        Chart t1=QuickTest.getChart();
//
//
//        //add ticker chart TODO
////        content.addComponent(t1,0,2);
////        content.setComponentAlignment(t1,Alignment.MIDDLE_CENTER);
//
////        Chart t2=QuickTest.getChart();
//
////        content.addComponent(t1,1,2);
////        content.setComponentAlignment(t1,Alignment.MIDDLE_CENTER);
////
////        content.addComponent(t2,2,2);
////        content.setComponentAlignment(t2,Alignment.MIDDLE_CENTER);
//
//        this.setContent(content);
    }


    private Chart buildOHLCChart() {
        Chart chart = new Chart();
//        chart.setHeight("350px");
//        chart.setWidth("90%");

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        tooltip.setHeaderFormat("{point.key}");
        tooltip.setPointFormat("");
        tooltip.setFooterFormat("{series.name}: 	{point.y} EUR");

        Configuration configuration = new Configuration();
        configuration.setTooltip(tooltip);
        configuration.getChart().setType(ChartType.SPLINE);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        plotOptions.setEnableMouseTracking(false);
        configuration.setPlotOptions(plotOptions);

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                DataSeries ls = new DataSeries();
                ls.setName(stock);
                configuration.addSeries(ls);

            }
        }
        chart.drawChart(configuration);

        //disable trademark
        chart.getConfiguration().disableCredits();
        chart.getConfiguration().getTitle().setText("Stock Closing Prices");

        return chart;
    }

    private Chart buildTickerChart() {
        tickerChart = new Chart();
//        tickerChart.setHeight("350px");
//        tickerChart.setWidth("250px");
//        tickerChart.setSizeFull();

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        tooltip.setHeaderFormat("{point.key}");
        tooltip.setPointFormat("");
        tooltip.setFooterFormat("{series.name}: 	{point.y} EUR");

        Configuration configuration = new Configuration();
        configuration.setTooltip(tooltip);
        configuration.getChart().setType(ChartType.SPLINE);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        plotOptions.setEnableMouseTracking(false);
        configuration.setPlotOptions(plotOptions);

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                DataSeries ls = new DataSeries();
                ls.setName(stock);
                configuration.addSeries(ls);

            }
        }


        tickerChart.drawChart(configuration);
        //disable trademark
        tickerChart.getConfiguration().disableCredits();


        tickerChart.getConfiguration().setTitle("Real-time Stock Prices");
        return tickerChart;
    }

    private void startAddGameWizard() {
        // Create a sub-window and set the content
        Window subWindow = new Window("Create New Game");
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);

        // Put some components in it
        subContent.addComponent(new NewDataPlaybackGameWizard(subWindow, this));

        // set window characteristics
        subWindow.center();
        subWindow.setClosable(false);
        subWindow.setDraggable(false);
        subWindow.setResizable(false);
        subWindow.setModal(true);

        // Add it to the root component
        UI.getCurrent().addWindow(subWindow);
    }


    //used to setup the game initially(after the wizard)
    public void setUpGame(boolean initialization) {

        //clear everything
        content.removeAllComponents();

        //if the game type is OHLC
        if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.OHLC_BASED && !initialization) {
            //TODO - find a proper place to define attributes
            //define the attributes needed
            TradingDataAttribute attributes[] = new TradingDataAttribute[2];

            //just the closing price is enough for now
            attributes[0] = TradingDataAttribute.DAY;
            attributes[1] = TradingDataAttribute.PRICE;

            try {
                ohlcPLayer = new OHLCDataPLayer(DataPlaybackEngineStates.playingSymbols, attributes);
                ohlcPLayer.setStartDate(DataPlaybackEngineStates.gameStartDate);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //if the game type is ticker data based
        else if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.TICKER_BASED &&
                !initialization) {

            //TODO - find a proper place to define attributes
            //define the attributes needed
            TradingDataAttribute attributes[] = new TradingDataAttribute[3];

            //just the closing price is enough for now
            attributes[0] = TradingDataAttribute.DAY;
            attributes[1] = TradingDataAttribute.PRICE;
            attributes[2] = TradingDataAttribute.SHARES;

            realTimePlayer = new RealTimeDataPlayer(DataPlaybackEngineStates.playingSymbols,
                    DataPlaybackEngineStates.gameStartDate, attributes);

            //set myself as an observer
            realTimePlayer.setObserver(this);


        }

        //Top buttons
        HorizontalLayout topBar = new HorizontalLayout();
        HorizontalLayout topButtonContainer = new HorizontalLayout();
        topButtonContainer.setStyleName("sidebar");
        topBar.addComponent(topButtonContainer);
//        topBar.setSizeFull();
        //to set the alignment of the buttons
        topBar.setComponentAlignment(topButtonContainer, Alignment.TOP_RIGHT);
        topButtonContainer.setSizeFull();

        content.addComponent(topBar, 1, 0, 2, 0);
        content.setComponentAlignment(topBar, Alignment.TOP_RIGHT);

        //create the buttons
        Button addGameButton = new Button("New Game");
        Button playGameButton = new Button("Play Game");
        Button pauseGameButton = new Button("Pause Game");
        Button stopGameButton = new Button("Stop Game");
        topButtonContainer.addComponent(addGameButton);
        topButtonContainer.addComponent(playGameButton);
        topButtonContainer.addComponent(pauseGameButton);
        topButtonContainer.addComponent(stopGameButton);

        //set alignments of the buttons
        topButtonContainer.setComponentAlignment(addGameButton, Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(playGameButton, Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(pauseGameButton, Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(stopGameButton, Alignment.MIDDLE_RIGHT);

        //add the action listeners for buttons
        addGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                startAddGameWizard();
            }
        });

        playGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                //if an OHLC based game
                if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.OHLC_BASED) {
                    //TODO - what if there were multiple serieses?
                    DataSeries series = (DataSeries) ohlcChart.getConfiguration().getSeries().get(0);
                    try {
                        StockEvent[] events=ohlcPLayer.startGame();
                        series.add(new DataSeriesItem(ohlcPLayer.getToday(), events[0].
                                getData().get(TradingDataAttribute.PRICE)));
                        series = (DataSeries) ohlcChart.getConfiguration().getSeries().get(1);
                        series.add(new DataSeriesItem(ohlcPLayer.getToday(), events[1].
                                getData().get(TradingDataAttribute.PRICE)));

                        //update the table
                        BeanContainer<String,StockNamePriceBean> beans = (BeanContainer<String,StockNamePriceBean>)
                                stockPriceTable.getContainerDataSource();


                        if (stockPriceTable.isConnectorEnabled()) {
                            getSession().lock();
                            try {
                                beans.removeItem(events[0].getStockId());
                                beans.addBean(new StockNamePriceBean(events[0].getStockId(),events[0].getData().get(TradingDataAttribute.PRICE)));

                                beans.removeItem(events[1].getStockId());
                                beans.addBean(new StockNamePriceBean(events[1].getStockId(),events[1].getData().get(TradingDataAttribute.PRICE)));

                            } finally {
                                getSession().unlock();
                            }
                        }
                    } catch (GameAlreadyStartedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
                //if a ticker based game
                else if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.TICKER_BASED) {
                    //TODO - what if there were multiple serieses?
                    DataSeries series = (DataSeries) tickerChart.getConfiguration().getSeries().get(0);
                    //TODO - how to set resolution?
                    realTimePlayer.startPlayback(1);

                }


            }
        });

        //TODO- implement this??
//        stopGameButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                player.stopPlayback();
//            }
//        });


        Button nextDayB = new Button("Next day");
        nextDayB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                //get the events
                try {
                    StockEvent[] events = ohlcPLayer.playNextDay();
                    //iterate every event
                    for (StockEvent event : events) {
                        //iterate every series in the chart at the moment
                        for (Series series : ohlcChart.getConfiguration().getSeries()) {
                            DataSeries dSeries = (DataSeries) series;
                            //if there's a match
                            if (event.getStockId().equals(dSeries.getName())) {
                                if (ohlcChart.isConnectorEnabled()) {
                                    getSession().lock();
                                    try {
                                        if (dSeries.getData().size() > OHLC_CHART_LENGTH) {

                                            dSeries.add(new DataSeriesItem(event.getTime(),
                                                    event.getData().get(TradingDataAttribute.PRICE)), true, true);

                                        } else {
                                            dSeries.add(new DataSeriesItem(event.getTime(),
                                                    event.getData().get(TradingDataAttribute.PRICE)));

                                        }
                                        ohlcChart.setImmediate(true);

                                    } finally {
                                        getSession().unlock();
                                    }
                                }


                            }

                        }

                        //update the table
                        BeanContainer<String,StockNamePriceBean> beans = (BeanContainer<String,StockNamePriceBean>)
                                stockPriceTable.getContainerDataSource();


                        if (stockPriceTable.isConnectorEnabled()) {
                            getSession().lock();
                            try {
                                beans.removeItem(event.getStockId());
                                beans.addBean(new StockNamePriceBean(event.getStockId(),event.getData().get(TradingDataAttribute.PRICE)));
                            } finally {
                                getSession().unlock();
                            }
                        }

                    }


                } catch (GameFinishedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


//
//                DataSeries series=(DataSeries)ohlcChart.getConfiguration().getSeries().get(0);
//
//                try {
//                    //TODO - what if there were multiple serieses?
//
//                    if (series.getData().size() > OHLC_CHART_LENGTH) {
//
//                        series.add(new DataSeriesItem(ohlcPLayer.getToday(),ohlcPLayer.playNextDay()[0].
//                                getData().get(TradingDataAttribute.PRICE)),true,true);
//                    } else {
//                        series.add(new DataSeriesItem(ohlcPLayer.getToday(),ohlcPLayer.playNextDay()[0].
//                                getData().get(TradingDataAttribute.PRICE)));
//                    }
//
//                    ohlcChart.setImmediate(true);
//
//
//
//                } catch (GameFinishedException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }


            }
        });

        content.addComponent(nextDayB, 1, 2);
        content.setComponentAlignment(nextDayB, Alignment.MIDDLE_CENTER);

        //Main chart
        HorizontalLayout chartContainer = new HorizontalLayout();
        chartContainer.setWidth(95, Unit.PERCENTAGE);

        //if the game type is OHLC
        if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.OHLC_BASED) {
            ohlcChart = buildOHLCChart();
            chartContainer.addComponent(ohlcChart);
            chartContainer.setComponentAlignment(ohlcChart, Alignment.MIDDLE_CENTER);
        }
        //if the game type is ticker data based
        else if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.TICKER_BASED) {
            tickerChart = buildTickerChart();
            chartContainer.addComponent(tickerChart);
            chartContainer.setComponentAlignment(tickerChart, Alignment.MIDDLE_CENTER);

        }
        content.addComponent(chartContainer, 0, 1, 2, 1);
        content.setComponentAlignment(chartContainer, Alignment.MIDDLE_CENTER);

        //Stock price table
        stockPriceTable=(Table)setupStockPriceTable();
        content.addComponent(stockPriceTable,0,2);
        content.setComponentAlignment(stockPriceTable,Alignment.BOTTOM_LEFT);



        this.setContent(content);


    }

    public Component setupStockPriceTable(){

        BeanContainer<String,StockNamePriceBean> beans =
                new BeanContainer<String,StockNamePriceBean>(StockNamePriceBean.class);
        beans.setBeanIdProperty("stockID");
        //if the game is initialized
        if(DataPlaybackEngineStates.playingSymbols!=null){
            for(String stock:DataPlaybackEngineStates.playingSymbols){
                beans.addBean(new StockNamePriceBean(stock,0));
            }
        }
        Table table=new Table("Stock Prices",beans);

        return table;
    }


    @Override
    public void update(Observable o, Object arg) {
        //if this is a stock price update
        if (arg instanceof StockEvent) {
            final StockEvent event = (StockEvent) arg;

            //iterate every series in the chart at the moment
            for (Series series : tickerChart.getConfiguration().getSeries()) {
                DataSeries dSeries = (DataSeries) series;
                //if this series matches the stock events stock
                if (dSeries.getName().equalsIgnoreCase(event.getStockId())) {

                    if (tickerChart.isConnectorEnabled()) {
                        getSession().lock();
                        try {
                            if (dSeries.getData().size() > TICKER_CHART_LENGTH) {

                                dSeries.add(new DataSeriesItem(event.getTime(), event.getData().get(TradingDataAttribute.PRICE)), true, true);

                            } else {
                                dSeries.add(new DataSeriesItem(event.getTime(), event.getData().get(TradingDataAttribute.PRICE)));

                            }
                            tickerChart.setImmediate(true);

                        } finally {
                            getSession().unlock();
                        }
                    }


                }

            }

            //update the table
            BeanContainer<String,StockNamePriceBean> beans = (BeanContainer<String,StockNamePriceBean>)
                    stockPriceTable.getContainerDataSource();


            if (stockPriceTable.isConnectorEnabled()) {
                getSession().lock();
                try {
                    beans.removeItem(event.getStockId());
                    beans.addBean(new StockNamePriceBean(event.getStockId(),event.getData().get(TradingDataAttribute.PRICE)));
                } finally {
                    getSession().unlock();
                }
            }




//            //TODO - only updates for GOOG stocks
//            if("GOOG".equalsIgnoreCase(event.getStockId())){
//                if (tickerChart.isConnectorEnabled()) {
//                    getSession().lock();
//                    try {
//                        DataSeries series = (DataSeries) tickerChart.getConfiguration().getSeries().get(0);
//
//                        if (series.getData().size() > TICKER_CHART_LENGTH) {
//
//                            series.add(new DataSeriesItem(event.getTime(), event.getData().get(TradingDataAttribute.PRICE)), true, true);
//
//                        } else {
//                            series.add(new DataSeriesItem(event.getTime(), event.getData().get(TradingDataAttribute.PRICE)));
//
//                        }
//                        tickerChart.setImmediate(true);
//                    } finally {
//                        getSession().unlock();
//                    }
//                }
//
//            }


        }
        //if the game has stopped
        else if (arg == EventManager.RealTimePlayerStates.GAME_OVER) {
            //TODO - how to handle this?
        }


    }
}
