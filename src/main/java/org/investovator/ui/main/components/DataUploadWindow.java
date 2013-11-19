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

package org.investovator.ui.main.components;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;

import java.io.OutputStream;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DataUploadWindow extends Window{

    //Layout
    private VerticalLayout content;
    private ComboBox companySelect;
    private HorizontalLayout companySelectLayout;
    private HorizontalLayout companyDetailUploadLayout;
    private Upload dataUpload;
    private Upload detailUpload;
    private CheckBox newCompanyCheckBox;

    //state
    boolean newCompanySettngsShown;
    private String selectedStock;

    public DataUploadWindow(){

        newCompanySettngsShown = false;
        setLayout();

    }

    private void setLayout(){

        //Window Properties
        setCaption("Upload Data");

        content = new VerticalLayout();

        companySelect = new ComboBox();

        companySelect = new ComboBox("New Company");
        companySelect.setNullSelectionAllowed(false);
        companySelect.setImmediate(true);

        dataUpload = new Upload();
        dataUpload.setCaption(null);

        companySelectLayout.addComponent(companySelect);

        content.addComponent(companySelectLayout);

        setContent(content);
        setDefaults();
        bindListeners();

    }


    private void setDefaults(){

        try {
            CompanyData companyData = new CompanyDataImpl();

            for(String stock : companyData.getAvailableStockIds()){
                  companySelect.addItem(stock);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }

    private void bindListeners(){


        newCompanyCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean status = newCompanyCheckBox.getValue();
                setNewCompanySettings(status);
            }
        });


        //File Receivers
        dataUpload.setReceiver(new DataFileReceiver());
        detailUpload.setReceiver(new DataFileReceiver());

    }


    private void setNewCompanySettings(boolean enable){

        if(enable && !newCompanySettngsShown){

            newCompanySettngsShown = true;
        }else if(!enable && newCompanySettngsShown){

            newCompanySettngsShown = false;
        }


    }


    private class DataFileReceiver implements Upload.Receiver{

        @Override
        public OutputStream receiveUpload(String s, String s2) {
            return null;
        }
    }
}
