package org.investovator.ui.main.components;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import org.investovator.controller.GameController;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.ConfigHelper;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.UIConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AdminGameDetailView extends GameDetailsView {


    public AdminGameDetailView(String instanceID, GameController controller) {
        super(instanceID, controller);
        setWidth("90%");
        addStyleName("admin-game-instance");
        setRatios(1,6,2);
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
                getUI().getNavigator().navigateTo(UIConstants.getUserDashboardURL(Authenticator.UserType.ADMIN,mode));
            }
        });

        Button stopGame = new Button("Stop Game");
        stopGame.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                GameModes mode = controller.getGameMode(gameInstance);
                if(mode==null){
                    //TODO:Add notification
                    return;
                }
                controller.stopGame(gameInstance);
                getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
            }
        });


        buttons.add(gotoGame);
        buttons.add(stopGame);

        return buttons;
    }

    @Override
    public Image getImage() {
        FileResource resource = new FileResource(new File(ConfigHelper.getImagePath()+"bullet-icon.png"));
        Image img = new Image(null,resource);
        img.setHeight("30px");
        img.setWidth("30px");
        return img;
    }

    @Override
    public String getName() {
        return  null;
    }
}
