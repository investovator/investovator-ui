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


package org.investovator.ui.dataplayback.user.dashboard.dailysummary;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.controller.dataplaybackengine.DataPlaybackGameFacade;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserAlreadyJoinedException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.beans.PortfolioBean;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.user.dashboard.realtime.RealTimeMainView;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryMultiPlayerMainView extends RealTimeMainView{
    //decides the number of points shown in the OHLC chart
    private static int OHLC_CHART_LENGTH = 10;

    private String userName;

    private DailySummaryDataPLayer player;

//    @Override
//    public Chart buildMainChart() {
//        Chart chart = new Chart();
////        chart.setHeight("350px");
////        chart.setWidth("90%");
//        chart.setHeight(70,Unit.MM);
//
//
//        Tooltip tooltip = new Tooltip();
//        tooltip.setShared(true);
//        tooltip.setUseHTML(true);
//        tooltip.setHeaderFormat("{point.key}");
//        tooltip.setPointFormat("");
//        tooltip.setFooterFormat("{series.name}: 	{point.y} EUR");
//
//        Configuration configuration = new Configuration();
//        configuration.setTooltip(tooltip);
//        configuration.getChart().setType(ChartType.LINE);
//
//        PlotOptionsLine plotOptions = new PlotOptionsLine();
//        plotOptions.setDataLabels(new Labels(true));
//        plotOptions.setEnableMouseTracking(false);
//        configuration.setPlotOptions(plotOptions);
//
//        configuration.getxAxis().setType(AxisType.DATETIME);
//        configuration.getxAxis().setDateTimeLabelFormats(
//                new DateTimeLabelFormats("%e. %b", "%b"));
//
//        if (DataPlaybackEngineStates.playingSymbols != null) {
//            for (String stock : DataPlaybackEngineStates.playingSymbols) {
//                DataSeries ls = new DataSeries();
//                ls.setName(stock);
//                configuration.addSeries(ls);
//                System.out.println(stock);
//
//            }
//        }
//        chart.drawChart(configuration);
//
//        //disable trademark
//        chart.getConfiguration().disableCredits();
//        chart.getConfiguration().getTitle().setText("Stock Prices");
//
//        return chart;
//    }

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
        DateTimeLabelFormats dateTimeLabelFormat=new DateTimeLabelFormats();
        dateTimeLabelFormat.setWeek("%e. %b");
        dateTimeLabelFormat.setYear("%Y");
        configuration.getxAxis().setDateTimeLabelFormats(dateTimeLabelFormat);

        configuration.getyAxis().setTitle("Price");

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                DataSeries ls = new DataSeries();
                ls.setName(stock);

                //add dummy points to fill it up
                for(int counter=1;counter<=OHLC_CHART_LENGTH;counter++){
                    ls.add(new DataSeriesItem
                            (DateUtils.decrementTimeByDays((OHLC_CHART_LENGTH - counter),
                                    DataPlaybackEngineStates.gameStartDate),0));
                }

                configuration.addSeries(ls);

            }
        }

        chart.setImmediate(true);
        chart.drawChart(configuration);
        //disable trademark
        chart.getConfiguration().disableCredits();


        chart.getConfiguration().setTitle("Price");
        return chart;
    }

    @Override
    public void onEnterMainView() {
        try {
            this.userName=Authenticator.getInstance().getCurrentUser();
            this.player= DataPlaybackGameFacade.getInstance().getDataPlayerFacade().getDailySummaryDataPLayer();
            //join the game if the user has not already done so
            if(!this.player.hasUserJoined(this.userName)){
                this.player.joinMultiplayerGame(this,this.userName);
            }
        } catch (UserAlreadyJoinedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (PlayerStateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void updatePieChart(final StockUpdateEvent event)
            throws PlayerStateException, UserJoinException {

        Portfolio portfolio=this.player.getMyPortfolio(this.userName);

        //since we know that there's only one data series
        final DataSeries dSeries = (DataSeries) stockPieChart.getConfiguration().getSeries().get(0);



        //if this is an update for a stock that the user has already bought
        if(portfolio.getShares().containsKey(event.getStockId())){
            float price =event.getData().get(DataPlaybackEngineStates.gameConfig.getAttributeToMatch());
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


}
