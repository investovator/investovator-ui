package org.investovator.ui.main;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataPlayerCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.ui.main.components.UserGameDetailsView;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class MyGamesPanel extends DashboardPanel {

    VerticalLayout content;

    VerticalLayout myGamesLayout;

    public MyGamesPanel(){
        content = new VerticalLayout();
        content.setSizeFull();
        setLayout();
        this.setContent(content);
    }

    public void setLayout(){

        myGamesLayout = new VerticalLayout();
        myGamesLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        myGamesLayout.setSpacing(true);
        myGamesLayout.setWidth("100%");
        myGamesLayout.setCaption("My Games");
        myGamesLayout.addStyleName("center-caption");

        content.addComponent(myGamesLayout);
    }

    @Override
    public void onEnter() {

        myGamesLayout.removeAllComponents();

        GameController controller = GameControllerImpl.getInstance();

        for(String instance : controller.getGameInstances()){
            //for data playback engine, don't add the admin only games
            if(controller.getGameMode(instance)== GameModes.PAYBACK_ENG){
                //todo - switch to isMultiplayer command
                GetDataPlayerCommand command=new GetDataPlayerCommand();
                try {
                    controller.runCommand(instance,command);
                    if(!command.getPlayer().isMultiplayer()){
                        continue;
                    }
                } catch (CommandSettingsException e) {
                    e.printStackTrace();
                    continue;
                } catch (CommandExecutionException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            UserGameDetailsView view = new UserGameDetailsView(instance, controller);
            myGamesLayout.addComponent(view);
            view.update();
        }

    }
}
