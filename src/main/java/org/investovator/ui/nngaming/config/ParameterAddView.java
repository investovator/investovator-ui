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

import com.vaadin.ui.*;
import org.investovator.ui.utils.ConfigHelper;
import org.vaadin.teemu.wizards.WizardStep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class ParameterAddView implements WizardStep, Upload.SucceededListener {

    private HashMap<String, String> newParameters;

    private Upload upload;;
    private TextField newParamField;
    private Button submit;
    private String newParameter;

    private HorizontalLayout layout;
    private VerticalLayout content;


    public ParameterAddView() {

        newParameters = new HashMap();

        upload = new Upload();
        upload.setCaption("Select Data File");
        upload.setButtonCaption(null);

        newParamField = new TextField();
        newParamField.setCaption("Specify New Parameter");
        newParamField.setRequired(true);
        newParamField.setImmediate(true);

        submit = new Button("Upload Data");

        layout = new HorizontalLayout();
        layout.addComponent(newParamField);
        layout.setSpacing(true);
        layout.setMargin(true);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.addComponent(upload);
        bottomLayout.addComponent(submit);
        bottomLayout.setSpacing(true);
        bottomLayout.setMargin(true);
        bottomLayout.setComponentAlignment(upload, Alignment.MIDDLE_LEFT);
        bottomLayout.setComponentAlignment(submit, Alignment.MIDDLE_RIGHT);

        content = new VerticalLayout();
        content.addComponent(layout);
        content.addComponent(bottomLayout);

        bindListeners();
    }

    private void bindListeners(){
        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                if(!newParamField.getValue().isEmpty() && upload.getUploadSize()!=0){
                    newParameter = newParamField.getValue();
                    newParamField.setValue("");
                    upload.submitUpload();
                }
                else {
                    Notification.show("Please enter a valid input parameter or file", Notification.Type.TRAY_NOTIFICATION);
                }

            }
        });

        //File Receivers
        upload.setReceiver(new DataFileReceiver());
        upload.addSucceededListener(this);

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
        //todo
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
        Notification.show("Parameter Added Successfully", Notification.Type.TRAY_NOTIFICATION);
    }

    private String getUploadPath(){

        String path = ConfigHelper.getBasepath() + "/resources/";

        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        return path;
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
                file = new File(getUploadPath()+newParameter+".txt");
                try {
                    file.createNewFile();
                    addParameter(newParameter,(getUploadPath()+newParameter+".txt"));

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

}
