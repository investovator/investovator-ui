package org.investovator.ui.main;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
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
            UserGameDetailsView view = new UserGameDetailsView(instance, controller);
            myGamesLayout.addComponent(view);
            view.update();
        }

    }
}
