package org.investovator.ui.agentgaming;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.investovator.controller.GameControllerFacade;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.enums.GameStates;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.UIConstants;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.LinkedHashMap;

/**
 * @author: Ishan Somasiri
 * @author: Amila Surendra
 * @version: ${Revision}
 *
 * Main Agent Dashboard
 */
public class AgentDashboard extends BasicDashboard {

    DashboardPlayingView mainDashView;
    ReportsView reportView;

    public AgentDashboard() {
        super("<span><center>investovator</center></span>Dashboard");
    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        GameControllerFacade instance =   GameControllerFacade.getInstance();

        if(Authenticator.getInstance().getMyPrivileges()== Authenticator.UserType.ADMIN) getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);

        if(instance.getCurrentGameMode()!= GameModes.AGENT_GAME || instance.getCurrentGameState()!= GameStates.RUNNING){
           Notification.show("No Agent Game is Configured");
           getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
        }
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menuList = new LinkedHashMap<String, DashboardPanel>();
        mainDashView = new DashboardPlayingView();
        reportView = new ReportsView();
        menuList.put("my dashboard", mainDashView);
        menuList.put("market reports", reportView);

        return menuList;
    }
}
