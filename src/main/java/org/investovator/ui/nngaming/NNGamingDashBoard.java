/*
 * investovator, Stock Market Gaming Framework
 *     Copyright (C) 2013  investovator
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.nngaming;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.ui.analysis.AnalysisPanel;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.LinkedHashMap;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class NNGamingDashBoard extends BasicDashboard{

    DashboardPlayingView mainDashView;
    DashboardAnalysisView analysisView;
    AnalysisPanel analysisPanel;

    public NNGamingDashBoard() {
        super("<span><center>investovator</center></span>Dashboard");
    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
       /* GameController controller = GameControllerImpl.getInstance();

        if(Authenticator.getInstance().getMyPrivileges()== Authenticator.UserType.ADMIN) getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);

        if(controller.getCurrentGameMode()!= GameModes.NN_GAME || controller.getCurrentGameState()!= GameStates.RUNNING){
            Notification.show("No Neural Network Game is Configured");
            getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
        }*/
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menuList = new LinkedHashMap<String, DashboardPanel>();
        mainDashView = new DashboardPlayingView();
        menuList.put("MAIN VIEW", mainDashView);

       /* VerticalLayout panelContent2 = new VerticalLayout();
        panelContent2.addComponent(new Button("Test 2"));*/

    /*    analysisView = new DashboardAnalysisView();
        menuList.put("DATA OVERVIEW", analysisView);*/

        analysisPanel = new AnalysisPanel();
        menuList.put("ANALYSIS", analysisPanel);

        return menuList;
    }
}
