package org.investovator.ui.main;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.GlobalView;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.UIConstants;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
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
