package org.investovator.ui.agentgaming;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.GlobalView;
import org.investovator.ui.authentication.Authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class AgentGamingView extends GlobalView {

    Authenticator authenticator;

    public AgentGamingView(){

    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to Agent Gaming Engine");
    }

}

