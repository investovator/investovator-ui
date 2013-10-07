package org.investovator.ui.nngaming;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.authentication.Authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/7/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class NNGamingView extends VerticalLayout implements View {
    Authenticator authenticator;

    public NNGamingView(){
        authenticator = Authenticator.getInstance();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!authenticator.isLoggedIn()){
            getUI().getNavigator().navigateTo("");
        }
        else{
            Notification.show("Welcome to NN Gaming Engine");
        }

    }
}
