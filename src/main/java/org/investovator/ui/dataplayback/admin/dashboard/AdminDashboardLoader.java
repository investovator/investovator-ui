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
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.ui.dataplayback.user.dashboard.dailysummary.DailySummarySinglePlayerMainView;
import org.investovator.ui.dataplayback.user.dashboard.realtime.RealTimeMainView;
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
        try{
        if(DataPlaybackEngineStates.currentGameMode== PlayerTypes.REAL_TIME_DATA_PLAYER){
            if(DataPlayerFacade.getInstance().getRealTimeDataPlayer().isMultiplayer()){
                Notification.show("Not implemented-load admin summary view");

            }
//            else{
//                map.put("main view", new RealTimeMainView());
//            }
        }
        else if(DataPlaybackEngineStates.currentGameMode==PlayerTypes.DAILY_SUMMARY_PLAYER){
            if(DataPlayerFacade.getInstance().getDailySummaryDataPLayer().isMultiplayer()){
                Notification.show("Not implemented-load admin summary view");
            }
//            else{
//                map.put("main view", new DailySummarySinglePlayerMainView());
//            }
        }
        }catch (PlayerStateException ex){
            ex.printStackTrace();
        }


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