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

package org.investovator.ui.nngaming;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.controller.utils.events.PortfolioChangedEvent;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.nngaming.beans.StockSummaryBean;
import org.investovator.ui.nngaming.eventinterfaces.BroadcastEvent;

import java.util.HashMap;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class UserPortfolio extends HorizontalLayout implements BroadcastEvent {

    //External Data
    UserData userData;

    //Layout Components
    Label accountBalance;
    Label blockedAmount;
    Table stocksSummaryTable;

    private EventBroadcaster eventBroadcaster;

    public UserPortfolio() {
        setupUI();

        eventBroadcaster = EventBroadcaster.getInstance();
        eventBroadcaster.addListener(this);
    }

    public void setupUI(){

        this.setWidth("100%");
        this.setHeight("100%");
        this.setCaption("Portfolio Summary");
        addStyleName("center-caption");


        accountBalance=new Label();
        accountBalance.setCaption("Cash Balance");

        blockedAmount = new Label();
        blockedAmount.setCaption("Blocked Amount");

        createStocksTable();

        VerticalLayout portSummary = new VerticalLayout();
        portSummary.addComponent(accountBalance);
        portSummary.addComponent(blockedAmount);

        this.addComponent(portSummary);
        this.addComponent(stocksSummaryTable);
        this.setExpandRatio(portSummary,1);
        this.setExpandRatio(stocksSummaryTable,1);
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

    private void updateStocksTable(){

        final BeanContainer<String, StockSummaryBean> shownStocks = (BeanContainer<String, StockSummaryBean>) stocksSummaryTable.getContainerDataSource();


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
                        StockSummaryBean tmp = new StockSummaryBean(stock, quantity, avgPrice);

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

        BeanContainer<String, StockSummaryBean> myStocks = new BeanContainer<>(StockSummaryBean.class);
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

    @Override
    public void onBroadcast(Object object) {

        if (object instanceof PortfolioChangedEvent){
            updatePortfolio(((PortfolioChangedEvent) object).getPortfolio());
            updateStocksTable();
        }

    }
}