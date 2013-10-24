package org.investovator.ui.agentgaming;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
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

    public AgentDashboard() {
        super("<span><center>investovator</center></span>Dashboard");
    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menuList = new LinkedHashMap<String, DashboardPanel>();
        mainDashView = new DashboardPlayingView();
        menuList.put("my dashboard", mainDashView);
        return menuList;
    }
}
