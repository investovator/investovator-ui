package org.investovator.ui.main;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.LinkedHashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AgentConfigDashboard extends BasicDashboard {

    MainGamingView configView;
    DataImportPanel dataImportView;

    public AgentConfigDashboard() {
        super("<span><center>Investovator</center></span>Configuration");
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menuList = new LinkedHashMap<String, DashboardPanel>();
        configView = new MainGamingView();
        dataImportView = new DataImportPanel();
        menuList.put("game summary", configView);
        menuList.put("data import", dataImportView);
        return menuList;
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
