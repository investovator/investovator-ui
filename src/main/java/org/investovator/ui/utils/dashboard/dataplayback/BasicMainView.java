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

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.exceptions.GameFinishedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.ui.dataplayback.beans.PortfolioBean;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
* @author: ishan
* @version: ${Revision}
*/
public abstract class BasicMainView extends DashboardPanel {

    //charts to be shown
    protected Chart mainChart;
    protected Chart stockPieChart;
    protected Chart quantityChart;

    protected Table stockPriceTable;
    protected Table portfolioTable;

    //to store every component
    VerticalLayout content;

    protected BasicMainView() {
        //set a link to this class
//        mySelf = this;

        content = new VerticalLayout();
        //to show the scroll bar
        this.setHeight("100%");
        content.setSizeFull();
    }

    @Override
    public void onEnter() {
        setupPanel();
        onEnterMainView();
    }

    public void setupPanel(){
        //clear everything
//        content.removeAllComponents();

        //add components only if components have not already been added
        if(content.getComponentCount()==0){

            //Main chart
            HorizontalLayout chartContainer = new HorizontalLayout();
            chartContainer.setWidth(95, Unit.PERCENTAGE);
            chartContainer.setMargin(true);
//            chartContainer.setHeight(30,Unit.PERCENTAGE);
            mainChart = buildMainChart();
            chartContainer.addComponent(mainChart);
            chartContainer.setComponentAlignment(mainChart, Alignment.MIDDLE_CENTER);
            chartContainer.setCaption(mainChart.getCaption());
//            chartContainer.setCaption("Price");
//            chartContainer.addStyleName("center-caption");


            content.addComponent(chartContainer);
            content.setExpandRatio(chartContainer,1.3f);
            content.setComponentAlignment(chartContainer, Alignment.MIDDLE_CENTER);

            //Quantity chart
            HorizontalLayout quantityChartContainer = new HorizontalLayout();
            quantityChartContainer.setWidth(95, Unit.PERCENTAGE);
//            quantityChartContainer.setMargin(true);
            quantityChartContainer.setMargin(new MarginInfo(true,true,false,true));
//            quantityChartContainer.setHeight(30,Unit.PERCENTAGE);
            quantityChart = buildQuantityChart();
            quantityChartContainer.addComponent(quantityChart);
            quantityChartContainer.setComponentAlignment(quantityChart, Alignment.MIDDLE_CENTER);
//            quantityChartContainer.setCaption("Quantity");
//            quantityChartContainer.addStyleName("center-caption");

            content.addComponent(quantityChartContainer);
            content.setExpandRatio(quantityChartContainer,1.0f);

            content.setComponentAlignment(quantityChartContainer, Alignment.MIDDLE_CENTER);

            //bottom row conatainer
            HorizontalLayout bottowRow=new HorizontalLayout();
            content.addComponent(bottowRow);
            content.setExpandRatio(bottowRow,1.0f);


            //Stock price table
            GridLayout stockPriceTableContainer = new GridLayout(1,2);
            //add a caption to the table
//            Label tableCaption=new Label("Stock Price Table");
//            stockPriceTableContainer.addComponent(tableCaption, 0, 0);
//            stockPriceTableContainer.setComponentAlignment(tableCaption,Alignment.MIDDLE_RIGHT);
            stockPriceTable=setupStockPriceTable();
            stockPriceTableContainer.addComponent(stockPriceTable, 0, 1);
            stockPriceTableContainer.setMargin(new MarginInfo(false, true, true, true));
            stockPriceTableContainer.setCaption("Stock Price Table");
            stockPriceTableContainer.addStyleName("center-caption");

            stockPriceTableContainer.setComponentAlignment(stockPriceTable,Alignment.MIDDLE_CENTER);
            bottowRow.addComponent(stockPriceTableContainer);

            //buy-sell window
            GridLayout buySellWindowContainer = new GridLayout(1,2);
//            //add a caption to the table
//            Label buySellWindowCaption=new Label("Buy/Sell Stocks");
//            buySellWindowContainer.addComponent(buySellWindowCaption,0,0);
//            buySellWindowContainer.setComponentAlignment(buySellWindowCaption,Alignment.MIDDLE_CENTER);
            Component buySellWindow=setupBuySellForm();
            buySellWindowContainer.addComponent(buySellWindow,0,1);
            buySellWindowContainer.setMargin(new MarginInfo(false,false,true,false));
            buySellWindowContainer.setCaption("Buy/Sell Stocks");
            buySellWindowContainer.addStyleName("center-caption");

            buySellWindowContainer.setComponentAlignment(buySellWindow,Alignment.MIDDLE_CENTER);
            bottowRow.addComponent(buySellWindowContainer);

            //portfolio data
//            VerticalLayout myPortfolioLayout=new VerticalLayout();
//            myPortfolioLayout.setMargin(new MarginInfo(false,true,true,true));
//            bottowRow.addComponent(myPortfolioLayout);
            //add a caption to the table
//            Label portfolioCaption=new Label("My Portfolio");
//            myPortfolioLayout.addComponent(portfolioCaption);
//            myPortfolioLayout.setComponentAlignment(portfolioCaption,Alignment.MIDDLE_CENTER);

            HorizontalLayout portfolioContainer = new HorizontalLayout();
            portfolioContainer.setMargin(new MarginInfo(false,true,true,true));
            portfolioContainer.setCaption("My Portfolio");
            portfolioContainer.addStyleName("center-caption");
            bottowRow.addComponent(portfolioContainer);

            //portfolio table
            portfolioTable=setupPortfolioTable();
            portfolioContainer.addComponent(portfolioTable);
            //pie-chart
            stockPieChart =setupPieChart();
            portfolioContainer.addComponent(stockPieChart);
//            portfolioContainer.setComponentAlignment(stockPieChart,Alignment.TOP_RIGHT);


            this.setContent(content);
        }





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
        table.setCaption(null);
        //restrict the maximum number of viewable entries to 5
        table.setPageLength(5);

        //set the column order
        table.setVisibleColumns(new Object[]{"stockID", "price"});
        table.setColumnHeader("stockID","Symbols");
        table.setColumnHeader("price","Price");


        return table;
    }

