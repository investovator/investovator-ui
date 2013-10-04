package org.investovator;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.investovator.Authentication.AgentGamingView;
import org.investovator.Authentication.DataPlaybackView;
import org.investovator.Authentication.LoginView;
import org.investovator.Authentication.MainGamingView;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "org.investovator.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    private Navigator navigator;
    private static final String MAINVIEW = "main";
    private static final String AGENTVIEW = "agentView";
    private static final String DATAPLAYVIEW = "playbackView";

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("investovator | Gaming Framework");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        // Create and register the views
        navigator.addView("", new LoginView());
        navigator.addView(MAINVIEW, new MainGamingView());
        navigator.addView(AGENTVIEW, new AgentGamingView());
        navigator.addView(DATAPLAYVIEW, new DataPlaybackView());

        //test JASA code
        Main main=new Main();
        String[] v=new String[1];
        v[0]="d";
        //main.main(v);
    }

}
