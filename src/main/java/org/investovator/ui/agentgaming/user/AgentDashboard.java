package org.investovator.ui.agentgaming.user;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.agentsimulation.api.JASAFacade;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.enums.GameStates;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.TestDataGenerator;
import org.investovator.ui.utils.UIConstants;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

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

        if(Session.getCurrentGameInstance()==null){
            getUI().getNavigator().navigateTo(UIConstants.USER_VIEW);
        }

       /* GameFacade instance =   GameControllerFacade.getInstance();

        if(Authenticator.getInstance().getMyPrivileges()== Authenticator.UserType.ADMIN) getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);

        if(instance.getCurrentGameMode()!= GameModes.AGENT_GAME || instance.getCurrentGameState()!= GameStates.RUNNING){
           Notification.show("No Agent Game is Configured");
           getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
        }*/

        JASAFacade facade = JASAFacade.getMarketFacade();
        String user = Authenticator.getInstance().getCurrentUser();
        Properties gameConfig = new Properties();
        try {

            gameConfig.load(new FileReader(System.getProperty("game_properties_url")));
            double funds =  Double.parseDouble(gameConfig.getProperty("initialFunds"));
            if (!facade.isUserAgentAvailable(user)) facade.AddUserAgent(user,funds );

            UserData userData = new UserDataImpl();
            if(!userData.getGameInstanceUsers(Session.getCurrentGameInstance()).contains(user)){
                TestDataGenerator.registerUser(user,Session.getCurrentGameInstance(),funds);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menuList = new LinkedHashMap<String, DashboardPanel>();
        mainDashView = new DashboardPlayingView();
        reportView = new ReportsView();
        menuList.put("my dashboard", mainDashView);
        menuList.put("watch list", new WatchListView());
        menuList.put("market reports", reportView);

        return menuList;
    }
}
