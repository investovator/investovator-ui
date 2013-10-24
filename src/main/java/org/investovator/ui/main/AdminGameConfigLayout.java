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


package org.investovator.ui.main;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.UIConstants;

/**
 * @author: ishan
 * @version: ${Revision}
 *
 * Responsible for setting up the Admin UI depending up on the games available
 * and their state
 */
public class AdminGameConfigLayout extends VerticalLayout {

    Authenticator authenticator;

    /**
     * If no argument is passed, that means a game is not running/configured at the moment
     */
    public AdminGameConfigLayout() {
        authenticator=Authenticator.getInstance();
        init();
    }

    private void init(){
        Button agentGames = new Button("Agent Gaming Engine", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(authenticator.isLoggedIn()){
                    getUI().getNavigator().navigateTo(UIConstants.AGENTVIEW);
                }
                else {
                    getUI().getNavigator().navigateTo("");
                }
            }
        });
        Button dataPlayback = new Button("Data Playback Engine", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(authenticator.isLoggedIn()){
                    getUI().getNavigator().navigateTo(UIConstants.DATAPLAYVIEW);
                }
                else {
                    getUI().getNavigator().navigateTo("");
                }
            }
        });
        Button nnGames = new Button("NN Gaming Engine", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(authenticator.isLoggedIn()){
                    getUI().getNavigator().navigateTo(UIConstants.NNVIEW);
                }
                else {
                    getUI().getNavigator().navigateTo("");
                }
            }
        });
        FormLayout layout = new FormLayout(agentGames,dataPlayback,nnGames);
        this.addComponent(layout);
    }
}
