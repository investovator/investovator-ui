package org.investovator.ui.nngaming;

import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.investovator.ui.GlobalView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/7/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class NNGamingView extends GlobalView implements Upload.Receiver,Upload.SucceededListener {

    private HashMap<String,String> parameters;
    private final Window paramWindow;
    private final TwinColSelect select;
    private TextField newParamField;
    private FormLayout layout;

    public NNGamingView(){
        paramWindow = new Window();
        select = new TwinColSelect();
        layout = new FormLayout();
        newParamField = new TextField();
        parameters = new HashMap<String, String>();
        initUI();
    }

    private void initUI(){

        initTwincolSelect();
        initParamWindow();
        Button createGame = new Button("Create Game",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        layout.addComponent(createGame);
        addComponent(layout);
    }

    private void initParamWindow(){
        FormLayout modalLayout = new FormLayout();
        Upload upload = new Upload();
        upload.setButtonCaption("Add Parameter");
        Label newParam = new Label("Specify New Parameter");
        Label dataSet = new Label("Specify Data Set");
        modalLayout.addComponents(newParam,newParamField,dataSet,upload);
        modalLayout.setMargin(true);
        paramWindow.setContent(modalLayout);
        paramWindow.setModal(true);

        upload.addSucceededListener(this);
    }

    private void initTwincolSelect(){
        Label parameterLabel = new Label("Select Input Parameters for the Game..!!");
        select.setLeftColumnCaption("Available Input Parameters");
        select.setRightColumnCaption("Selected Input Parameters");
        // Put some data in the select
        String param[] = {"High Price","Low Price","Closing Price","No of Trades","No of Shares","Turnover"};
        for (int count=0; count<param.length; count++)
        {
            select.addItem(param[count]);
        }
        select.setRows(param.length);
        select.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Collection selectedItems = (Collection) event.getProperty().getValue();
                    Notification.show(selectedItems.toString());
            }
        });
        select.setImmediate(true);


        Button newParam = new Button("Add New Parameter",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
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
        //To change body of implemented methods use File | Settings | File Templates.
        Notification.show("Welcome to NN Gaming Engine");
    }

    @Override
    public OutputStream receiveUpload(String filename,String mimeType) {
        Notification.show("In Call Back");
        File file;
        FileOutputStream fos = null;
        try {
            // Open the file for writing.
            file = new File("/resources/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        Notification.show("In Call Back");
    }
}
