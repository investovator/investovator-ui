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

import com.vaadin.ui.*;
import org.investovator.ui.agentgaming.config.AgentGamingView;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.admin.wizard.NewDataPlaybackGameWizard;
import org.investovator.ui.nngaming.config.NNGamingView;
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

        //final GameModes gameMode= GameControllerFacade.getInstance().getCurrentGameMode();
        //final GameStates gameState=GameControllerFacade.getInstance().getCurrentGameState();

        Button agentGames = new Button("Agent Gaming Engine", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(authenticator.isLoggedIn()){
                    startAgentCreateWizard();
                }
                else {
                    getUI().getNavigator().navigateTo(UIConstants.LOGIN_VIEW);
                }
            }
        });

        Button dataPlayback = new Button("Data Playback Engine", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {

                startDailySummaryAddGameWizard();

                /*//if there is no game running
                if(gameState==GameStates.NEW){
                    startDailySummaryAddGameWizard();

                }
                //if there is a game running
                else if(gameState==GameStates.RUNNING && gameMode==GameModes.PAYBACK_ENG){
                    if(DataPlaybackEngineStates.gameConfig.getPlayerType()== PlayerTypes.REAL_TIME_DATA_PLAYER){
                        try {
                            //if the game is multi player
                            //todo - modify to data player parentnew  (no need to even check the follwoing if conditions it seems)
                            if(DataPlayerFacade.getInstance().getRealTimeDataPlayer().isMultiplayer()){
                                getUI().getNavigator().navigateTo(UIConstants.DATA_PLAYBACK_ADMIN_DASH);

                            }
                            else{
                                //loads single player real time data playback view
                                getUI().getNavigator().navigateTo(UIConstants.DATAPLAY_USR_DASH);
                            }
                        } catch (PlayerStateException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                    else if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.DAILY_SUMMARY_PLAYER){
//                        try {
                            //if this is a multiplayer game
//                            if (DataPlayerFacade.getInstance().getCurrentPlayer().isMultiplayer()){
                                if (DataPlaybackEngineStates.isMultiplayer){

                                    getUI().getNavigator().navigateTo(UIConstants.DATA_PLAYBACK_ADMIN_DASH);
                            }
                            else{
                                //loads single player daily summary data playback view
                                getUI().getNavigator().navigateTo(UIConstants.DATAPLAY_USR_DASH);
                            }
//                        } catch (PlayerStateException e) {
//                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                        }
                    }

                }*/

            }
        });

        Button nnGames = new Button("NN Gaming Engine", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(authenticator.isLoggedIn()){
                    startNNCreateWizard();
                }
                else {
                    getUI().getNavigator().navigateTo(UIConstants.LOGIN_VIEW);
                }
            }
        });
        Label agentGamesLabel=new Label("Agent based game");
        Label agentGamesStatusLabel=new Label("Not Running");
        Label dataPlaybackGamesStatusLabel=new Label("Not Running");
        Label annGamesStatusLabel=new Label("Not Running");
        /*if(gameMode==GameModes.AGENT_GAME && gameState==GameStates.RUNNING){
            agentGamesStatusLabel.setValue("<b> <p style=\"color:red\">Running<b>");
            agentGamesStatusLabel.setContentMode(ContentMode.HTML);
            dataPlaybackGamesStatusLabel.setValue("Not Running");
            annGamesStatusLabel.setValue("Not Running");



        }*/


        Label dataPlaybackGamesLabel=new Label("Data Playback based game");
        /*if(gameMode==GameModes.PAYBACK_ENG && gameState==GameStates.RUNNING){
            dataPlaybackGamesStatusLabel.setValue("<b> <p style=\"color:red\">Running<b>");
            dataPlaybackGamesStatusLabel.setContentMode(ContentMode.HTML);
            annGamesStatusLabel.setValue("Not Running");
            agentGamesStatusLabel.setValue("Not Running");


        }*/


        Label annGamesLabel=new Label("ANN based game");
       /* if(gameMode==GameModes.NN_GAME && gameState==GameStates.RUNNING){
            annGamesStatusLabel.setValue("<b> <p style=\"color:red\">Running<b>");
            annGamesStatusLabel.setContentMode(ContentMode.HTML);
            dataPlaybackGamesStatusLabel.setValue("Not Running");
            agentGamesStatusLabel.setValue("Not Running");



        }*/

        //add a stop button for DPE
        Button stopDpeButton=new Button("Stop DPE",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
               /* if(gameState==GameStates.RUNNING && gameMode==GameModes.PAYBACK_ENG){
                    if(DataPlaybackEngineStates.gameConfig.getPlayerType()== PlayerTypes.REAL_TIME_DATA_PLAYER){
                        try {
                            DataPlaybackGameFacade.getDataPlayerFacade().getRealTimeDataPlayer().stopGame();
                            GameControllerFacade.getInstance().stopGame(GameModes.PAYBACK_ENG);
                        } catch (PlayerStateException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                    else if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.DAILY_SUMMARY_PLAYER){
                        try {
                            DataPlaybackGameFacade.getDataPlayerFacade().getDailySummaryDataPLayer().stopGame();
                            GameControllerFacade.getInstance().stopGame(GameModes.PAYBACK_ENG);
                        } catch (PlayerStateException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }

                }*/


            }
        });

        HorizontalLayout agentLayout = new HorizontalLayout(agentGamesLabel,agentGames,agentGamesStatusLabel);
        agentLayout.setSizeFull();
        HorizontalLayout dataPlaybackLayout;
        //if(GameControllerFacade.getInstance().getCurrentGameState()==GameStates.RUNNING){
        //    dataPlaybackLayout=new HorizontalLayout(dataPlaybackGamesLabel,
        //            dataPlayback,stopDpeButton,dataPlaybackGamesStatusLabel);
        //}
        //else{
            dataPlaybackLayout= new HorizontalLayout(dataPlaybackGamesLabel,
                    dataPlayback,dataPlaybackGamesStatusLabel);
       // }

        dataPlaybackLayout.setSizeFull();
        HorizontalLayout annLayout = new HorizontalLayout(annGamesLabel,nnGames,annGamesStatusLabel);
        annLayout.setSizeFull();


        this.addComponent(agentLayout);
        this.addComponent(dataPlaybackLayout);
        this.addComponent(annLayout);

    }




    private void startDailySummaryAddGameWizard() {
        // Create a sub-window and set the content
        Window subWindow = new Window("Create New Game");
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);

        // Put some components in it
        subContent.addComponent(new NewDataPlaybackGameWizard(subWindow));

        // set window characteristics
        subWindow.center();
        subWindow.setClosable(false);
        subWindow.setDraggable(false);
        subWindow.setResizable(false);
        subWindow.setModal(true);

        subWindow.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent closeEvent) {
                //getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
                getUI().getPage().reload();
            }
        });

        // Add it to the root component
        UI.getCurrent().addWindow(subWindow);
    }


    private void startAgentCreateWizard(){

        // Create a sub-window and set the content
        AgentGamingView subWindow = new AgentGamingView();
        subWindow.update();

        // set window characteristics
        subWindow.setHeight("60%");
        subWindow.setWidth("50%");
        subWindow.center();
        subWindow.setClosable(false);
        subWindow.setDraggable(false);
        subWindow.setResizable(false);
        subWindow.setModal(true);

        subWindow.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent closeEvent) {
                //getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
                getUI().getPage().reload();
            }
        });

        // Add it to the root component
        UI.getCurrent().addWindow(subWindow);

    }


    private void startNNCreateWizard() {
        NNGamingView subWindow = new NNGamingView("Create New Game");

        // set window characteristics
        subWindow.setHeight("50%");
        subWindow.setWidth("40%");
        subWindow.center();
        subWindow.setClosable(false);
        subWindow.setDraggable(false);
        subWindow.setResizable(false);
        subWindow.setModal(true);

        subWindow.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent closeEvent) {
                getUI().getPage().reload();
            }
        });

        // Add it to the root component
        UI.getCurrent().addWindow(subWindow);

    }
}
