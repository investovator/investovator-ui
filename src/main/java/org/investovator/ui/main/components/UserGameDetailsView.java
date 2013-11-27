package org.investovator.ui.main.components;

import com.vaadin.ui.Button;
import org.investovator.controller.GameController;
import org.investovator.controller.utils.enums.GameModes;
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
                getUI().getNavigator().navigateTo(UIConstants.getUserDashboardURL(mode));
            }
        });


        return buttons;
    }
}
