package org.investovator;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.investovator.ui.agentgaming.AgentDashboard;
import org.investovator.ui.agentgaming.config.AgentGamingView;
import org.investovator.ui.authentication.LoginView;
import org.investovator.ui.dataplayback.admin.dashboard.AdminDashboardLoader;
import org.investovator.ui.dataplayback.user.dashboard.UserDashboardLoader;
import org.investovator.ui.main.MainGamingView;
import org.investovator.ui.nngaming.config.NNGamingView;
import org.investovator.ui.utils.UIConstants;

import javax.servlet.annotation.WebServlet;

@Theme("dashboard")
//@PreserveOnRefresh
@SuppressWarnings("serial")
@Push
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "org.investovator.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    private Navigator navigator;
    private static String userName="userName";


    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("investovator | Gaming Framework");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        // Create and register the views
        navigator.addView("", new LoginView());
        navigator.addView(UIConstants.MAINVIEW, new MainGamingView());
        navigator.addView(UIConstants.AGENTVIEW, new AgentGamingView());
        navigator.addView(UIConstants.DATAPLAY_USR_DASH, new UserDashboardLoader());
        navigator.addView(UIConstants.DATA_PLAYBACK_ADMIN_DASH,new AdminDashboardLoader());
        navigator.addView(UIConstants.NNVIEW, new NNGamingView());
        navigator.addView(UIConstants.AGENT_DASH_VIEW, new AgentDashboard());

        //test JASA code
//        Main main=new Main();
//        String[] v=new String[1];
//        v[0]="d";
//        main.main(v);
    }

    public String getUser() {
        return (String)getSession().getAttribute(userName);
    }

    public void setUser(String user) {
        getSession().setAttribute(userName,user);
    }

//    private String user;





}
