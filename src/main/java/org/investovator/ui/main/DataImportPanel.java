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

package org.investovator.ui.main;

import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.main.components.CompanyTable;
import org.investovator.ui.main.components.DataUploadWindow;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DataImportPanel extends DashboardPanel {

    //Layout Components
    VerticalLayout content;
    HorizontalLayout dataTableLayout;
    HorizontalLayout dataInsertLayout;
    VerticalLayout ohclTableLayout;
    VerticalLayout tickerTableLayout;
    CompanyTable ohclCompaniesTable;
    CompanyTable tickerCompaniesTable;
    Button dataInsertButton;
    Label pageTitle;

    public DataImportPanel() {
        createLayout();
    }

    private void createLayout() {

        content = new VerticalLayout();

        pageTitle = new Label("Available Historical Transaction Data");
        pageTitle.setSizeUndefined();
        pageTitle.setStyleName("h2");

        ohclTableLayout = new VerticalLayout();
        ohclTableLayout.setCaption("Summary Data");
        ohclTableLayout.addStyleName("center-caption");
        ohclTableLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        tickerTableLayout = new VerticalLayout();
        tickerTableLayout.setCaption("Ticker Data");
        tickerTableLayout.addStyleName("center-caption");
        tickerTableLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        dataInsertLayout = new HorizontalLayout();
        dataInsertLayout.setWidth("100%");

        dataInsertButton = new Button("Insert Historical Data");

        dataInsertLayout.addComponent(dataInsertButton);
        dataInsertLayout.setComponentAlignment(dataInsertButton, Alignment.MIDDLE_CENTER);

        dataTableLayout = new HorizontalLayout();
        dataTableLayout.setWidth("95%");

        ohclCompaniesTable = new CompanyTable();
        tickerCompaniesTable = new CompanyTable();

        ohclTableLayout.addComponent(ohclCompaniesTable);
        tickerTableLayout.addComponent(tickerCompaniesTable);

        dataTableLayout.addComponent(ohclTableLayout);
        dataTableLayout.addComponent(tickerTableLayout);

        content.addComponent(pageTitle);
        content.addComponent(dataTableLayout);
        content.addComponent(dataInsertLayout);
        content.setComponentAlignment(pageTitle, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(dataTableLayout, Alignment.MIDDLE_CENTER);

        bindEvents();
        this.setContent(content);

    }


    private void bindEvents(){

        dataInsertButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                  getUI().addWindow(new DataUploadWindow());
            }
        });

    }


    @Override
    public void onEnter() {
        setEntryData();
    }

    private void setEntryData() {

        try {

            CompanyData companyData = new CompanyDataImpl();

            ArrayList<String> stockIds = companyData.getAvailableStockIds();
            HashMap<String, String> companyNames = companyData.getCompanyIDsNames();

            CompanyStockTransactionsData transactionData = new CompanyStockTransactionsDataImpl();

            for (String stock : stockIds) {

                Date[] dateRange = transactionData.getDataDaysRange(CompanyStockTransactionsData.DataType.
                        OHLC, stock);
                ohclCompaniesTable.addCompany(stock, companyNames.get(stock), dateRange[0], dateRange[1]);
            }


        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        CompanyData companyData;
        ArrayList<String> stockIds = null;
        HashMap<String, String> companyNames = null;

        try {
            companyData = new CompanyDataImpl();
            stockIds = companyData.getAvailableStockIds();
            companyNames = companyData.getCompanyIDsNames();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        CompanyStockTransactionsData transactionData = new CompanyStockTransactionsDataImpl();

        //TODO: Remove this and add method to get is data available.
        for (String stock : stockIds) {
            Date[] dateRange = new Date[0];
            try {
                dateRange = transactionData.getDataDaysRange(CompanyStockTransactionsData.DataType.TICKER, stock);
                tickerCompaniesTable.addCompany(stock, companyNames.get(stock), dateRange[0], dateRange[1]);

            } catch (DataAccessException e) { }
        }


    }
}
