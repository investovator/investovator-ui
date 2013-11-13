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
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.GameFinishedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserAlreadyJoinedException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.dataplayback.BasicMainView;

import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummarySinglePlayerMainView extends BasicMainView {

    //decides the number of points shown in the OHLC chart
    private static int OHLC_CHART_LENGTH = 10;

    private String userName;

    private DailySummaryDataPLayer player;


    //used in ticker data observing
    DailySummarySinglePlayerMainView mySelf;


    //to store every component
    GridLayout content;

    public DailySummarySinglePlayerMainView() {
        //set a link to this class
        mySelf = this;

        content = new GridLayout(3, 3);
        content.setSizeFull();

    }


    public Chart buildMainChart(){

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
//                ls.add(new DataSeriesItem(new Date(),100));
                configuration.addSeries(ls);
            }
        }
        chart.drawChart(configuration);

        //disable trademark
        chart.getConfiguration().disableCredits();
        chart.getConfiguration().getTitle().setText("Stock Closing Prices");

        return chart;

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
                        Boolean status=player.executeOrder(stocksList.getValue().toString(),
                                Integer.parseInt(quantity.getValue().toString()), ((OrderType) orderSide.getValue()),
                                Authenticator.getInstance().getCurrentUser());
                        Notification.show(status.toString());
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

        //TODO-load this only if this is a multiplayer game

        Button nextDayB = new Button("Next day");
        nextDayB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                //get the events
                try {
                    StockUpdateEvent[] events = player.playNextDay();
                    //iterate every event
                    for (StockUpdateEvent event : events) {
                        //iterate every series in the chart at the moment
                        for (Series series : mainChart.getConfiguration().getSeries()) {
                            DataSeries dSeries = (DataSeries) series;
                            //if there's a match
                            if (event.getStockId().equals(dSeries.getName())) {
                                if (mainChart.isConnectorEnabled()) {
                                    getSession().lock();
                                    try {
                                        if (dSeries.getData().size() > OHLC_CHART_LENGTH) {

                                            dSeries.add(new DataSeriesItem(event.getTime(),
                                                    event.getData().get(TradingDataAttribute.PRICE)), true, true);

                                        } else {
                                            dSeries.add(new DataSeriesItem(event.getTime(),
                                                    event.getData().get(TradingDataAttribute.PRICE)));

                                        }
                                        mainChart.setImmediate(true);

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


        buttonsBar.addComponent(nextDayB);

        buttonsBar.addComponent(buySellButton);

        return buttonsBar;
    }

    @Override
    public void onEnterMainView() {
        //join the game
        try {
            this.userName=Authenticator.getInstance().getCurrentUser();
            this.player= DataPlaybackGameFacade.getInstance().getDataPlayerFacade().getDailySummaryDataPLayer();
            //join the game if the user has not already done so
            if(!this.player.hasUserJoined(this.userName)){
                this.player.joinSingleplayerGame(this.userName);
            }
        } catch (UserAlreadyJoinedException e) {
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (PlayerStateException e) {
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Chart buildQuantityChart(){
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight(40,Unit.MM);

        Configuration conf = chart.getConfiguration();

        XAxis x = new XAxis();
        x.setType(AxisType.DATETIME);
//        x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
//                "Sep", "Oct", "Nov", "Dec");
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Quantity");
        conf.addyAxis(y);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setBackgroundColor("#FFFFFF");
        legend.setHorizontalAlign(HorizontalAlign.LEFT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(100);
        legend.setY(70);
        legend.setFloating(true);
        legend.setShadow(true);
        conf.setLegend(legend);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.x +': '+ this.y +''");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                ListSeries ls = new ListSeries();
                ls.setName(stock);
                conf.addSeries(ls);

            }
        }

//        conf.addSeries(new ListSeries("Tokyo", 49.9, 71.5, 106.4, 129.2, 144.0,
//                176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4));
//        conf.addSeries(new ListSeries("New York", 83.6, 78.8, 98.5, 93.4,
//                106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3));
//        conf.addSeries(new ListSeries("London", 48.9, 38.8, 39.3, 41.4, 47.0,
//                48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2));
//        conf.addSeries(new ListSeries("Berlin", 42.4, 33.2, 34.5, 39.7, 52.6,
//                75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1));

        chart.drawChart(conf);
        chart.setImmediate(true);
        return chart;
    }
}