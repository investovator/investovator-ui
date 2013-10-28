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
import com.vaadin.ui.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.exceptions.GameFinishedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
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

    protected Table stockPriceTable;

    //to store every component
    GridLayout content;

    protected BasicMainView() {
        //set a link to this class
//        mySelf = this;

        content = new GridLayout(3, 3);
        content.setSizeFull();
    }

    @Override
    public void onEnter() {
        setupPanel();
        onEnterMainView();
    }

    public void setupPanel(){
        //clear everything
        content.removeAllComponents();

        //Main chart
        HorizontalLayout chartContainer = new HorizontalLayout();
        chartContainer.setWidth(95, Unit.PERCENTAGE);
        mainChart = buildMainChart();
        chartContainer.addComponent(mainChart);
        chartContainer.setComponentAlignment(mainChart, Alignment.MIDDLE_CENTER);

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
        table.setColumnHeader("stockID","Symbols");
        table.setColumnHeader("price","Price");


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











        HorizontalLayout bBar=getBuySellForumButtons(stocksList,quantity,orderSide);
        formContent.addComponent(bBar);
        formContent.setComponentAlignment(bBar,Alignment.BOTTOM_RIGHT);
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

    abstract public Chart buildMainChart();

    abstract public HorizontalLayout getBuySellForumButtons(ComboBox stocksList,
                                                            TextField quantity,NativeSelect orderSide);

    /**
     * override this to call the onEnter method of the DashboardPanel
     */
    abstract public void onEnterMainView();

}
