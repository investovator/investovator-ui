package org.investovator.ui.main;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;
import org.investovator.ui.utils.dashboard.IconLoader;

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
    public LinkedHashMap<IconLoader, DashboardPanel> getMenuItems() {
        LinkedHashMap<IconLoader, DashboardPanel> menuList = new LinkedHashMap<IconLoader, DashboardPanel>();
        configView = new MainGamingView();
        dataImportView = new DataImportPanel();
        menuList.put(IconLoader.GAME_SUMMARY, configView);
        menuList.put(IconLoader.DATA_IMPORT, dataImportView);
        return menuList;
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
