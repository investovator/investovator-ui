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
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class StockSelectView implements WizardStep{

    private ArrayList<String> selectedStocks = null;
    private CompanyDataImpl companyData;
    private ArrayList<String> stockIDList;

    TwinColSelect stockSelectList;
    VerticalLayout content;

    public StockSelectView() {

        stockSelectList = new TwinColSelect("Select Stocks for Game");

        stockSelectList.setHeight(28, Sizeable.Unit.MM);

        stockSelectList.setNullSelectionAllowed(false);

        stockSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                Set<String> results = (Set<String>) valueChangeEvent.getProperty().getValue();

                selectedStocks = new ArrayList<>();

                for (Iterator<String> iterator = results.iterator(); iterator.hasNext(); ) {
                    String next = iterator.next();
                    selectedStocks.add(next);
                }
            }
        });

        content = new VerticalLayout();
        content.addComponent(stockSelectList);
        content.setComponentAlignment(stockSelectList, Alignment.MIDDLE_CENTER);
        content.setMargin(true);
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
        if(selectedStocks == null)
        {
            Notification.show("Please Select Stocks for the Game", Notification.Type.TRAY_NOTIFICATION);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean onBack() {
        return false;
    }

    public ArrayList<String> getSelectedStocks(){
        return selectedStocks;
    }
}
