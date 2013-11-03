package org.investovator.ui.main;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.ui.agentgaming.DashboardPlayingView;
import org.investovator.ui.agentgaming.ReportsView;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.LinkedHashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AgentConfigDashboard extends BasicDashboard {

    MainGamingView configView;

    public AgentConfigDashboard() {
        super("<span><center>Investovator</center></span>Configuration");
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menuList = new LinkedHashMap<String, DashboardPanel>();
        configView = new MainGamingView();
        menuList.put("game summary", configView);
        return menuList;
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
