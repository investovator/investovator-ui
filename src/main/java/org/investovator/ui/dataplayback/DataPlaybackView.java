package org.investovator.ui.dataplayback;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.Authentication.Authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class DataPlaybackView extends VerticalLayout implements View {
    Authenticator authenticator;

    public DataPlaybackView(){
        authenticator = Authenticator.getInstance();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // TODO Auto-generated method stub
        if(!authenticator.isLoggedIn()){
            getUI().getNavigator().navigateTo("");
        }
        else{
            Notification.show("Welcome to Data Playback Engine");
        }
    }

}

