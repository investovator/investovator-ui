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


package org.investovator.ui.utils.dashboard.dataplayback;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.ui.dataplayback.beans.PlayerBasicInformationBean;
import org.investovator.ui.dataplayback.beans.PlayerInformationBean;
import org.investovator.ui.utils.UIConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public abstract class BasicGameOverWindow extends Window {

    String username;

    public BasicGameOverWindow(String username) {
        // set window characteristics
        this.center();
        this.setClosable(false);
        this.setDraggable(false);
        this.setResizable(false);
        this.setModal(true);

        this.username=username;

        setupUI();
    }

    public void setupUI(){
        VerticalLayout content=new VerticalLayout();

        //create the leaderboard
        Table leaderboard=getLeaderboard();


        Portfolio myPortfolio=this.getMyPortfolio(username);
        Label rankLabel=new Label("<H2 align=\"center\">Congratulations "+
                myPortfolio.getUsername()+". You Won..!</H2>");

        //if this player has not won the game
        if(!leaderboard.isFirstId(username)){
            rankLabel.setValue("<H2 align=\"center\">Game Over!</H2>");
        }
        rankLabel.setContentMode(ContentMode.HTML);
        content.addComponent(rankLabel);

        content.addComponent(leaderboard);

        //add the exit game button
        Button exitGame=new Button("Exit Game");
        exitGame.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
                close();
            }
        });

        content.addComponent(exitGame);

        this.setContent(content);


    }

    public Table getLeaderboard(){
        BeanContainer<String,PlayerBasicInformationBean> beans =
                new BeanContainer<String,PlayerBasicInformationBean>(PlayerBasicInformationBean.class);
        beans.setBeanIdProperty("userName");

        Portfolio[] portfolios=this.getPortfolios();

        for(Portfolio portfolio:portfolios){
            beans.addBean(new PlayerBasicInformationBean(portfolio));
        }



        Table table=new Table("Player Information",beans);
        table.setCaption("Leaderboard");
        table.addStyleName("center-caption");
        table.setSelectable(false);

        //set the column order
        table.setVisibleColumns(new Object[]{"userName", "cashBalance", "totalExpense", "totalStocks"});
        table.setColumnHeader("userName","Player");
        table.setColumnHeader("cashBalance","Cash Balance");
        table.setColumnHeader("totalExpense","Total Expense");
        table.setColumnHeader("totalStocks","Total Stocks");

        //sort the table
        Object [] properties={"cashBalance","totalExpense"};
        boolean [] ordering={true,true};
        table.sort(properties,ordering);

        //add row numbering
        table.setRowHeaderMode(Table.RowHeaderMode.INDEX);

        return table;
    }

    /**
     * Should return all the user portfolios in the game
     * @return
     */
    public abstract Portfolio[] getPortfolios();

    /**
     * Should return the current users portfolio
     * @return
     */
    public abstract Portfolio getMyPortfolio(String username);
}
