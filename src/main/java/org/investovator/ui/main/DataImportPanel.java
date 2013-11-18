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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.main.components.CompanyTable;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DataImportPanel extends DashboardPanel{

    //Layout Components
    VerticalLayout content;
    HorizontalLayout dataTableLayout;
    VerticalLayout ohclTable;
    VerticalLayout tickerTable;
    CompanyTable ohclCompaniesTable;
    CompanyTable tickerCompaniesTable;

    Label pageTitle;

    public  DataImportPanel(){
        createLayout();
    }

    private void createLayout(){

        content = new VerticalLayout();

        pageTitle = new Label("Available Transaction Data");
        pageTitle.setSizeUndefined();
        pageTitle.setStyleName("h2");

        ohclTable = new VerticalLayout();
        ohclTable.setCaption("Summary Data");
        ohclTable.addStyleName("center-caption");

        tickerTable = new VerticalLayout();

        dataTableLayout = new HorizontalLayout();

        ohclCompaniesTable = new CompanyTable();
        tickerCompaniesTable = new CompanyTable();

        ohclTable.addComponent(ohclCompaniesTable);

        dataTableLayout.addComponent(ohclTable);
        dataTableLayout.addComponent(tickerTable);

        content.addComponent(pageTitle);
        content.addComponent(dataTableLayout);
        content.setComponentAlignment(pageTitle, Alignment.MIDDLE_CENTER);

        this.setContent(content);


    }

    @Override
    public void onEnter() {
        setDefaults();
    }

    private void setDefaults(){

        try {

            CompanyData companyData = new CompanyDataImpl();

            ArrayList<String> stockIds = companyData.getAvailableStockIds();
            HashMap<String, String> companyNames =  companyData.getCompanyIDsNames();

            CompanyStockTransactionsData transactionData = new CompanyStockTransactionsDataImpl();

            for(String stock: stockIds){

                Date[] dateRange = transactionData.getDataDaysRange(CompanyStockTransactionsData.DataType.OHLC,stock);
                ohclCompaniesTable.addCompany(stock, companyNames.get(stock), dateRange[0], dateRange[1]);

            }


        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }
}
