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
import org.investovator.core.data.api.CompanyData;
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
public class ParameterSelectView implements WizardStep{

    TwinColSelect parameterSelectList;
    VerticalLayout content;

    private ArrayList<String> selectedParameters = null;
    private ArrayList<String> stockIDList;

    private CompanyData companyData;


    public ParameterSelectView() {

        parameterSelectList = new TwinColSelect("Select Analysis Parameters for Game");
        parameterSelectList.setHeight(28, Sizeable.Unit.MM);
        parameterSelectList.setNullSelectionAllowed(false);

        parameterSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                Set<String> results = (Set<String>) valueChangeEvent.getProperty().getValue();

                selectedParameters = new ArrayList<>();

                for (Iterator<String> iterator = results.iterator(); iterator.hasNext(); ) {
                    String next = iterator.next();
                    selectedParameters.add(next.substring(0,next.indexOf(" ")));
                }

            }
        });

        content = new VerticalLayout();
        content.addComponents(parameterSelectList);
        content.setComponentAlignment(parameterSelectList, Alignment.MIDDLE_CENTER);
        content.setMargin(true);
    }

    public void update(){

        parameterSelectList.removeAllItems();

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
            parameterSelectList.addItem(next+" "+"Stock Price");

        }

        parameterSelectList.setValue(stockIDList.get(0)+" "+"Stock Price");
    }

    @Override
    public String getCaption() {
        return "Select Analysis Parameters";
    }

    @Override
    public Component getContent() {
        return  content;
    }

    @Override
    public boolean onAdvance() {
        if(selectedParameters == null)
        {
            Notification.show("Please Select Analysis Parameters", Notification.Type.TRAY_NOTIFICATION);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean onBack() {
        return true;
    }

    public ArrayList<String> getSelectedParameters(){
        return selectedParameters;
    }
}