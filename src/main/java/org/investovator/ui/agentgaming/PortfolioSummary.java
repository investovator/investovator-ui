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

import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import net.sourceforge.jabm.event.EventListener;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jasa.event.TransactionExecutedEvent;
import org.investovator.controller.utils.events.GameEvent;
import org.investovator.controller.utils.events.GameEventListener;
import org.investovator.controller.utils.events.PortfolioChangedEvent;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.authentication.Authenticator;

import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class PortfolioSummary extends HorizontalLayout implements GameEventListener{

    //External Data
    UserData userData;

    //Layout Components
    Label accountBalance;
    Label blockedAmount;
    Table stocksSummaryTable;

    public PortfolioSummary() {
        setupUI();
    }

    public void setupUI(){

        accountBalance=new Label();
        accountBalance.setCaption("Cash Balance");

        blockedAmount = new Label();
        blockedAmount.setCaption("Blocked Amount");

        createStocksTable();


        this.setCaption("Portfolio Summary");


        VerticalLayout portSummary = new VerticalLayout();
        portSummary.addComponent(accountBalance);
        portSummary.addComponent(blockedAmount);
        this.addComponent(portSummary);
        this.addComponent(stocksSummaryTable);
    }


    public void update(){

        if(userData == null) try {
            userData = new UserDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        try {
            String currentUser = Authenticator.getInstance().getCurrentUser();
            accountBalance.setValue(Double.toString(userData.getUserPortfolio(currentUser).getCashBalance()));
            blockedAmount.setValue(Double.toString(userData.getUserPortfolio(currentUser).getBlockedCash()));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }


    public void updatePortfolio(Portfolio portfolio){

        if (this.isConnectorEnabled()) {
            getSession().lock();
            try {
                accountBalance.setValue(Double.toString(portfolio.getCashBalance()));
                blockedAmount.setValue(Double.toString(portfolio.getBlockedCash()));
            } finally {
                getSession().unlock();
            }
        }
    }

    @Override
    public void eventOccurred(GameEvent event) {
        if (event instanceof PortfolioChangedEvent){
            updatePortfolio(((PortfolioChangedEvent) event).getPortfolio());
            updateStocksTable();
        }
    }

    private void updateStocksTable(){

        final BeanContainer<String, StockSummary> shownStocks = (BeanContainer<String, StockSummary>) stocksSummaryTable.getContainerDataSource();


        try {
            UserData userData = new UserDataImpl();
            Portfolio userPortfolio =   userData.getUserPortfolio(Authenticator.getInstance().getCurrentUser());
            final HashMap<String, HashMap<String, Double>> shares = userPortfolio.getShares();

            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    shownStocks.removeAllItems();

                    for (String stock : shares.keySet()) {

                        int quantity = shares.get(stock).get(Terms.QNTY).intValue();
                        double avgPrice = shares.get(stock).get(Terms.PRICE);
                        StockSummary tmp = new StockSummary(stock, quantity, avgPrice);

                        shownStocks.addBean(tmp);
                    }

                    getUI().push();
                }
            });

        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }

    private void createStocksTable(){

        //stocksSummaryTable.setColumnHeaders(new String[]{"Stock","Shares","Average"});

        BeanContainer<String, StockSummary> myStocks = new BeanContainer<String, StockSummary>(StockSummary.class);
        myStocks.setBeanIdProperty("stockID");


        stocksSummaryTable  = new Table("My Portfolio", myStocks);

        stocksSummaryTable.setSizeFull();
        stocksSummaryTable.setWidth("90%");
        stocksSummaryTable.setSelectable(true);
        stocksSummaryTable.setImmediate(true);

        stocksSummaryTable.setColumnHeader("stockID", "Stock");
        stocksSummaryTable.setColumnHeader("stocks", "Shares");
        stocksSummaryTable.setColumnHeader("value", "Total Value");

        stocksSummaryTable.setVisibleColumns(new String[]{"stockID","stocks","value"});


        updateStocksTable();
    }

    public class StockSummary{

        private String stockID;
        private int stocks;
        private double value;

        public StockSummary(String stockID, int stocks, double value) {
            this.stockID = stockID;
            this.stocks = stocks;
            this.value = value;
        }

        public String getStockID() {
            return stockID;
        }

        public void setStockID(String stockID) {
            this.stockID = stockID;
        }

        public int getStocks() {
            return stocks;
        }

        public void setStocks(int stocks) {
            this.stocks = stocks;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}


