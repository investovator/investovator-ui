package org.investovator.ui.agentgaming.user.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class WatchListTable extends Table implements Button.ClickListener {


    public WatchListTable() {

        setSizeUndefined();
        setSelectable(true);
        setImmediate(true);

        setColumns();

    }

    public void updateTable(){
        removeAllItems();
        setTableRows();
    }

    public void addCompany(String stockID, String companyName) {
        Button removeButton = new Button("Remove");
        removeButton.setData(stockID);
        removeButton.addClickListener(this);
        addItem(new Object[]{stockID, companyName, removeButton}, stockID);
    }


    private void setColumns() {

        addContainerProperty("Symbol", String.class, null);
        addContainerProperty("Company Name", String.class, null);
        addContainerProperty("Remove",       Button.class, null);

        setColumnExpandRatio("Symbol", 1);
        setColumnExpandRatio("Company Name", 5);
        setColumnExpandRatio("Remove", 1);


    }


    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {

        String removedStock = clickEvent.getButton().getData().toString();

        try {
            UserData userData = new UserDataImpl();
            userData.deleteFromWatchList(Session.getCurrentGameInstance(),Authenticator.getInstance().getCurrentUser(), removedStock);
            updateTable();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }


    public void setTableRows(){


        try {
            UserData userData = new UserDataImpl();
            CompanyData companyData =new CompanyDataImpl();
            HashMap<String,String> companyNames = companyData.getCompanyIDsNames();

            ArrayList<String> watchlist =  userData.getWatchList(Session.getCurrentGameInstance(),Authenticator.getInstance().getCurrentUser());

            if(watchlist != null){
                for(String stock : watchlist){
                    addCompany(stock, companyNames.get(stock));
                }
            }


        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }
}
