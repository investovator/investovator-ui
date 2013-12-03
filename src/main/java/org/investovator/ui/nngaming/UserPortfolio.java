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
import org.investovator.ann.nngaming.NNGamingFacade;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.commons.utils.Terms;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.nngaming.beans.StockSummaryBean;
import org.investovator.ui.utils.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class UserPortfolio extends HorizontalLayout {

    //External Data
    UserData userData;

    //Layout Components
    Label accountBalance;
    Label blockedAmount;
    Label date;
    Table stocksSummaryTable;

    private double USERCASH = 1000000.0;
    private double USERBLOCKEDCASH = 0.0;

    private EventBroadcaster eventBroadcaster;
    private String currentInstance;
    private NNGamingFacade nnGamingFacade;

    public UserPortfolio() {

        currentInstance = Session.getCurrentGameInstance();
        nnGamingFacade = NNGamingFacade.getInstance();

        if(userData == null){
            try {
                userData = new UserDataImpl();

            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        loadUserPortfolio();

        setupUI();


    }

    public void setupUI(){

        this.setHeight("100%");

        accountBalance=new Label();
        accountBalance.setCaption("Cash Balance");

        blockedAmount = new Label();
        blockedAmount.setCaption("Blocked Amount");

        date = new Label();
        date.setCaption("Date");

        createStocksTable();

        HorizontalLayout portSummary = new HorizontalLayout();
        portSummary.addComponent(date);
        portSummary.addComponent(accountBalance);
        portSummary.addComponent(blockedAmount);
        portSummary.setWidth("100%");
        portSummary.setSpacing(true);
        portSummary.setExpandRatio(date,1);
        portSummary.setExpandRatio(accountBalance,1);
        portSummary.setExpandRatio(blockedAmount,1);

        HorizontalLayout stockSummary = new HorizontalLayout();
        stockSummary.addComponent(stocksSummaryTable);
        stockSummary.setWidth("100%");

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(portSummary);
        layout.addComponent(stockSummary);
        layout.setWidth("100%");

        this.addComponent(layout);

        this.setImmediate(true);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        this.setWidth("100%");

    }

    private void loadUserPortfolio(){
        Portfolio portfolio = null;
        String currentUser = Session.getCurrentUser();

        try {
            userData.addUserToGameInstance(currentInstance,Session.getCurrentUser());
            portfolio = userData.getUserPortfolio(currentInstance,currentUser);
        } catch (DataAccessException e) {

            if(portfolio == null) {
                try {
                    portfolio = new PortfolioImpl(currentUser, USERCASH, USERBLOCKEDCASH);
                    userData.updateUserPortfolio(currentInstance,currentUser, portfolio);
                } catch (DataAccessException e1) {
                    e1.printStackTrace();
                }
            }

        }

    }

    public void update(){

        try {
            String currentUser = Session.getCurrentUser();
            Double balance = userData.getUserPortfolio(currentInstance,currentUser).getCashBalance();
            accountBalance.setValue(String.format("%.2f", balance));
            Double blocked = userData.getUserPortfolio(currentInstance,currentUser).getBlockedCash();
            blockedAmount.setValue(String.format("%.2f", blocked));
            Date currentDate = nnGamingFacade.getDateRange("SAMP")[1];
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE,3);
            DateFormat df = new SimpleDateFormat("dd MMM yyyy");
            String dateString = df.format(calendar.getTime());
            date.setValue(dateString);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }


    public void updatePortfolio(final Portfolio portfolio){

        if (this.isConnectorEnabled()) {
            getSession().lock();
            try {

                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        accountBalance.setValue(String.format("%.2f", portfolio.getCashBalance()));
                        blockedAmount.setValue(String.format("%.2f", portfolio.getBlockedCash()));
                    }
                });

            } finally {
                getSession().unlock();
            }
        }
    }

    public void updateStocksTable(){

        final BeanContainer<String, StockSummaryBean> shownStocks = (BeanContainer<String, StockSummaryBean>) stocksSummaryTable.getContainerDataSource();

        try {
            Portfolio userPortfolio =  userData.getUserPortfolio(currentInstance,Session.getCurrentUser());
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


        stocksSummaryTable  = new Table("", myStocks);

        stocksSummaryTable.setWidth("80%");
        stocksSummaryTable.setSelectable(true);
        stocksSummaryTable.setImmediate(true);
        stocksSummaryTable.setPageLength(4);

        stocksSummaryTable.setColumnHeader("stockID", "Stock");
        stocksSummaryTable.setColumnHeader("stocks", "Shares");

        stocksSummaryTable.setVisibleColumns(new String[]{"stockID","stocks"});

        updateStocksTable();
    }

    public void updateDate(final Date currentDate){

        if (this.isConnectorEnabled()) {
            getSession().lock();
            try {

                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
                        String dateString = df.format(currentDate);
                        date.setValue(dateString);
                        getUI().push();
                    }
                });

            } finally {
                getSession().unlock();
            }
        }

    }

}
