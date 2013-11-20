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
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.utils.ConfigHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DataUploadWindow extends Window implements Upload.SucceededListener{

    //Layout
    private VerticalLayout content;
    private HorizontalLayout companySelectLayout;
    private HorizontalLayout companyDetailUploadLayout;
    private FormLayout newCompanyForm;
    private ComboBox companySelect;
    private ComboBox dataTypeSelect;
    private Upload dataUpload;
    private Upload detailUpload;
    private CheckBox newCompanyCheckBox;
    private Button submitButton;

    private TextField symbol;
    private TextField companyName;

    //state
    boolean newCompany;
    private String selectedStock;
    private CompanyStockTransactionsData.DataType selectedDataType;

    private String OHCL_DATA = "Summary Data";
    private String TICKER_DATA = "Ticker Data";


    public DataUploadWindow(){

        newCompany = false;
        setLayout();

    }

    private void setLayout(){

        //Window Properties
        setCaption("Upload Data");
        setResizable(false);
        setClosable(true);
        setModal(true);
        center();

        content = new VerticalLayout();
        content.setSpacing(true);
        content.setMargin(true);

        companySelectLayout = new HorizontalLayout();
        companyDetailUploadLayout = new HorizontalLayout();
        newCompanyForm = new FormLayout();

        newCompanyCheckBox = new CheckBox("New Company");

        companySelect = new ComboBox();

        companySelect = new ComboBox();
        companySelect.setNullSelectionAllowed(false);
        companySelect.setImmediate(true);

        dataTypeSelect = new ComboBox();
        dataTypeSelect.setNullSelectionAllowed(false);

        dataUpload = new Upload();
        dataUpload.setCaption("Select Data File");
        dataUpload.setButtonCaption(null);

        detailUpload = new Upload();
        detailUpload.setCaption("select Company Info XML");
        detailUpload.setButtonCaption(null);

        symbol = new TextField("Company Symbol");
        symbol.setRequired(true);
        companyName = new TextField("Company Name");
        companyName.setRequired(true);

        submitButton = new Button("Upload Data");

        newCompanyForm.addComponent(symbol);
        newCompanyForm.addComponent(companyName);

        companyDetailUploadLayout.addComponent(detailUpload);

        companySelectLayout.addComponent(companySelect);
        companySelectLayout.addComponent(newCompanyCheckBox);

        content.addComponent(companySelectLayout);
        content.addComponent(dataTypeSelect);
        content.addComponent(dataUpload);
        content.addComponent(submitButton);

        content.setComponentAlignment(submitButton,Alignment.MIDDLE_CENTER);

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
            companySelect.select(companyData.getAvailableStockIds().get(0));
            selectedStock=companyData.getAvailableStockIds().get(0);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        dataTypeSelect.addItem(OHCL_DATA);
        dataTypeSelect.addItem(TICKER_DATA);
        dataTypeSelect.select(OHCL_DATA);
        selectedDataType = CompanyStockTransactionsData.DataType.OHLC;


    }

    private void bindListeners(){

        submitButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                if(newCompany) selectedStock = symbol.getValue();

                dataUpload.submitUpload();

            }
        });


        newCompanyCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean status = newCompanyCheckBox.getValue();
                setNewCompanySettings(status);
            }
        });

        companySelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                selectedStock = (String) companySelect.getValue();
            }
        });

        dataTypeSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String valueString = (String) dataTypeSelect.getValue();
                if(valueString.equals(TICKER_DATA)) selectedDataType = CompanyStockTransactionsData.DataType.TICKER;
                if(valueString.equals(OHCL_DATA)) selectedDataType = CompanyStockTransactionsData.DataType.OHLC;

            }
        });


        //File Receivers
        dataUpload.setReceiver(new DataFileReceiver());
        dataUpload.addSucceededListener(this);
        detailUpload.setReceiver(new DetailFileReceiver());

    }


    private void setNewCompanySettings(boolean enable){

        if(enable && !newCompany){

            content.addComponent(newCompanyForm, 1);
            companySelect.setEnabled(false);
            newCompany = true;

        }else if(!enable && newCompany){

            content.removeComponent(newCompanyForm);
            companySelect.setEnabled(true);
            newCompany = false;

        }


    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {

        if(succeededEvent.getUpload().getReceiver()instanceof DataFileReceiver){

            if(newCompany){

                try {
                    new CompanyDataImpl().addCompanyData(selectedStock, companyName.getValue(), 100000);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }

            CompanyStockTransactionsData historyData = new CompanyStockTransactionsDataImpl();
            try {
                historyData.importCSV(selectedDataType,selectedStock,"MM/dd/yyyy",new File(((DataFileReceiver)succeededEvent.getUpload().getReceiver()).getFile().getAbsolutePath()));
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        close();
        Notification.show("Data Successfully Inserted", Notification.Type.TRAY_NOTIFICATION);
    }


    private class DataFileReceiver implements Upload.Receiver{

        private File file;

        public File getFile(){
            return  file;
        }

        @Override
        public OutputStream receiveUpload(String fileName, String mimeType) {
            // Create upload stream
            FileOutputStream fos = null; // Stream to write to
            try {
                // Open the file for writing.
                file = new File(ConfigHelper.getUploadPath() + selectedStock+".csv");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                e.printStackTrace();
            }
            return fos; // Return the output stream to write to
        }
    }

    private class DetailFileReceiver implements Upload.Receiver{

        @Override
        public OutputStream receiveUpload(String s, String s2) {
            return null;
        }
    }
}
