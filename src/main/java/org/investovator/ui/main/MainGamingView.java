package org.investovator.ui.main;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.MyVaadinUI;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.enums.GameStates;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.main.components.AdminGameCreateView;
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

    GameController controller = GameControllerImpl.getInstance();
    VerticalLayout content = new VerticalLayout();

    VerticalLayout agentView;
    VerticalLayout nnView;
    VerticalLayout playbackView;

    public MainGamingView() {
        setSizeFull();
        content.setSizeUndefined();
        content.setWidth("100%");
        content.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setWindowContent();
        this.setContent(content);
    }

    /**
     * Loads the necessary content to the main view up on the conditions
     */
    private void setWindowContent() {

        agentView = new VerticalLayout();
        agentView.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        nnView = new VerticalLayout();
        nnView.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        playbackView = new VerticalLayout();
        playbackView.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        content.addComponent(new AdminGameCreateView(GameModes.AGENT_GAME, controller));
        content.addComponent(agentView);
        content.addComponent(new AdminGameCreateView(GameModes.NN_GAME, controller));
        content.addComponent(nnView);
        content.addComponent(new AdminGameCreateView(GameModes.PAYBACK_ENG, controller));
        content.addComponent(playbackView);

    }


    @Override
    public void onEnter() {

        //if not logged in
        if (!Authenticator.getInstance().isLoggedIn()) {
            ((MyVaadinUI) MyVaadinUI.getCurrent()).getNavigator().navigateTo("");
        }

        GameController controller = GameControllerImpl.getInstance();

        for (String instance : controller.getGameInstances()) {
            AdminGameDetailView view = new AdminGameDetailView(instance, controller);

            switch (controller.getGameMode(instance)){
                case AGENT_GAME:
                    agentView.addComponent(view);
                    break;
                case NN_GAME:
                    nnView.addComponent(view);
                    break;
                case PAYBACK_ENG:
                    playbackView.addComponent(view);
                    break;
            }

            //instancesView.addComponent(view);
            view.update();
        }
    }
}
