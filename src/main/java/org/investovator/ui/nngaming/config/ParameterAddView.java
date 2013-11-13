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

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.teemu.wizards.WizardStep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class ParameterAddView implements WizardStep, Upload.SucceededListener, Upload.Receiver {

    HashMap<String, String> newParameters;

    UploadField upload;
    Label newParam;
    Label dataSet;
    TextField newParamField;

    FormLayout formContent;
    VerticalLayout content;


    public ParameterAddView() {

        newParameters = new HashMap();

        upload = new UploadField();
        newParam = new Label("Specify New Parameter");
        dataSet = new Label("Specify Data Set");
        newParamField = new TextField();

        upload.setButtonCaption("Choose Files");
        /*upload.addSucceededListener(this);
        upload.setReceiver(this);
        upload.*/

        Button addParam = new Button("Add Parameter");
        addParam.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                System.out.println(upload.isValid());
            }
        });

        formContent = new FormLayout();
        formContent.addComponents(newParam, newParamField, dataSet, upload,addParam);
        content = new VerticalLayout();
        content.addComponent(formContent);
    }

    @Override
    public String getCaption() {
        return "Add New Input Parameters";
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
        return true;
    }

    public HashMap getAddedParameterList(){
        return newParameters;
    }

    private void addParameter(String parameter, String filePath){
        newParameters.put(parameter, filePath);
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        Notification.show("Parameter Added");
    }

    @Override
    public OutputStream receiveUpload(String filename,String mimeType) {
        File file;
        FileOutputStream fos;

        try {
            // Open the file for writing.
            file = new File(filename);
            fos = new FileOutputStream(file);
            String newParameter = newParamField.getValue();

            addParameter(newParameter,file.getAbsolutePath());
            newParamField.setValue("");

        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",e.getMessage(),
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            return null;
        }
        return fos;
    }
}
