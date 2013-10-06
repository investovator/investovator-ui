package org.investovator.ui.main;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.authentication.Authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")

public class MainGamingView extends VerticalLayout implements View {
    Navigator navigator;
    Authenticator authenticator;

    public MainGamingView(){
        init();
        authenticator = Authenticator.getInstance();
    }
    private void init(){
        Button agentGames = new Button("Agent Gaming Engine", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // TODO Auto-generated method stub
                if(authenticator.isLoggedIn()){
                    getUI().getNavigator().navigateTo("agentView");
                }
                else {
                    getUI().getNavigator().navigateTo("");
                }
            }
        });
        Button dataPlayback = new Button("Data Playback Engine", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // TODO Auto-generated method stub
                if(authenticator.isLoggedIn()){
                    getUI().getNavigator().navigateTo("playbackView");
                }
                else {
                    getUI().getNavigator().navigateTo("");
                }
            }
        });
        FormLayout layout = new FormLayout(agentGames,dataPlayback);
        addComponent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!authenticator.isLoggedIn()){
            getUI().getNavigator().navigateTo("");
        }
        else{
            Notification.show("Welcome to Main Gaming Engine");
        }
    }

}
