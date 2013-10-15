/*
 * investovator, Stock Market Gaming framework
 * Copyright (C) 2013  investovator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.agentgaming;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.investovator.jasa.api.JASAFacade;
import org.investovator.jasa.api.MarketFacade;


import java.util.*;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DashboardPlayingView extends Panel implements StockChangedEvent {

    GridLayout content;
    ReportHelper reportHelper;
    MarketFacade simulationFacade = JASAFacade.getMarketFacade();

    Table watchListTable;
    WatchList watchList;


    boolean simulationRunning = false;
    boolean reportsReady=false;


    public DashboardPlayingView() {

        //Setup Layout
        content = new GridLayout();
        content.setRows(2);
        content.setColumns(2);


        Button test = new Button("Start");
        Button stop = new Button("Stop");

        test.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //testing
                simulationFacade = JASAFacade.getMarketFacade();
                simulationFacade.startSimulation();
                simulationRunning = true;

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                reportHelper.viewReport();

                reportsReady = true;

            }
        });

        stop.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                simulationFacade.terminateSimulation();
            }
        });


        watchListTable = getTable();


        content.addComponent(watchListTable);
        content.addComponent(test);
        content.addComponent(stop);
        content.setComponentAlignment(test, Alignment.MIDDLE_CENTER);
        content.addComponent(getChart());

        content.setSizeFull();

        this.setSizeFull();

        this.setContent(content);

        //Reports Config
        reportHelper = new ReportHelper();
        watchList = new WatchList(reportHelper);
        watchList.addStockChangeListener(this);

        new Thread(watchList).start();

    }


    protected Table getTable() {

        BeanContainer<String, StockItemBean> watchList = new BeanContainer<String, StockItemBean>(StockItemBean.class);
        watchList.setBeanIdProperty("stockID");

        StockItemBean stockItemBean = new StockItemBean();
        stockItemBean.setStockID("GOOG");
        stockItemBean.setLastAsk(125.4f);
        stockItemBean.setLastBid(100);
        stockItemBean.setMarketPrice(102.5f);

        watchList.addBean(stockItemBean);

        Table table = new Table("Watch List", watchList);

        table.setSizeFull();
        table.setSelectable(true);
        table.setImmediate(true);

        return table;
    }


    final ListSeries series = new ListSeries(0);





    protected Component getChart() {

        final Chart chart = new Chart();
        chart.setHeight("450px");
        chart.setWidth("75%");

        final Configuration configuration = new Configuration();

        configuration.getChart().setType(ChartType.SPLINE);
        configuration.disableCredits();


        configuration.setSeries(series);

        chart.drawChart(configuration);


        Thread randomDataGenerator = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);

                        if(!reportsReady) continue;

                        if (chart.isConnectorEnabled()) {
                            getSession().lock();
                            try {
                                series.addData(reportHelper.getValue());
                            } finally {
                                getSession().unlock();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        randomDataGenerator.start();


        return chart;
    }


    @Override
    public void onStockChange(StockItemBean stockChanged) {
        //To change body of implemented methods use File | Settings | File Templates.

        String changedStockID = stockChanged.getStockID();

        BeanContainer<String, StockItemBean> shownStocks = (BeanContainer<String, StockItemBean>) watchListTable.getContainerDataSource();


        if (watchListTable.isConnectorEnabled()) {
            getSession().lock();
            try {
                shownStocks.removeItem(changedStockID);
                shownStocks.addBean(stockChanged);
            } finally {
                getSession().unlock();
            }
        }


    }
}



