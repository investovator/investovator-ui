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

package org.investovator.ui.nngaming;

import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.investovator.ui.GlobalView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 *
 */
public class NNGamingView extends GlobalView implements Upload.Receiver,Upload.SucceededListener {

    private final Window paramWindow;
    private final TwinColSelect select;

    private HashMap<String,String> parameters;
    private TextField newParamField;
    private FormLayout layout;
    private String selectedStockID;
    private ArrayList<String> selectedParameters;
    private Select selectStockID;

    public NNGamingView(){

        paramWindow = new Window();
        select = new TwinColSelect();
        layout = new FormLayout();
        newParamField = new TextField();

        parameters = new HashMap<String, String>();

        initUI();
    }

    private void initStockList(){

        Label stockIDLabel = new Label("Please specify the Stock ID");
        selectStockID = new Select();

        selectStockID.setNullSelectionAllowed(false);
        selectStockID.addItem("Sampath");

        layout.addComponents(stockIDLabel,selectStockID);
    }

    private void initUI(){

        initStockList();
        initTwincolSelect();
        initParamWindow();

        Button createGame = new Button("Create Game");
        createGame.addClickListener(listener);

        layout.addComponent(createGame);
        addComponent(layout);
    }

    private void initParamWindow(){
        FormLayout modalLayout = new FormLayout();
        Upload upload = new Upload();
        Label newParam = new Label("Specify New Parameter");
        Label dataSet = new Label("Specify Data Set");

        modalLayout.addComponents(newParam,newParamField,dataSet,upload);
        modalLayout.setMargin(true);

        paramWindow.setContent(modalLayout);
        paramWindow.setModal(true);
        paramWindow.setResizable(false);

        upload.setButtonCaption("Add Parameter");
        upload.addSucceededListener(this);
        upload.setReceiver(this);
    }

    private void initTwincolSelect(){
        Label parameterLabel = new Label("Select Input Parameters for the Game..!!");

        select.setLeftColumnCaption("Available Input Parameters");
        select.setRightColumnCaption("Selected Input Parameters");

        String param[] = {"High Price","Low Price","Closing Price","No of Trades","No of Shares","Turnover"};
        for (String aParam : param) {
            select.addItem(aParam);
        }

        select.setRows(param.length);

        select.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                    Set<String> results = (Set) event.getProperty().getValue();

                    selectedParameters = new ArrayList<String>(results.size());

                    int i = 0;

                    for (Iterator<String> iterator = results.iterator(); iterator.hasNext();) {
                        String next = iterator.next();
                        selectedParameters.add(next);
                        i++;
                    }
            }
        });
        select.setImmediate(true);

        Button newParam = new Button("Add New Parameter",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(paramWindow);
            }
        });

        layout.addComponents(parameterLabel,select,newParam);
    }

    private void addNewParameter(String param, String filePath){
         parameters.put(param,filePath);
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to NN Gaming Engine");
    }

    @Override
    public OutputStream receiveUpload(String filename,String mimeType) {
        File file;
        FileOutputStream fos;

        try {
                // Open the file for writing.
                file = new File(filename);
                fos = new FileOutputStream(file);

                addNewParameter(newParamField.getValue(),file.getAbsolutePath());
                newParamField.setValue("");

        } catch (final java.io.FileNotFoundException e) {
                new Notification("Could not open file<br/>",e.getMessage(),
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            return null;
        }
        return fos;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        Notification.show("Parameter Added");
        UI.getCurrent().removeWindow(paramWindow);
    }

    Button.ClickListener listener = new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            Notification.show("Hello", Notification.Type.ERROR_MESSAGE);
            /* ToDo */
        }
    };
}
