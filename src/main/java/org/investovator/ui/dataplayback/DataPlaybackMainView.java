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
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.OHLCDataPLayer;
import org.investovator.dataplaybackengine.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.events.EventManager;
import org.investovator.dataplaybackengine.events.StockEvent;
import org.investovator.dataplaybackengine.exceptions.*;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.util.DataPLaybackEngineGameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.dataplayback.wizards.NewDataPlaybackGameWizard;

import java.text.ParseException;
import java.util.ArrayList;
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
    Chart stockPieChart;

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

        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%e. %b", "%b"));

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

        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%e. %b", "%b"));

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
            ArrayList<TradingDataAttribute> attributes = new ArrayList<TradingDataAttribute>();

            //just the closing price is enough for now
            attributes.add(TradingDataAttribute.DAY);
            attributes.add(TradingDataAttribute.PRICE);

            try {
                ohlcPLayer = new OHLCDataPLayer(DataPlaybackEngineStates.playingSymbols,
                        attributes,TradingDataAttribute.PRICE);
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
            ArrayList<TradingDataAttribute> attributes= new ArrayList<TradingDataAttribute>();

            //just the closing price is enough for now
            attributes.add(TradingDataAttribute.DAY);
            attributes.add(TradingDataAttribute.PRICE);
            attributes.add(TradingDataAttribute.SHARES);

            realTimePlayer = new RealTimeDataPlayer(DataPlaybackEngineStates.playingSymbols,
                    DataPlaybackEngineStates.gameStartDate, attributes,TradingDataAttribute.PRICE);

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
                        //join the game
                        ohlcPLayer.joinGame();
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
                                beans.addBean(new StockNamePriceBean(events[0].getStockId(),
                                        events[0].getData().get(TradingDataAttribute.PRICE)));

                                beans.removeItem(events[1].getStockId());
                                beans.addBean(new StockNamePriceBean(events[1].getStockId(),
                                        events[1].getData().get(TradingDataAttribute.PRICE)));

                            } finally {
                                getSession().unlock();
                            }
                        }
                    } catch (GameAlreadyStartedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (UserAlreadyJoinedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
                //if a ticker based game
                else if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.TICKER_BASED) {
                    try {
                        realTimePlayer.joinGame();
                    } catch (UserAlreadyJoinedException e) {
                        Notification.show(e.getMessage());
                    }
                    //TODO - what if there were multiple serieses?
                    DataSeries series = (DataSeries) tickerChart.getConfiguration().getSeries().get(0);
                    //TODO - how to set resolution?
                    realTimePlayer.startPlayback(3);

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
        stockPriceTable=setupStockPriceTable();
        content.addComponent(stockPriceTable,0,2);
        content.setComponentAlignment(stockPriceTable,Alignment.BOTTOM_LEFT);

        //buy-sell window
        Component buySellWindow=setupBuySellForm();
        content.addComponent(buySellWindow,1,2);
//        content.setComponentAlignment(buySellWindow,Alignment.BOTTOM_CENTER);

        //pie-chart
        stockPieChart =setupPieChart();
        content.addComponent(stockPieChart,2,2);



        this.setContent(content);


    }

    private Component setupBuySellForm(){
        VerticalLayout formContent=new VerticalLayout();

        FormLayout form=new FormLayout();

        //stocks list
        final ComboBox stocksList=new ComboBox();
        stocksList.setCaption("Stock");
        stocksList.setNullSelectionAllowed(false);
        if(DataPlaybackEngineStates.playingSymbols!=null){
            for(String stock:DataPlaybackEngineStates.playingSymbols){
                stocksList.addItem(stock);
            }
        }
        stocksList.setWidth("75%");

        //side
        final NativeSelect orderSide=new NativeSelect();
        orderSide.setCaption("Side");
        orderSide.addItem(OrderType.BUY);
        orderSide.addItem(OrderType.SELL);
        orderSide.setWidth("90%");
        orderSide.select(OrderType.BUY);
        orderSide.setNullSelectionAllowed(false);
        orderSide.setImmediate(true);

        //Quantity
        final TextField quantity=new TextField("Amount");
        quantity.setWidth("75%");


        form.addComponent(stocksList);
        form.addComponent(orderSide);
        form.addComponent(quantity);

        formContent.addComponent(form);

        HorizontalLayout buttonsBar=new HorizontalLayout();
        final Button buySellButton=new Button("Buy");
        buySellButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

//                Notification.show(stocksList.getValue().toString() + "--" + orderSide.getValue().toString() + "--" + quantity.getValue().toString());
//                System.out.println();

                if (DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.OHLC_BASED){
                    try {
                        Boolean status=ohlcPLayer.executeOrder(stocksList.getValue().toString(),
                                Integer.parseInt(quantity.getValue().toString()),((OrderType)orderSide.getValue()));
                        Notification.show(status.toString());
                    } catch (InvalidOrderException e) {
                        Notification.show(e.getMessage());
                    } catch (UserJoinException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }

                if (DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.TICKER_BASED){
                    try {
                        Boolean status=realTimePlayer.executeOrder(stocksList.getValue().toString(),
                                Integer.parseInt(quantity.getValue().toString()), ((OrderType) orderSide.getValue()));
                        Notification.show(status.toString());

                    } catch (InvalidOrderException e) {
                        Notification.show(e.getMessage());
                    } catch (UserJoinException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }

            }});

        orderSide.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if(valueChangeEvent.getProperty().getValue()==OrderType.BUY){
                    buySellButton.setCaption("Buy");
                }
                else if(valueChangeEvent.getProperty().getValue()==OrderType.SELL){
                    buySellButton.setCaption("Sell");
                }
            }
        });

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
                                beans.addBean(new StockNamePriceBean(event.getStockId(),
                                        event.getData().get(TradingDataAttribute.PRICE)));
                            } finally {
                                getSession().unlock();
                            }
                        }

                    }


                } catch (GameFinishedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        });

        //only add if it's an OHLC game
        if(DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.OHLC_BASED){

            buttonsBar.addComponent(nextDayB);
        }
        buttonsBar.addComponent(buySellButton);
        formContent.addComponent(buttonsBar);
        formContent.setComponentAlignment(buttonsBar,Alignment.BOTTOM_RIGHT);
        //content.setComponentAlignment(nextDayB, Alignment.MIDDLE_CENTER);

        return formContent;
    }

    private Table setupStockPriceTable(){

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

        //set the column order
        table.setVisibleColumns(new Object[]{"stockID", "price"});

        return table;
    }

    private Chart setupPieChart(){
//        VerticalLayout layout=new VerticalLayout();

        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("Portfolio Summary");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setColor(new SolidColor(0, 0, 0));
        dataLabels.setConnectorColor(new SolidColor(0, 0, 0));
        dataLabels
                .setFormatter("''+ this.point.name +': '+ this.percentage +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        //if the stock items has been set
        if(DataPlaybackEngineStates.playingSymbols!=null){
            for(String stock:DataPlaybackEngineStates.playingSymbols){
                series.add(new DataSeriesItem(stock, 0));
            }
        }

//        series.add(new DataSeriesItem("Firefox", 45.0));
//        series.add(new DataSeriesItem("IE", 26.8));
//        DataSeriesItem chrome = new DataSeriesItem("Chrome", 12.8);
//        chrome.setSliced(true);
//        chrome.setSelected(true);
//        series.add(chrome);
//        series.add(new DataSeriesItem("Safari", 8.5));
//        series.add(new DataSeriesItem("Opera", 6.2));
//        series.add(new DataSeriesItem("Others", 0.7));
        conf.setSeries(series);

        chart.drawChart(conf);
        chart.setWidth("90%");
        chart.setHeight(70,Unit.MM);


        return chart;
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

                                dSeries.add(new DataSeriesItem(event.getTime(),
                                        event.getData().get(TradingDataAttribute.PRICE)), true, true);

                            } else {
                                dSeries.add(new DataSeriesItem(event.getTime(),
                                        event.getData().get(TradingDataAttribute.PRICE)));

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
                    beans.addBean(new StockNamePriceBean(event.getStockId(),
                            event.getData().get(TradingDataAttribute.PRICE)));
                } finally {
                    getSession().unlock();
                }
            }

            //update the pie-chart

            //since we know that there's only one data series
                DataSeries dSeries = (DataSeries) stockPieChart.getConfiguration().getSeries().get(0);

                //find the matching Data item
