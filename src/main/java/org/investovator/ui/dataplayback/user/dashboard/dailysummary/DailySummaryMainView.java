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
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.events.StockEvent;
import org.investovator.dataplaybackengine.exceptions.GameFinishedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.player.OHLCDataPLayer;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryMainView extends DashboardPanel {

    //decides the number of points shown in the OHLC chart
    private static int OHLC_CHART_LENGTH = 10;

    private OHLCDataPLayer ohlcPLayer;
    private DataPlayerFacade playerFacade;

    //used in ticker data observing
    DailySummaryMainView mySelf;

    //charts to be shown
    Chart ohlcChart;
    Chart stockPieChart;

    Table stockPriceTable;

    //to store every component
    GridLayout content;

    public DailySummaryMainView() {
        //set a link to this class
        mySelf = this;

        content = new GridLayout(3, 3);
        content.setSizeFull();

    }

    @Override
    public void onEnter() {
        Notification.show("DailySummaryMainView");

        //get the player
        try {
            this.ohlcPLayer=DataPlayerFacade.getInstance().getDailySummaryDataPLayer();
            setupPanel();
        } catch (PlayerStateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void setupPanel(){
        //clear everything
        content.removeAllComponents();

        //Main chart
        HorizontalLayout chartContainer = new HorizontalLayout();
        chartContainer.setWidth(95, Unit.PERCENTAGE);
        ohlcChart = buildMainChart();
        chartContainer.addComponent(ohlcChart);
        chartContainer.setComponentAlignment(ohlcChart, Alignment.MIDDLE_CENTER);

        content.addComponent(chartContainer, 0, 1, 2, 1);
        content.setComponentAlignment(chartContainer, Alignment.MIDDLE_CENTER);

        //Stock price table
        stockPriceTable=setupStockPriceTable();
        content.addComponent(stockPriceTable,0,2);
        content.setComponentAlignment(stockPriceTable,Alignment.BOTTOM_LEFT);

        //buy-sell window
        Component buySellWindow=setupBuySellForm();
        content.addComponent(buySellWindow,1,2);

        //pie-chart
        stockPieChart =setupPieChart();
        content.addComponent(stockPieChart,2,2);



        this.setContent(content);





    }

    private Chart buildMainChart(){

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

    private Component setupBuySellForm(){
        VerticalLayout formContent=new VerticalLayout();

        FormLayout form=new FormLayout();

        //stocks list
        final ComboBox stocksList=new ComboBox();
        stocksList.setCaption("Stock");
        stocksList.setNullSelectionAllowed(false);
            for(String stock:DataPlaybackEngineStates.playingSymbols){
                stocksList.addItem(stock);
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

                if (DataPlaybackEngineStates.currentGameMode== PlayerTypes.DAILY_SUMMARY_PLAYER){
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


            buttonsBar.addComponent(nextDayB);

        buttonsBar.addComponent(buySellButton);
        formContent.addComponent(buttonsBar);
        formContent.setComponentAlignment(buttonsBar,Alignment.BOTTOM_RIGHT);
        //content.setComponentAlignment(nextDayB, Alignment.MIDDLE_CENTER);

        return formContent;
    }

    private Chart setupPieChart(){

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
        conf.setSeries(series);

        chart.drawChart(conf);
        chart.setWidth("90%");
        chart.setHeight(70,Unit.MM);


        return chart;
    }
}
