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

import com.vaadin.ui.Button;
import org.investovator.controller.GameController;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.UIConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class UserGameDetailsView extends GameDetailsView {

    public UserGameDetailsView(String instanceID, GameController controller) {
        super(instanceID, controller);
        addStyleName("user-mygame-view");
        setRatios(1,6,2);
        setWidth("90%");

    }

    @Override
    public List<Button> getButtons() {
        ArrayList<Button> buttons = new ArrayList<>();

        Button gotoGame = new Button("Go to Game");
        gotoGame.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                GameModes mode = controller.getGameMode(gameInstance);
                if(mode==null){
                    //TODO:Add notification
                   return;
                }
                Session.setCurrentGameInstance(gameInstance);
                getUI().getNavigator().navigateTo(UIConstants.getUserDashboardURL(Authenticator.UserType.ORDINARY,
                        mode));
            }
        });

        buttons.add(gotoGame);

        return buttons;
    }
}
