/*
 * investovator, Stock Market Gaming framework
 * Copyright (C) 2013  investovator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.main.components;

import com.vaadin.ui.*;
import org.investovator.controller.GameController;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.ui.agentgaming.config.AgentGamingView;
import org.investovator.ui.dataplayback.admin.wizard.NewDataPlaybackGameWizard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AdminGameCreateView extends GameDetailsView {


    public AdminGameCreateView(GameModes gameMode, GameController controller) {
        super(gameMode, controller);
        setWidth("90%");
        setCaption(getCaption(gameMode));
        addStyleName("center-caption");
        addStyleName("admin-game-create");
    }


    private String getCaption(GameModes mode){

        switch (mode){
            case AGENT_GAME:return "Artificial players based games";
            case NN_GAME:return "Prediction Based Games";
            case PAYBACK_ENG:return "Past data replay games";
        }

        return  null;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        switch (gameMode){
            case AGENT_GAME: return "This game mode allows users to play with set of configured artificial players " +
                    "along with other human players. The admin can configure the behaviour of the game using different " +
                    "agent populations in the configuration window. This game mode allows the user to get full stock " +
                    "market trading experience and test trading strategies with set of intelligent players.";

            case NN_GAME: return "Generic NN based game - description description description description description " +
                    "description description description description description description description description " +
                    "description description description description description description description " +
                    "description description description";

            case PAYBACK_ENG: return "Generic playback engine based game - description description description description " +
                    "description description description description description description description description " +
                    "description description description description description description description description " +
                    "description description description";
        }
        return null;
    }

    @Override
    public List<Button> getButtons() {
        ArrayList<Button> buttons = new ArrayList<>();

        Button createButton = new Button("Create Game");
        createButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                startGame(gameMode);
            }
        });

        buttons.add(createButton);
        return buttons;
    }


    private void startGame(GameModes mode){

        switch (mode){
            case PAYBACK_ENG:
                startDailySummaryAddGameWizard();
                break;
            case AGENT_GAME:
                startAgentCreateWizard();
                break;
            case NN_GAME:
                Notification.show("Add Methods to NN creation");
                break;
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

}
