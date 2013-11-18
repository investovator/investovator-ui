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

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.main.components.CompanyTable;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DataImportPanel extends DashboardPanel{

    //Layout Components
    VerticalLayout content;
    HorizontalLayout ohclData;
    CompanyTable ohclCompaniesTable;

    public  DataImportPanel(){
        createLayout();
    }

    private void createLayout(){

        content = new VerticalLayout();

        //OHCL Data
        ohclData = new HorizontalLayout();
        ohclData.setCaption("Summary Data");
        ohclData.setStyleName("center-caption");


        ohclCompaniesTable = new CompanyTable();
        ohclData.addComponent(ohclCompaniesTable);


        content.addComponent(ohclData);
        this.setContent(content);


    }



    @Override
    public void onEnter() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
