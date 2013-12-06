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


package org.investovator.ui.dataplayback.admin.dashboard;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.ui.dataplayback.admin.DataPlaybackEngineAdminDashboard;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;
import org.investovator.ui.utils.dashboard.IconLoader;

import java.util.LinkedHashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class AdminDashboardLoader extends BasicDashboard{

    public AdminDashboardLoader() {
        super("<span><center>investovator</center></span> Data Playback");
    }

    @Override
    public LinkedHashMap<IconLoader, DashboardPanel> getMenuItems() {
        LinkedHashMap<IconLoader,DashboardPanel> map=new LinkedHashMap<IconLoader, DashboardPanel>();

        map.put(IconLoader.OVERVIEW, new DataPlaybackEngineAdminDashboard());


        return map;

    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