//            DataSeriesItem item=dSeries.get(event.getStockId());
//                    if(item.getName().equalsIgnoreCase(event.getStockId())){
                        if (stockPieChart.isConnectorEnabled()) {
                            getSession().lock();
                            try {
                                //TODO - assumes there's only one stock from each type

                                int total=0;
                                //get the values from the stock price table
                                for(String beanId:beans.getItemIds()){
                                    //add the new price for the updated stock
                                    if(beanId.equalsIgnoreCase(event.getStockId())){
                                       total+=event.getData().get(TradingDataAttribute.PRICE);

                                    }
                                    else{
                                        total+=beans.getItem(beanId).getBean().getPrice();
                                    }


                                }

                                //remove every stock percentage
//                                for(DataSeriesItem item:dSeries.getData()){
//                                    dSeries.remove(item);
//                                }

//                                System.out.println("+++++++++++++++++++++++++++++++++++");


                                int k=0;
                                float totalPer=0;
                                //add the new percentages
                                for(String beanId:beans.getItemIds()){
                                    if(k==beans.getItemIds().size()-1){
                                        dSeries.add(new DataSeriesItem(beanId,100-totalPer));
                                    }
                                    else{
                                        dSeries.add(new DataSeriesItem(beanId,
                                                ((beans.getItem(beanId).getBean().getPrice())/total)*100));



//                                        System.out.println(beanId+"-->"+((beans.getItem(beanId).getBean().getPrice())/total)*100);
                                        totalPer+=((beans.getItem(beanId).getBean().getPrice())/total)*100;

                                    }

//                                    dSeries.add(new DataSeriesItem(beanId,
//                                            ((beans.getItem(beanId).getBean().getPrice())/total)*100));
//
//                                    System.out.println(beanId+"-->"+((beans.getItem(beanId).getBean().getPrice())/total)*100);
                                    //k+=((beans.getItem(beanId).getBean().getPrice())/total)*100;
                                    k++;
                                }
//                                System.out.println("+++++++++++++++++++++++++++++++++++");
//                                System.out.println(k);


                                stockPieChart.setImmediate(true);

//                                System.out.println(event.getStockId());
//                                System.out.println((event.getData().get(TradingDataAttribute.PRICE))/10);
//                                System.out.println("---------------------");

                            } finally {
                                getSession().unlock();
                            }
                        }




//                    }






        }
        //if the game has stopped
        else if (arg == EventManager.RealTimePlayerStates.GAME_OVER) {
            //TODO - how to handle this?
        }


    }
}
