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
import com.vaadin.ui.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.DataPlayer;
import org.investovator.dataplaybackengine.OHLCDataPLayer;
import org.investovator.dataplaybackengine.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.events.EventManager;
import org.investovator.dataplaybackengine.events.StockEvent;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.dataplayback.util.DataPLaybackEngineGameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.dataplayback.wizards.NewDataPlaybackGameWizard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlaybackMainView extends Panel implements Observer {

    //decides the number of points shown in the ticker chart
    private static int TICKER_CHART_LENGTH=10;

    //decides the number of points shown in the OHLC chart
    private static int OHLC_CHART_LENGTH=10;

    OHLCDataPLayer ohlcPLayer;
    RealTimeDataPlayer realTimePlayer;

    //stocks to playback
//    String[] stocks;

    //the day the game starts
//    Date startDate;
    //the day the game ends
    Date endingDate;
    //Date format used in the game
    String dateFormat= DateUtils.DATE_FORMAT_1;

    //used for counting data iteration number
    int timeTracker =0;

    //used in ticker data observing
    DataPlaybackMainView mySelf;

    Chart tickerChart;

    public DataPlaybackMainView() {
        //set a link to this class
        mySelf=this;

        //add the main graph
        final VerticalLayout panelContent = new VerticalLayout();

        HorizontalLayout topBar=new HorizontalLayout();
        HorizontalLayout topButtonContainer=new HorizontalLayout();
        topButtonContainer.setStyleName("sidebar");
        topBar.addComponent(topButtonContainer);
        //to set the alignment of the buttons
        topBar.setComponentAlignment(topButtonContainer,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setSizeFull();

        panelContent.addComponent(topBar);
        final Chart ohlcChart=buildMainChart();
        panelContent.addComponent(ohlcChart);

        //create the buttons
        Button addGameButton=new Button("New Game");
        Button playGameButton=new Button("Play Game");
        Button pauseGameButton=new Button("Pause Game");
        Button stopGameButton=new Button("Stop Game");
        topButtonContainer.addComponent(addGameButton);
        topButtonContainer.addComponent(playGameButton);
        topButtonContainer.addComponent(pauseGameButton);
        topButtonContainer.addComponent(stopGameButton);

        //set alignments of the buttons
        topButtonContainer.setComponentAlignment(addGameButton,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(playGameButton,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(pauseGameButton,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(stopGameButton,Alignment.MIDDLE_RIGHT);
//        topButtonContainer.setSpacing(false);

        //add the action listeners for buttons
        addGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
//                //TODO- fix this static value
//                String[] stocks=new String[2];
//                stocks[0]="GOOG";
//                stocks[1]="APPL";
//                player=new DataPlayer(stocks);
//
//                //add as an observer
//                player.setObserver(mySelf);

                //test wizard
                 startAddGameWizard();
            }
        });
        playGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String date="2012-10-3-19-45-"+Integer.toString(timeTracker);
                //convert date string to a real date object
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss"); //should be in format year-month-date-24hr-minute-second
                try {
                    Date eventTime =format.parse(date);

                    //if an OHLC based game
                    if(DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.OHLC_BASED){
                        //TODO - what if there were multiple serieses?
                        DataSeries series=(DataSeries)ohlcChart.getConfiguration().getSeries().get(0);
                        try {
                            series.add(new DataSeriesItem(ohlcPLayer.getToday(),ohlcPLayer.startGame()[0].
                                    getData().get(TradingDataAttribute.PRICE)));
                        } catch (GameAlreadyStartedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                    //if a ticker based game
                    else if(DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.TICKER_BASED){
                        //TODO - what if there were multiple serieses?
                        DataSeries series=(DataSeries)tickerChart.getConfiguration().getSeries().get(0);
                        //TODO - how to set resolution?
                            realTimePlayer.startPlayback(1);

                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeTracker++;

            }
        });


//        stopGameButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                player.stopPlayback();
//            }
//        });




        Button nextDayB=new Button("Next day");
        nextDayB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DataSeries series=(DataSeries)ohlcChart.getConfiguration().getSeries().get(0);

                String date="2012-10-3-19-45-"+Integer.toString(timeTracker);
                //convert date string to a real date object
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss"); //should be in format year-month-date-24hr-minute-second
                try {
                    Date eventTime =format.parse(date);

                    //TODO - implement properly
//
//
//                    if (series.getData().size() > TICKER_CHART_LENGTH) {
//
//                        series.add(new DataSeriesItem(eventTime,player.getOHLCPrice("Goog",date)), true, true);
//                    } else{
//                        series.add(new DataSeriesItem(eventTime,player.getOHLCPrice("Goog",date)));
//                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                timeTracker++;

            }
        });


        panelContent.addComponent(nextDayB);

        //add ticker chart
        panelContent.addComponent(buildTickerChart());

        this.setContent(panelContent);
    }

//    public void setStocks(String[] stocks) {
//        this.stocks = stocks;
//    }

    private Chart buildMainChart(){
        Chart chart = new Chart();
        chart.setHeight("350px");
        chart.setWidth("90%");

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

        DataSeries ls = new DataSeries();
        ls.setName("GOOG");

        configuration.addSeries(ls);

        //disable trademark
        chart.getConfiguration().disableCredits();
        chart.getConfiguration().getTitle().setText("Stock Closing Prices");

        chart.drawChart(configuration);
        return chart;
    }

    private Chart buildTickerChart(){
        tickerChart = new Chart();
        tickerChart.setHeight("350px");
        tickerChart.setWidth("90%");

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

        DataSeries ls = new DataSeries();
        ls.setName("GOOG");
        configuration.addSeries(ls);


        //disable trademark
        tickerChart.getConfiguration().disableCredits();

        tickerChart.getConfiguration().getTitle().setText("Real-time Stock Prices");


        tickerChart.drawChart(configuration);
        return tickerChart;
    }

    private void startAddGameWizard(){
        // Create a sub-window and set the content
        Window subWindow = new Window("Create New Game");
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);

        // Put some components in it
        subContent.addComponent(new NewDataPlaybackGameWizard(subWindow,this));

        // set window characteristics
        subWindow.center();
        subWindow.setClosable(false);
        subWindow.setDraggable(false);
        subWindow.setResizable(false);
        subWindow.setModal(true);

        // Add it to the root component
        UI.getCurrent().addWindow(subWindow);
    }

