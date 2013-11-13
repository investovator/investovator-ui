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


package org.investovator.ui.dataplayback.user.dashboard.realtime;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.controller.dataplaybackengine.DataPlaybackGameFacade;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.PlaybackEvent;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.events.PlaybackFinishedEvent;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserAlreadyJoinedException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.dataplaybackengine.player.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.beans.PortfolioBean;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.dataplayback.BasicMainView;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class RealTimeMainView extends BasicMainView implements PlaybackEventListener{

    //decides the number of points shown in the ticker chart
    private static int TICKER_CHART_LENGTH = 10;

    private String userName;

    private RealTimeDataPlayer player;



    @Override
    public Chart buildMainChart() {
        Chart chart = new Chart();
        chart.setHeight(70,Unit.MM);
//        chart.setWidth("250px");
//        chart.setSizeFull();

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        tooltip.setHeaderFormat("{point.key}");
        tooltip.setPointFormat("");
        tooltip.setFooterFormat("{series.name}: 	{point.y} EUR");

        Configuration configuration = new Configuration();
        configuration.setTooltip(tooltip);
        configuration.getChart().setType(ChartType.LINE);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        plotOptions.setEnableMouseTracking(false);
        //performance related
        plotOptions.setShadow(false);

        configuration.setPlotOptions(plotOptions);

        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%e. %b", "%b"));

        configuration.getyAxis().setTitle("Price");

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                DataSeries ls = new DataSeries();
                ls.setName(stock);

                //add dummy points to fill it up
                for(int counter=1;counter<=TICKER_CHART_LENGTH;counter++){
                    ls.add(new DataSeriesItem
                            (DateUtils.decrementTimeBySeconds((TICKER_CHART_LENGTH-counter),
                                    DataPlaybackEngineStates.gameStartDate),0));
                }

                configuration.addSeries(ls);

            }
        }

        chart.setImmediate(true);
        chart.drawChart(configuration);
        //disable trademark
        chart.getConfiguration().disableCredits();


        chart.getConfiguration().setTitle("Real-time Stock Prices");
        return chart;
    }

    public void updatePortfolioTable(final String stockID){
        final BeanContainer<String,PortfolioBean> beans = (BeanContainer<String,PortfolioBean>)
                portfolioTable.getContainerDataSource();

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                //if the stock is already bought
                if(beans.containsId(stockID)){
                    beans.removeItem(stockID);
                }
                try {
                    double price = player.getMyPortfolio(userName).getShares().get(stockID).get(Terms.PRICE);
                    int quantity =player.getMyPortfolio(userName).getShares().get(stockID).get(Terms.QNTY).intValue();
                    beans.addBean(new PortfolioBean(stockID,price, quantity));
                } catch (UserJoinException e) {
                    Notification.show("First joint the game", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

    }

    @Override
    public HorizontalLayout getBuySellForumButtons(final ComboBox stocksList,
                                                   final TextField quantity,final NativeSelect orderSide) {
        HorizontalLayout buttonsBar=new HorizontalLayout();
        final Button buySellButton=new Button("Buy");
        buySellButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

//                Notification.show(stocksList.getValue().toString() + "--" + orderSide.getValue().toString() + "--" + quantity.getValue().toString());
//                System.out.println();

//                if (DataPlaybackEngineStates.currentGameMode== PlayerTypes.DAILY_SUMMARY_PLAYER){
                try {
                    Boolean status= player.executeOrder(stocksList.getValue().toString(),
                            Integer.parseInt(quantity.getValue().toString()), ((OrderType) orderSide.getValue()),
                            userName);
                    //if the transaction was a success
                    if(status){
                        updatePortfolioTable(stocksList.getValue().toString());
                    }
                    else{

                        Notification.show(status.toString());
                    }
                } catch (InvalidOrderException e) {
                    Notification.show(e.getMessage());
                } catch (UserJoinException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
//                }

//                if (DataPlaybackEngineStates.currentGameMode==PlayerTypes.REAL_TIME_DATA_PLAYER){
//                    try {
//                        Boolean status=realTimePlayer.executeOrder(stocksList.getValue().toString(),
//                                Integer.parseInt(quantity.getValue().toString()), ((OrderType) orderSide.getValue()));
//                        Notification.show(status.toString());
//
//                    } catch (InvalidOrderException e) {
//                        Notification.show(e.getMessage());
//                    } catch (UserJoinException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//                }

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


        buttonsBar.addComponent(buySellButton);

        return buttonsBar;
    }

    @Override
    public void onEnterMainView() {
        try {
            this.userName=Authenticator.getInstance().getCurrentUser();
            this.player= DataPlaybackGameFacade.getInstance().getDataPlayerFacade().getRealTimeDataPlayer();
            //join the game if the user has not already done so
            if(!this.player.hasUserJoined(this.userName)){
                this.player.joinGame(this,this.userName);
            }

//            System.out.println("ui join -->"+this.toString());
//            DataPlayerFacade.getInstance().getRealTimeDataPlayer().setObserver(this);
        } catch (UserAlreadyJoinedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (PlayerStateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UserJoinException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private void updateTickerChart(final StockUpdateEvent event)  {

        //iterate every series in the chart at the moment
        for (Series series : mainChart.getConfiguration().getSeries()) {
            final DataSeries dSeries = (DataSeries) series;
            //if this series matches the stock events stock
            if (dSeries.getName().equalsIgnoreCase(event.getStockId())) {

//                if (mainChart.isConnectorEnabled()) {
//                    getSession().lock();
//                    try {
//                        if (dSeries.getData().size() > TICKER_CHART_LENGTH) {
//
//                            dSeries.add(new DataSeriesItem(event.getTime(),
//                                    event.getData().get(TradingDataAttribute.PRICE)), true, true);
//
//                        } else {
//                            dSeries.add(new DataSeriesItem(event.getTime(),
//                                    event.getData().get(TradingDataAttribute.PRICE)));
//
//                        }
//
//                    } finally {
//                        getSession().unlock();
//                    }
//                }

                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        if (dSeries.getData().size() > TICKER_CHART_LENGTH) {

                            dSeries.add(new DataSeriesItem(event.getTime(),
                                    event.getData().get(TradingDataAttribute.PRICE)), true, true);

                        } else {
                            dSeries.add(new DataSeriesItem(event.getTime(),
                                    event.getData().get(TradingDataAttribute.PRICE)));

                        }

                    }
                });


            }

        }

    }

    private void updateStockPriceTable(final StockUpdateEvent event){

        final BeanContainer<String,StockNamePriceBean> beans = (BeanContainer<String,StockNamePriceBean>)
                stockPriceTable.getContainerDataSource();


//        if (stockPriceTable.isConnectorEnabled()) {
//            getSession().lock();
//            try {
//                beans.removeItem(event.getStockId());
//                beans.addBean(new StockNamePriceBean(event.getStockId(),
//                        event.getData().get(TradingDataAttribute.PRICE)));
//            } finally {
//                getSession().unlock();
//            }
//        }

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                beans.removeItem(event.getStockId());
                beans.addBean(new StockNamePriceBean(event.getStockId(),
                        event.getData().get(TradingDataAttribute.PRICE)));
            }
        });

        //update the pie-chart
        try {
            updatePieChart(event,beans);
        } catch (PlayerStateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UserJoinException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void updatePieChart(final StockUpdateEvent event, BeanContainer<String,StockNamePriceBean> beans)
            throws PlayerStateException, UserJoinException {

        Portfolio portfolio=this.player.getMyPortfolio(this.userName);

        //since we know that there's only one data series
        final DataSeries dSeries = (DataSeries) stockPieChart.getConfiguration().getSeries().get(0);



                //if this is an update for a stock that the user has already bought
                if(portfolio.getShares().containsKey(event.getStockId())){
                    float price =event.getData().get(TradingDataAttribute.PRICE);
                    double quantity= portfolio.getShares().get(event.getStockId()).get(Terms.QNTY);

                    //update the chart
                    DataSeriesItem item=dSeries.get(event.getStockId());
                    item.setY(price*quantity);
                    dSeries.update(item);

                }


//
//        if (stockPieChart.isConnectorEnabled()) {
//            getSession().lock();
//            try {
//                dSeries.update(dSeries.get(event.getStockId()));
//                stockPieChart.setImmediate(true);
//                stockPieChart.drawChart();
//
//            } finally {
//                getSession().unlock();
//            }
//        }

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                dSeries.update(dSeries.get(event.getStockId()));
                stockPieChart.setImmediate(true);
                stockPieChart.drawChart();
                //UI.getCurrent().push();
                //System.out.println("pushed");
                getUI().push();
            }
        });
    }


    @Override
    public void eventOccurred(PlaybackEvent arg) {
//        System.out.println("Event");
        //if this is a stock price update
        if (arg instanceof StockUpdateEvent) {
            final StockUpdateEvent event = (StockUpdateEvent) arg;

            //update the ticker chart
            updateTickerChart(event);

            //update quantity chart
            updateQuantityChart(event);

            //update the table
            updateStockPriceTable(event);
        }
        //if the game has stopped
        else if (arg instanceof PlaybackFinishedEvent) {
            //TODO - how to handle this?
        }
    }


    private void updateQuantityChart(final StockUpdateEvent event)  {

        //iterate every series in the chart at the moment
        for (Series series : quantityChart.getConfiguration().getSeries()) {
            final DataSeries dSeries = (DataSeries) series;
            //if this series matches the stock events stock
            if (dSeries.getName().equalsIgnoreCase(event.getStockId())) {

//                if (mainChart.isConnectorEnabled()) {
//                    getSession().lock();
//                    try {
//                        if (dSeries.getData().size() > TICKER_CHART_LENGTH) {
//
//                            dSeries.add(new DataSeriesItem(event.getTime(),
//                                    event.getData().get(TradingDataAttribute.SHARES)/100), true, true);
//
//                        } else {
//                            dSeries.add(new DataSeriesItem(event.getTime(),
//                                    event.getData().get(TradingDataAttribute.SHARES)/100));
//
//                        }
//
//                    } finally {
//                        getSession().unlock();
//                    }
//                }

                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {

                            if (dSeries.getData().size() > TICKER_CHART_LENGTH) {

                                dSeries.add(new DataSeriesItem(event.getTime(),
                                        event.getData().get(TradingDataAttribute.SHARES)/100), true, true);

                            } else {
                                dSeries.add(new DataSeriesItem(event.getTime(),
                                        event.getData().get(TradingDataAttribute.SHARES)/100));

                            }
                    }
                });


            }

        }

    }


    public Chart buildQuantityChart(){
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight(43,Unit.MM);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Quantity");

        XAxis x = new XAxis();
        x.setType(AxisType.DATETIME);
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
//        y.setMinRange(500);
        y.setTitle("Quantity");
        conf.addyAxis(y);


        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.y+' sold'");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0);
        plot.setBorderWidth(0);
        //set the widht of the columns
        plot.setPointWidth(15);

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                DataSeries ls = new DataSeries();
                ls.setName(stock);
                ls.setPlotOptions(plot);

                //add dummy points to fill it up
                for(int counter=1;counter<=TICKER_CHART_LENGTH;counter++){
                    ls.add(new DataSeriesItem
                            (DateUtils.decrementTimeBySeconds((TICKER_CHART_LENGTH-counter),
                                    DataPlaybackEngineStates.gameStartDate),0));
                }

                conf.addSeries(ls);


            }
        }

        conf.disableCredits();
        chart.drawChart(conf);
        chart.setImmediate(true);
        return chart;
    }
}