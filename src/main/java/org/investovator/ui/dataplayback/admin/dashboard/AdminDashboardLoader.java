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
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.dataplayback.DataPlaybackMainView;
import org.investovator.ui.dataplayback.util.DataPLaybackEngineGameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;

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
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String,DashboardPanel> map=new LinkedHashMap<String, DashboardPanel>();

//        //if this is a daily summary data game
//        if(DataPlaybackEngineStates.currentGameMode== DataPLaybackEngineGameTypes.OHLC_BASED){
//            //todo - load DAilySummaryMainView
            map.put("main view", new DataPlaybackMainView());
//        }
//        //if this is a real time data game
//        else if(DataPlaybackEngineStates.currentGameMode==DataPLaybackEngineGameTypes.TICKER_BASED){
//
//        }

        VerticalLayout panelContent2 = new VerticalLayout();
        panelContent2.addComponent(new Button("Test 2"));

        DashboardPanel panel2 = new DashboardPanel() {
            @Override
            public void onEnter() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        panel2.setContent(panelContent2);
        map.put("test 2", panel2);

        return map;

    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
