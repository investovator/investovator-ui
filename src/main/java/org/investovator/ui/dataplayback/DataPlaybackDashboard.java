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


package org.investovator.ui.dataplayback;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.utils.dashboard.BasicDashboard;

import java.util.LinkedHashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlaybackDashboard extends BasicDashboard {
    public DataPlaybackDashboard() {
        super("<span><center>investovator</center></span> Data Playback");
    }

    @Override
    public LinkedHashMap<String, Panel> getMenuItems() {
        LinkedHashMap<String,Panel> map=new LinkedHashMap<String, Panel>();

        map.put("main view", new DataPlaybackMainView());

        /*
        Example Button 2
         */
        VerticalLayout panelContent2 = new VerticalLayout();
        panelContent2.addComponent(new Button("Test 2"));

        Panel panel2 = new Panel();
        panel2.setContent(panelContent2);
        map.put("test 2", panel2);
         /*
        End of Example Button 2
         */

        return map;
    }
}
