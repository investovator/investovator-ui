package org.investovator.ui.main;

import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.MyVaadinUI;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.enums.GameStates;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.main.components.AdminGameDetailView;
import org.investovator.ui.main.components.UserGameDetailsView;
import org.investovator.ui.utils.UIConstants;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author : ishan
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
@SuppressWarnings("serial")

public class MainGamingView extends DashboardPanel {

    VerticalLayout content = new VerticalLayout();
    VerticalLayout instancesView = new VerticalLayout();

    public MainGamingView() {
        content.setSizeFull();
        setWindowContent();
        this.setContent(content);
    }

    /**
     * Loads the necessary content to the main view up on the conditions
     */
    private void setWindowContent() {

        instancesView = new VerticalLayout();
        instancesView.setWidth("100%");
        instancesView.setCaption("Deployed Game Instances");
        instancesView.addStyleName("center-caption");

        content.addComponent(new AdminGameConfigLayout());
        content.addComponent(instancesView);

    }


    @Override
    public void onEnter() {

        //if not logged in
        if (!Authenticator.getInstance().isLoggedIn()) {
            ((MyVaadinUI) MyVaadinUI.getCurrent()).getNavigator().navigateTo("");
        }

        instancesView.removeAllComponents();

        GameController controller = GameControllerImpl.getInstance();

        for (String instance : controller.getGameInstances()) {
            AdminGameDetailView view = new AdminGameDetailView(instance, controller);
            instancesView.addComponent(view);
            view.update();
        }
    }
}
