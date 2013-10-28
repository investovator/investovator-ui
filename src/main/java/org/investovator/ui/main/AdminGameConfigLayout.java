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
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.admin.wizard.NewDataPlaybackGameWizard;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.UIConstants;
import org.vaadin.easyuploads.MultiFileUpload;

import java.io.File;

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

        final GameModes gameMode= GameControllerFacade.getInstance().getCurrentGameMode();
        final GameStates gameState=GameControllerFacade.getInstance().getCurrentGameState();

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
                //if there is no game running
                if(gameState==GameStates.NEW){
                    //todo-navigate to the game creation wizard
//                    getUI().getNavigator().navigateTo(UIConstants.DATA_PLAYBACK_ADMIN_DASH);
                    startDailySummaryAddGameWizard();

                }
                //if there is a game running
                else if(gameState==GameStates.RUNNING && gameMode==GameModes.PAYBACK_ENG){
                    if(DataPlaybackEngineStates.currentGameMode== PlayerTypes.REAL_TIME_DATA_PLAYER){
                        try {
                            //if the game is multi player
                            if(DataPlayerFacade.getInstance().getRealTimeDataPlayer().isMultiplayer()){
                                //todo - load the summary view --  from DATA_PLAYBACK_ADMIN_DASH?
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
                    else if(DataPlaybackEngineStates.currentGameMode==PlayerTypes.DAILY_SUMMARY_PLAYER){
                        try {
                            //if this is a multiplayer game
                            if (DataPlayerFacade.getInstance().getDailySummaryDataPLayer().isMultiplayer()){
                                getUI().getNavigator().navigateTo(UIConstants.DATA_PLAYBACK_ADMIN_DASH);
                            }
                            else{
                                //loads single player daily summary data playback view
                                getUI().getNavigator().navigateTo(UIConstants.DATAPLAY_USR_DASH);
                            }
                        } catch (PlayerStateException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
//                    getUI().getNavigator().navigateTo(UIConstants.DATAPLAY_USR_DASH);

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



        //Configuration Popup
        Button setConfig = new Button("Config");

        setConfig.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().addWindow(new UploadWindow("Select Data Files"));
            }
        });

        this.addComponent(setConfig);
    }

    private class UploadWindow extends Window {

        private UploadWindow(String caption) {
            super(caption);

            final VerticalLayout content = new VerticalLayout();

            MultiFileUpload uploder = new MultiFileUpload() {
                @Override
                protected void handleFile(File file, String s, String s2, long l) {

                    CompanyStockTransactionsData historyData = new CompanyStockTransactionsDataImpl();
                    try {
                        historyData.importCSV(CompanyStockTransactionsData.DataType.OHLC,"SAMP","MM/dd/yyyy",file);
                        new CompanyDataImpl().addCompanyData("SAMP", "Sampath Bank", 100000);
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }

                    synchronized (UI.getCurrent()){
                        content.addComponent(new Label(s));
                    }

                }

                @Override
                protected boolean supportsFileDrops(){
                    return false;
                }


            };

            uploder.setUploadButtonCaption("Choose Files");
            uploder.setImmediate(true);

            content.addComponent(uploder);

            this.setContent(content);
            this.setResizable(false);
            this.center();
        }
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

        // Add it to the root component
        UI.getCurrent().addWindow(subWindow);
    }
}
