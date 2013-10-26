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

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.controller.GameControllerFacade;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.enums.GameStates;
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

        GameModes gameMode= GameControllerFacade.getInstance().getCurrentGameMode();
        GameStates gameState=GameControllerFacade.getInstance().getCurrentGameState();

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
        Label agentGamesLabel=new Label("Agent based game");
        Label agentGamesStatusLabel=new Label("Not Running");
        Label dataPlaybackGamesStatusLabel=new Label("Not Running");
        Label annGamesStatusLabel=new Label("Not Running");
        if(gameMode==GameModes.AGENT_GAME && gameState==GameStates.RUNNING){
            agentGamesStatusLabel.setValue("<b> <p style=\"color:red\">Running<b>");
            agentGamesStatusLabel.setContentMode(ContentMode.HTML);
            dataPlaybackGamesStatusLabel.setValue("Not Running");
            annGamesStatusLabel.setValue("Not Running");



        }


        Label dataPlaybackGamesLabel=new Label("Data Playback based game");
        if(gameMode==GameModes.PAYBACK_ENG && gameState==GameStates.RUNNING){
            dataPlaybackGamesStatusLabel.setValue("<b> <p style=\"color:red\">Running<b>");
            dataPlaybackGamesStatusLabel.setContentMode(ContentMode.HTML);
            annGamesStatusLabel.setValue("Not Running");
            agentGamesStatusLabel.setValue("Not Running");


        }


        Label annGamesLabel=new Label("ANN based game");
        if(gameMode==GameModes.NN_GAME && gameState==GameStates.RUNNING){
            annGamesStatusLabel.setValue("<b> <p style=\"color:red\">Running<b>");
            annGamesStatusLabel.setContentMode(ContentMode.HTML);
            dataPlaybackGamesStatusLabel.setValue("Not Running");
            agentGamesStatusLabel.setValue("Not Running");



        }

        HorizontalLayout agentLayout = new HorizontalLayout(agentGamesLabel,agentGames,agentGamesStatusLabel);
        agentLayout.setSizeFull();
        HorizontalLayout dataPlaybackLayout = new HorizontalLayout(dataPlaybackGamesLabel,
                dataPlayback,dataPlaybackGamesStatusLabel);
        dataPlaybackLayout.setSizeFull();
        HorizontalLayout annLayout = new HorizontalLayout(annGamesLabel,nnGames,annGamesStatusLabel);
        annLayout.setSizeFull();


        this.addComponent(agentLayout);
        this.addComponent(dataPlaybackLayout);
        this.addComponent(annLayout);
    }
}