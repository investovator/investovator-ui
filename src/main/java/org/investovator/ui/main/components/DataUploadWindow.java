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
    boolean newCompanySettingsShown;
    private String selectedStock;

    public DataUploadWindow(){

        newCompanySettingsShown = false;
        setLayout();

    }

    private void setLayout(){

        //Window Properties
        setCaption("Upload Data");
        setResizable(false);
        setClosable(true);
        center();

        content = new VerticalLayout();
        content.setSpacing(true);
        companySelectLayout = new HorizontalLayout();
        companyDetailUploadLayout = new HorizontalLayout();

        newCompanyCheckBox = new CheckBox("New Company");

        companySelect = new ComboBox();

        companySelect = new ComboBox();
        companySelect.setNullSelectionAllowed(false);
        companySelect.setImmediate(true);

        dataUpload = new Upload();
        dataUpload.setCaption("Select Data File");
        dataUpload.setButtonCaption(null);

        detailUpload = new Upload();
        detailUpload.setCaption("select Company Info XML");
        detailUpload.setButtonCaption(null);

        companyDetailUploadLayout.addComponent(detailUpload);

        companySelectLayout.addComponent(companySelect);
        companySelectLayout.addComponent(newCompanyCheckBox);

        content.addComponent(companySelectLayout);
        content.addComponent(dataUpload);

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
        detailUpload.setReceiver(new DetailFileReceiver());

    }


    private void setNewCompanySettings(boolean enable){

        if(enable && !newCompanySettingsShown){

            content.addComponent(companyDetailUploadLayout, 1);
            companySelect.setEnabled(false);
            newCompanySettingsShown = true;

        }else if(!enable && newCompanySettingsShown){

            content.removeComponent(companyDetailUploadLayout);
            companySelect.setEnabled(true);
            newCompanySettingsShown = false;

        }


    }


    private class DataFileReceiver implements Upload.Receiver{

        @Override
        public OutputStream receiveUpload(String s, String s2) {
            return null;
        }
    }

    private class DetailFileReceiver implements Upload.Receiver{

        @Override
        public OutputStream receiveUpload(String s, String s2) {
            return null;
        }
    }
}
