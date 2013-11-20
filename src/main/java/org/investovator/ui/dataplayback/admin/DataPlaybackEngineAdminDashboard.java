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


package org.investovator.ui.dataplayback.admin;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.ui.dataplayback.beans.PlayerInformationBean;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.gametype.GameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlaybackEngineAdminDashboard extends DashboardPanel {

    GridLayout content;
    DataPlayer player;

    public DataPlaybackEngineAdminDashboard() {
        this.content = new GridLayout(3,3);
        this.setContent(this.content);
    }

    @Override
    public void onEnter() {
        //set the data player
        this.player= DataPlayerFacade.getInstance().getCurrentPlayer();
        //add the game config
        this.content.addComponent(setupGameConfigBox(),1,1);
        //add the game stats
        this.content.addComponent(setupGameStatsBox(),2,1);
        //add players table
        this.content.addComponent(setupLeaderBoard(),0,0,0,2);
    }

    public Component setupGameConfigBox(){
        FormLayout layout=new FormLayout();
        layout.setCaption("Game Configuration");
        layout.addStyleName("center-caption");

        GameTypes config=DataPlaybackEngineStates.gameConfig;

        //show the game description
        Label gameDescription=new Label("<p>"+config.getDescription()+"</p>");
        layout.addComponent(gameDescription);
        gameDescription.setContentMode(ContentMode.HTML);
        gameDescription.setCaption("Game: ");


        //add the player type
        Label playerType=new Label();
        layout.addComponent(playerType);
        playerType.setCaption("Player Type: ");
        playerType.setValue(player.getName());

        //show the attributes
        Label attributes=new Label(config.getInterestedAttributes().toString());
        layout.addComponent(attributes);
        attributes.setContentMode(ContentMode.HTML);
        attributes.setCaption("Attributes: ");

        //matching attribute
        Label matchingAttr=new Label(config.getAttributeToMatch().toString());
        layout.addComponent(matchingAttr);
        matchingAttr.setContentMode(ContentMode.HTML);
        matchingAttr.setCaption("Played On: ");

        return layout;

    }

    public Component setupGameStatsBox(){
        FormLayout layout=new FormLayout();
        layout.setCaption("Game Stats");
        layout.addStyleName("center-caption");

        //show game runtime
        final Label runTime=new Label();
        //push the changes
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
//                while(true){
                    TimeZone defaultTz=TimeZone.getDefault();
                    TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
                    SimpleDateFormat sDate = new SimpleDateFormat("HH:mm:ss");
                    runTime.setValue(sDate.format(new Date(player.getGameRuntime())));
                    TimeZone.setDefault(defaultTz);
                    getUI().push();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                }
            }
        });

        runTime.setCaption("Up Time: ");
        layout.addComponent(runTime);

        //show market turnover
        Label turnover=new Label(Float.toString(player.getMarketTurnover()));
        turnover.setCaption("Market Turnover: ");
        layout.addComponent(turnover);

        //show total trades
        Label totalTrades=new Label(Integer.toString(player.getTotalTrades()));
        totalTrades.setCaption("Total Trades: ");
        layout.addComponent(totalTrades);

        return layout;
    }

    public Table setupLeaderBoard(){
        BeanContainer<String,PlayerInformationBean> beans =
                new BeanContainer<String,PlayerInformationBean>(PlayerInformationBean.class);
        beans.setBeanIdProperty("userName");

        HashMap<String,Portfolio> portfolios=player.getAllPortfolios();
        Iterator it=portfolios.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry=(Map.Entry)it.next();
            Portfolio portfolio =(Portfolio)entry.getValue();

            beans.addBean(new PlayerInformationBean(portfolio,player));
        }


        Table table=new Table("Player Information",beans);
        table.setCaption(null);
        //restrict the maximum number of viewable entries to 5
        table.setPageLength(5);

        //set the column order
        table.setVisibleColumns(new Object[]{"userName", "cashBalance", "totalExpense", "totalStocks",
                "equityPosition"});
        table.setColumnHeader("userName","Player");
        table.setColumnHeader("cashBalance","Cash Balance");
        table.setColumnHeader("totalExpense","Total Expense");
        table.setColumnHeader("totalStocks","Total Stocks");
        table.setColumnHeader("equityPosition","Equity Position");

        return table;
    }
}
