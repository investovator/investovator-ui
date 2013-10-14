package org.investovator.ui.agentgaming;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.investovator.ui.utils.dashboard.BasicDashboard;

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
        super.setupUI(viewChangeEvent);
    }

    @Override
    public LinkedHashMap<String, Panel> getMenuItems() {
        LinkedHashMap<String, Panel> menuList = new LinkedHashMap<String, Panel>();
        mainDashView = new DashboardPlayingView();
        menuList.put("my dashboard", mainDashView);
        return menuList;
    }
}
