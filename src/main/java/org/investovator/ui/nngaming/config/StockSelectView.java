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

package org.investovator.ui.nngaming.config;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class StockSelectView implements WizardStep{

    private String selectedStock = null;
    private CompanyDataImpl companyData;
    private ArrayList<String> stockIDList;

    ListSelect stockSelectList;
    VerticalLayout content;

    public StockSelectView() {

        stockSelectList = new ListSelect("Select Stock for Game");

        stockSelectList.setNullSelectionAllowed(false);

        stockSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                selectedStock = (String) valueChangeEvent.getProperty().getValue();
            }
        });

        content = new VerticalLayout();
        content.addComponent(stockSelectList);
    }

    public void update(){

        stockSelectList.removeAllItems();

        try {
            companyData = new CompanyDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        try {
            stockIDList = companyData.getAvailableStockIds();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        for (Iterator<String> iterator = stockIDList.iterator(); iterator.hasNext(); ) {

            String next = iterator.next();
            stockSelectList.addItem(next);

        }

        stockSelectList.setValue(stockIDList.get(0));
    }

    @Override
    public String getCaption() {
        return "Select Stock";
    }

    @Override
    public Component getContent() {
        return  content;
    }

    @Override
    public boolean onAdvance() {
        if(selectedStock == null)
        {
            selectedStock = (String) stockSelectList.getValue();
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    public String getSelectedStock(){
        return selectedStock;
    }
}
