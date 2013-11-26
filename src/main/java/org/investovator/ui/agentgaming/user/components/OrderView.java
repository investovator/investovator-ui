package org.investovator.ui.agentgaming.user.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.investovator.agentsimulation.api.JASAFacade;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.GameFacade;
import org.investovator.controller.command.agent.UnmatchedOrdersCommand;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.core.commons.simulationengine.MarketOrder;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class OrderView extends Table {

    GameController controller = GameControllerImpl.getInstance();

    public OrderView(){
        setLayout();
    }


    private void setLayout(){
        setColumns();
    }


    /**
     * Should implement this method to provide data source for shown orders.
     * @return List of orders
     */
    public HashMap<String,ArrayList<MarketOrder>> getOrders(){
        UnmatchedOrdersCommand command = new UnmatchedOrdersCommand(Authenticator.getInstance().getCurrentUser());
        HashMap<String, ArrayList<MarketOrder>> orders =null;

        try {
            controller.runCommand(Session.getCurrentGameInstance(), command);
            orders = command.getOrders();
        } catch (CommandSettingsException e) {
            e.printStackTrace();
        }
        return orders;
    }


    private void setColumns() {
        addContainerProperty("Symbol", String.class, null);
        addContainerProperty("Price", Double.class, null);
        addContainerProperty("Quantity", Integer.class, null);
        addContainerProperty("Side", String.class, null);

        setColumnExpandRatio("Symbol", 1);
        setColumnExpandRatio("PriceCompany Name", 1);
        setColumnExpandRatio("Quantity", 1);
        setColumnExpandRatio("Side", 1);
    }

    private void calculateColumns(){
        removeAllItems();

        HashMap<String,ArrayList<MarketOrder>> orders = getOrders();

    }


    public void update(){

    }




}



