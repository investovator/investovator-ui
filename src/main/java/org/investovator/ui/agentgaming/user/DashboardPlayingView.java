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

package org.investovator.ui.agentgaming.user;

import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.agentsimulation.api.JASAFacade;
import org.investovator.agentsimulation.api.MarketFacade;
import org.investovator.ui.agentgaming.*;
import org.investovator.ui.agentgaming.beans.StockItemBean;
import org.investovator.ui.agentgaming.events.AgentUIUpdater;
import org.investovator.ui.agentgaming.events.StockChangedEvent;
import org.investovator.ui.agentgaming.events.WatchList;
import org.investovator.ui.agentgaming.user.components.MultiStockChart;
import org.investovator.ui.agentgaming.user.components.PortfolioSummary;
import org.investovator.ui.agentgaming.user.components.QuoteUI;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.Collection;
import java.util.Date;

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
    VerticalLayout content;
    Table watchListTable;
    MultiStockChart currentPriceChart;
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

        //Subscribe to listeners
        AgentUIUpdater.getInstance().addListener(portfolioSummary);

    }


    private void createUI(){

        //Setup Layout
        content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        content.setSpacing(true);

        HorizontalLayout row1 = new HorizontalLayout();
        HorizontalLayout row2 = new HorizontalLayout();

        row1.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        row2.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        row1.setWidth("100%");
        row2.setWidth("100%");


        row1.setHeight("60%");
        row1.setHeight("35%");



        content.addComponent(row1);
        content.addComponent(row2);

        content.setExpandRatio(row1, 55);
        content.setExpandRatio(row2, 45);

        //Portfolio Summary
        portfolioSummary = new PortfolioSummary();

        //QuoteUI
        quoteUI = new QuoteUI(companyData);

        watchListTable = getTable();
        currentPriceChart = new MultiStockChart();

        //Adding to main layout
        row1.addComponent(watchListTable);
        row1.addComponent(currentPriceChart);
        row2.addComponent(quoteUI);
        row2.addComponent(portfolioSummary);
        row2.setComponentAlignment(portfolioSummary,Alignment.TOP_CENTER);

        watchListTable.addStyleName("center-caption");
        quoteUI.addStyleName("center-caption");
        currentPriceChart.addStyleName("center-caption");

        content.setSizeFull();


        this.setSizeFull();

        this.setContent(content);

    }

    protected Table getTable() {

        BeanContainer<String, StockItemBean> watchList = new BeanContainer<String, StockItemBean>(StockItemBean.class);
        watchList.setBeanIdProperty("stockID");

        Table table = new Table("Watch List", watchList);

        //table.setSizeFull();
        table.setWidth("90%");
        table.setHeight("300px");
        table.setSelectable(true);
        table.setImmediate(true);

        table.setVisibleColumns(new String[]{"stockID","bestBid","bestAsk","marketPrice"});

        return table;
    }


    final ListSeries series = new ListSeries(0);

    @Override
    public void onStockChange(final StockItemBean stockChanged) {
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


        if (currentPriceChart.isConnectorEnabled()) {
            currentPriceChart.insertDataPoint(stockChanged.getStockID(),stockChanged.getTimeStamp(), stockChanged.getMarketPrice());

            try {
                Collection<String> availableStocks = new UserDataImpl().getWatchList(Session.getCurrentGameInstance(), Authenticator.getInstance().getCurrentUser());

                for(String stock: availableStocks){
                    if(!stock.equals(changedStockID)){
                        Date tmp = ReportHelper.getInstance().getDate((int)JASAFacade.getMarketFacade().getSimulationTime(stock));
                        currentPriceChart.insertDataPoint(stock,tmp,ReportHelper.getInstance().getCurrentPrice(stock));
                    }
                }

            } catch (DataAccessException e) {
                e.printStackTrace();
            }

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
            availableStocks = new UserDataImpl().getWatchList(Session.getCurrentGameInstance(),Authenticator.getInstance().getCurrentUser());
            for(String stock : availableStocks){
                simulationFacade.addListener(stock,watchList);
                simulationFacade.addListener(stock, AgentUIUpdater.getInstance());
                currentPriceChart.addStock(stock, reportHelper.getTimeSeriesReport(stock,"market price",50));

            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }
}