    private Table setupPortfolioTable(){
        BeanContainer<String,PortfolioBean> beans =
                new BeanContainer<String,PortfolioBean>(PortfolioBean.class);
        beans.setBeanIdProperty("stockID");

        Table table=new Table("Stock Prices",beans);
        table.setCaption(null);
        //restrict the maximum number of viewable entries to 5
        table.setPageLength(5);

        //set the column order
        table.setVisibleColumns(new Object[]{"stockID", "numOfStocks","averageCost","totCost"});
        table.setColumnHeader("stockID","Symbols");
        table.setColumnHeader("numOfStocks","Quantity");
        table.setColumnHeader("averageCost","Avg. Cost");
        table.setColumnHeader("totCost","Tot. Cost");

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
        //stocksList.setWidth("75%");

        //side
        final NativeSelect orderSide=new NativeSelect();
        orderSide.setCaption("Side");
        orderSide.addItem(OrderType.BUY);
        orderSide.addItem(OrderType.SELL);
        //orderSide.setWidth("90%");
        orderSide.setSizeFull();
        orderSide.select(OrderType.BUY);
        orderSide.setNullSelectionAllowed(false);
        orderSide.setImmediate(true);

        //Quantity
        final TextField quantity=new TextField("Amount");
        //quantity.setWidth("75%");


        form.addComponent(stocksList);
        form.addComponent(orderSide);
        form.addComponent(quantity);

        formContent.addComponent(form);

        HorizontalLayout bBar=getBuySellForumButtons(stocksList,quantity,orderSide);
        formContent.addComponent(bBar);
        formContent.setComponentAlignment(bBar,Alignment.BOTTOM_RIGHT);
        //content.setComponentAlignment(nextDayB, Alignment.MIDDLE_CENTER);

        return formContent;
    }

    private Chart setupPieChart(){

        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.getTitle().setText(null);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
//        plotOptions.setSize("120%");

        Labels dataLabels = new Labels();
        dataLabels.setEnabled(false);
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
                series.add(new DataSeriesItem(stock, 50));
            }
        }
        conf.setSeries(series);

        conf.disableCredits();


        chart.drawChart(conf);
        //turn off animation
        conf.getChart().setAnimation(false);
        chart.setWidth(115,Unit.MM);
        chart.setHeight(55,Unit.MM);

//        chart.setWidth(75,Unit.PERCENTAGE);
//        chart.setHeight(55,Unit.MM);


        return chart;
    }

    abstract public Chart buildMainChart();

    abstract public HorizontalLayout getBuySellForumButtons(ComboBox stocksList,
                                                            TextField quantity,NativeSelect orderSide);

    /**
     * override this to call the onEnter method of the DashboardPanel
     */
    abstract public void onEnterMainView();

    abstract public Chart buildQuantityChart();
//    {
//        Chart chart = new Chart(ChartType.COLUMN);
//        chart.setHeight(40,Unit.MM);
//
//        Configuration conf = chart.getConfiguration();
//
//        //conf.setTitle("Total fruit consumtion, grouped by gender");
//        //conf.setSubTitle("Source: WorldClimate.com");
//
//        XAxis x = new XAxis();
//        x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
//                "Sep", "Oct", "Nov", "Dec");
//        conf.addxAxis(x);
//
//        YAxis y = new YAxis();
//        y.setMin(0);
//        y.setTitle("Quantity");
//        conf.addyAxis(y);
//
//        Legend legend = new Legend();
//        legend.setLayout(LayoutDirection.VERTICAL);
//        legend.setBackgroundColor("#FFFFFF");
//        legend.setHorizontalAlign(HorizontalAlign.LEFT);
//        legend.setVerticalAlign(VerticalAlign.TOP);
//        legend.setX(100);
//        legend.setY(70);
//        legend.setFloating(true);
//        legend.setShadow(true);
//        conf.setLegend(legend);
//
//        Tooltip tooltip = new Tooltip();
//        tooltip.setFormatter("this.x +': '+ this.y +' mm'");
//        conf.setTooltip(tooltip);
//
//        PlotOptionsColumn plot = new PlotOptionsColumn();
//        plot.setPointPadding(0.2);
//        plot.setBorderWidth(0);
//
//        conf.addSeries(new ListSeries("Tokyo", 49.9, 71.5, 106.4, 129.2, 144.0,
//                176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4));
////        conf.addSeries(new ListSeries("New York", 83.6, 78.8, 98.5, 93.4,
////                106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3));
////        conf.addSeries(new ListSeries("London", 48.9, 38.8, 39.3, 41.4, 47.0,
////                48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2));
////        conf.addSeries(new ListSeries("Berlin", 42.4, 33.2, 34.5, 39.7, 52.6,
////                75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1));
//
//        chart.drawChart(conf);
//        return chart;
//    }

}
