package org.investovator.ui.main;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import org.investovator.ui.GlobalView;
import org.investovator.ui.utils.UIConstants;

/**
 *
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 *
 */
@SuppressWarnings("serial")

public class MainGamingView extends GlobalView{

    public MainGamingView(){
        init();
    }

    private void init(){
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
                if(authenticator.isLoggedIn()){
                    getUI().getNavigator().navigateTo(UIConstants.DATAPLAYVIEW);
                }
                else {
                    getUI().getNavigator().navigateTo("");
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
        FormLayout layout = new FormLayout(agentGames,dataPlayback,nnGames);
        addComponent(layout);
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to Main Gaming Engine");
    }


}
