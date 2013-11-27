/*
 * investovator, Stock Market Gaming framework
 * Copyright (C) 2013  investovator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.main;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.LinkedHashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class UserDashboard extends BasicDashboard {

    public UserDashboard(){
        super("<span><center>Investovator</center></span>Dashboard");
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {

        LinkedHashMap<String, DashboardPanel> panels = new LinkedHashMap<>();
        panels.put("my games", new MyGamesPanel());
        return panels;
    }
}
