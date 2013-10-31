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
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.agentsimulation.api.JASAFacade;
import org.investovator.agentsimulation.api.MarketFacade;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.Collection;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DashboardPlayingView extends DashboardPanel implements StockChangedEvent {


    //External Data
    ReportHelper reportHelper;
    MarketFacade simulationFacade = JASAFacade.getMarketFacade();
    CompanyData companyData = null;
    UserData userData;

    //Layout Components
    GridLayout content;
    Table watchListTable;
    Chart currentPriceChart;
    WatchList watchList;
    QuoteUI quoteUI;
    PortfolioSummary portfolioSummary;

    boolean simulationRunning = false;

    public DashboardPlayingView() {

        createUI();

        //Reports Config
        reportHelper = ReportHelper.getInstance();
        watchList = new WatchList(reportHelper);
        watchList.addStockChangeListener(this);

        //new Thread(watchList).start();


    }


    private void createUI(){

        //Setup Layout
        content = new GridLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.setRows(2);
        content.setColumns(2);


        //Portfolio Summary
        portfolioSummary = new PortfolioSummary();


        //QuoteUI
        quoteUI = new QuoteUI(companyData);



        watchListTable = getTable();
        currentPriceChart = getChart();



        //Adding to main layout
        content.addComponent(watchListTable);
        content.addComponent(currentPriceChart);
        content.addComponent(quoteUI);
        content.addComponent(portfolioSummary);
        content.setComponentAlignment(watchListTable,Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(currentPriceChart,Alignment.MIDDLE_CENTER);
        //content.addComponent(buttons);

        content.setSizeFull();

        this.setSizeFull();

        this.setContent(content);

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
        table.setWidth("90%");
        table.setSelectable(true);
        table.setImmediate(true);

        table.setVisibleColumns(new String[]{"stockID","bestBid","bestAsk","marketPrice"});

        return table;
    }


    final ListSeries series = new ListSeries(0);





    protected Chart getChart() {

        final Chart chart = new Chart();
        chart.setHeight("350px");
        chart.setWidth("90%");
        chart.setCaption("Watchlist Summary");

        final Configuration configuration = new Configuration();
        configuration.setTitle("Last Traded Price");

        configuration.getChart().setType(ChartType.SPLINE);
        configuration.disableCredits();

        configuration.setSeries(series);

        chart.drawChart(configuration);


        return chart;
    }




    @Override
    public void onStockChange(StockItemBean stockChanged) {
        //To change body of implemented methods use File | Settings | File Templates.

        if(!simulationRunning) return;

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


        if (currentPriceChart.isConnectorEnabled()) synchronized (UI.getCurrent()){
             //if( series.getData().length > 20)  series.addData(stockChanged.getMarketPrice(),true,true);
             series.addData(stockChanged.getMarketPrice());
        }


    }

    @Override
    public void onEnter() {


        try {
            companyData =  new CompanyDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        quoteUI.update();
        portfolioSummary.update();

        reportHelper.initReports();
        simulationRunning = true;

        Collection<String> availableStocks = null;
        try {
            availableStocks = new UserDataImpl().getWatchList(Authenticator.getInstance().getCurrentUser());
            for(String stock : availableStocks){
                simulationFacade.addListener(stock,watchList);
                simulationFacade.addListener(stock, AgentUIUpdater.getInstance());

            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        //Subscribe to listeners
        AgentUIUpdater.getInstance().addListener(portfolioSummary);


    }
}



