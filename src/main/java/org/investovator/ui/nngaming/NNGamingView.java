package org.investovator.ui.nngaming;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.GlobalView;
import org.investovator.ui.authentication.Authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/7/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class NNGamingView extends GlobalView {
    Authenticator authenticator;

    public NNGamingView(){

    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
        Notification.show("Welcome to NN Gaming Engine");

    }
}
