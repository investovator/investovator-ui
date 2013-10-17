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
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class StockSelectView implements WizardStep{

    String selectedStock;
    Select stockSelectList;
    VerticalLayout content;

    public StockSelectView() {
        stockSelectList = new Select("Select Stock for Game");

        stockSelectList.setNullSelectionAllowed(false);

        stockSelectList.addItem("SAMP");
        stockSelectList.addItem("GOOG");
        stockSelectList.addItem("IBM");

        stockSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                selectedStock = (String) valueChangeEvent.getProperty().getValue();
            }
        });

        content = new VerticalLayout();
        content.addComponent(stockSelectList);
    }

    @Override
    public String getCaption() {
        return "Select Stocks";
    }

    @Override
    public Component getContent() {
        return  content;
    }

    @Override
    public boolean onAdvance() {
        return true;
    }

    @Override
    public boolean onBack() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getSelectedStock(){
        return selectedStock;
    }
}
