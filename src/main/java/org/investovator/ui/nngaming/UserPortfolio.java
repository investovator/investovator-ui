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
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.commons.utils.Terms;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.nngaming.beans.StockSummaryBean;
import org.investovator.ui.nngaming.eventinterfaces.BroadcastEvent;
import org.investovator.ui.nngaming.eventobjects.PortfolioData;

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

    private double USERCASH = 1000000.0;
    private double USERBLOCKEDCASH = 0.0;

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

        HorizontalLayout portSummary = new HorizontalLayout();
        portSummary.addComponent(accountBalance);
        portSummary.addComponent(blockedAmount);
        portSummary.setSpacing(true);

        VerticalLayout component = new VerticalLayout();
        component.addComponent(portSummary);
        component.addComponent(stocksSummaryTable);
        component.setExpandRatio(portSummary, 1);
        component.setExpandRatio(stocksSummaryTable, 1);

        this.addComponent(component);
    }


    public void update(){
        String currentUser = Authenticator.getInstance().getCurrentUser();
        Portfolio portfolio = null;

        if(userData == null){
            try {
                userData = new UserDataImpl();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            portfolio = userData.getUserPortfolio(currentUser);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        if(portfolio == null) {
            try {
                portfolio = new PortfolioImpl(currentUser, USERCASH, USERBLOCKEDCASH);
                userData.updateUserPortfolio(currentUser, portfolio);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }

        try {
            currentUser = Authenticator.getInstance().getCurrentUser();
            Double balance = userData.getUserPortfolio(currentUser).getCashBalance();
            accountBalance.setValue(String.format("%.2f", balance));
            Double blocked = userData.getUserPortfolio(currentUser).getBlockedCash();
            blockedAmount.setValue(String.format("%.2f", blocked));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }


    public void updatePortfolio(Portfolio portfolio){

        if (this.isConnectorEnabled()) {
            getSession().lock();
            try {
                accountBalance.setValue(String.format("%.2f", portfolio.getCashBalance()));
                blockedAmount.setValue(String.format("%.2f", portfolio.getBlockedCash()));
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
                        StockSummaryBean tmp = new StockSummaryBean(stock, quantity);

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

        stocksSummaryTable.setVisibleColumns(new String[]{"stockID","stocks"});


        updateStocksTable();
    }

    @Override
    public void onBroadcast(Object object) {

        String userName = Authenticator.getInstance().getCurrentUser();

        if (object instanceof PortfolioData){

            if(((PortfolioData) object).getUserName().equals(userName)){

                if(((PortfolioData) object).isOrderExecuted()){
                    updatePortfolio(((PortfolioData) object).getPortfolio());
                    updateStocksTable();
                }
                else {
                    updatePortfolio(((PortfolioData) object).getPortfolio());
                }
            }
            else {
                return;
            }

        }

    }
}
