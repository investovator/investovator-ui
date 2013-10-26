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
import com.vaadin.ui.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Iterator;
import java.util.Set;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class ParameterSelectView implements WizardStep{
    String[] selectedParameters = null;
    Boolean addNewParamStatus;

    TwinColSelect parameterSelectList;

    VerticalLayout content;


    public ParameterSelectView() {
        addNewParamStatus = false;

        parameterSelectList = new TwinColSelect("Select Input Parameters for Game");
        parameterSelectList.addItem(TradingDataAttribute.HIGH_PRICE);
        parameterSelectList.addItem(TradingDataAttribute.LOW_PRICE);
        parameterSelectList.addItem(TradingDataAttribute.CLOSING_PRICE);
        parameterSelectList.addItem(TradingDataAttribute.TRADES);
        parameterSelectList.addItem(TradingDataAttribute.SHARES);
        parameterSelectList.addItem(TradingDataAttribute.TURNOVER);

        parameterSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                Set<String> results = (Set) valueChangeEvent.getProperty().getValue();

                selectedParameters = new String[results.size()];

                int i = 0;

                for (Iterator<String> iterator = results.iterator(); iterator.hasNext(); ) {
                    String next = iterator.next();
                    selectedParameters[i] = next;
                    i++;
                }

            }
        });

        content = new VerticalLayout();
        content.addComponents(parameterSelectList);
    }

    @Override
    public String getCaption() {
        return "Select Input Parameters";
    }

    @Override
    public Component getContent() {
        return  content;
    }

    @Override
    public boolean onAdvance() {
        if(selectedParameters == null)
        {
            Notification.show("Please Select Input Parameters", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean onBack() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String[] getSelectedParameters(){
        return selectedParameters;
    }
}
