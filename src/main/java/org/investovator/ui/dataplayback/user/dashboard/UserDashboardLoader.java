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


package org.investovator.ui.dataplayback.user.dashboard;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.ui.dataplayback.user.dashboard.dailysummary.DailySummaryMultiPlayerMainView;
import org.investovator.ui.dataplayback.user.dashboard.dailysummary.DailySummarySinglePlayerMainView;
import org.investovator.ui.dataplayback.user.dashboard.dailysummary.DailySummaryStockDataView;
import org.investovator.ui.dataplayback.user.dashboard.realtime.RealTimeMainView;
import org.investovator.ui.dataplayback.user.dashboard.realtime.RealTimeStockDataView;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.BasicDashboard;
import org.investovator.ui.utils.dashboard.DashboardPanel;
import org.investovator.ui.utils.dashboard.dataplayback.BasicStockDataView;

import java.util.LinkedHashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class UserDashboardLoader extends BasicDashboard {

    public UserDashboardLoader() {
        super("<span><center>investovator</center></span> Data Playback");
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String,DashboardPanel> map=new LinkedHashMap<String, DashboardPanel>();

        //if this is a daily summary data game
        if(DataPlaybackEngineStates.gameConfig.getPlayerType()== PlayerTypes.DAILY_SUMMARY_PLAYER){
            //if this is a multiplayer game
            if(DataPlaybackEngineStates.isMultiplayer){
                map.put("main view", new DailySummaryMultiPlayerMainView());
                map.put("stocks", new DailySummaryStockDataView());


            }
            else{
                map.put("main view", new DailySummarySinglePlayerMainView());
                map.put("stocks", new DailySummaryStockDataView());


            }
        }
        //if this is a real time data game
        else if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.REAL_TIME_DATA_PLAYER){
            map.put("main view", new RealTimeMainView());
            map.put("stocks", new RealTimeStockDataView());


        }

        return map;

    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