//    public DataPlayer getDataPlayer(){
//        return player;
//    }

    //used to setup the game initially(after the wizard)
    public void setUpGame() {
        //if the game type is OHLC
        if (DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.OHLC_BASED) {
            //TODO - find a proper place to define attributes
            //define the attributes needed
            TradingDataAttribute attributes[]=new TradingDataAttribute[2];

            //just the closing price is enough for now
            attributes[0]=TradingDataAttribute.DAY;
            attributes[1]=TradingDataAttribute.PRICE;

            try {
                ohlcPLayer=new OHLCDataPLayer(DataPlaybackEngineStates.playingSymbols,attributes);
                ohlcPLayer.setStartDate(DataPlaybackEngineStates.gameStartDate);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //if the game type is ticker data based
        else if(DataPlaybackEngineStates.currentGameMode == DataPLaybackEngineGameTypes.TICKER_BASED){

            //TODO - find a proper place to define attributes
            //define the attributes needed
            TradingDataAttribute attributes[]=new TradingDataAttribute[3];

            //just the closing price is enough for now
            attributes[0]=TradingDataAttribute.DAY;
            attributes[1]=TradingDataAttribute.PRICE;
            attributes[2]=TradingDataAttribute.SHARES;

            realTimePlayer=new RealTimeDataPlayer(DataPlaybackEngineStates.playingSymbols,DataPlaybackEngineStates.gameStartDate,attributes);

            //set myself as an observer
            realTimePlayer.setObserver(this);


        }

    }


    @Override
    public void update(Observable o, Object arg) {
        //if this is a stock price update
        if(arg instanceof StockEvent){
            final StockEvent event=(StockEvent) arg;

            //TODO - only updates for GOOG stocks
            if("GOOG".equalsIgnoreCase(event.getStockId())){
                if (tickerChart.isConnectorEnabled()) {
                    getSession().lock();
                    try {
                        DataSeries series = (DataSeries) tickerChart.getConfiguration().getSeries().get(0);

                        if (series.getData().size() > TICKER_CHART_LENGTH) {

                            series.add(new DataSeriesItem(event.getTime(), event.getData().get(TradingDataAttribute.PRICE)), true, true);

                        } else {
                            series.add(new DataSeriesItem(event.getTime(), event.getData().get(TradingDataAttribute.PRICE)));

                        }
                        tickerChart.setImmediate(true);
                    } finally {
                        getSession().unlock();
                    }
                }

            }



        }
        //if the game has stopped
        else if(arg == EventManager.RealTimePlayerStates.GAME_OVER){
            //TODO - how to handle this?
        }

        //TODO - handle all cases




    }
}
