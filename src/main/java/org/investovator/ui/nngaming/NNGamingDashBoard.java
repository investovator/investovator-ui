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
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.UIConstants;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.LinkedHashMap;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class NNGamingDashBoard extends BasicDashboard{

    DashboardPlayingView mainDashView;
    StockAnalysisView stockAnalysisView;

    public NNGamingDashBoard() {
        super("<span><center>investovator</center></span>Dashboard");
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menuList = new LinkedHashMap<String, DashboardPanel>();
        mainDashView = new DashboardPlayingView();
        stockAnalysisView = new StockAnalysisView();

        menuList.put("main view", mainDashView);
        menuList.put("stock analysis",stockAnalysisView);

        return menuList;
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        //TODO: remove temporary instance
        Session.setCurrentGameInstance(GameModes.NN_GAME.toString());

        if(Session.getCurrentGameInstance() == null)
            getUI().getNavigator().navigateTo(UIConstants.USER_VIEW);

    }
}
