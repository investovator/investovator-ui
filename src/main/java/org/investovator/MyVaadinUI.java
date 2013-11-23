package org.investovator;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.UI;
import org.investovator.ui.agentgaming.config.AgentGamingView;
import org.investovator.ui.agentgaming.user.AgentDashboard;
import org.investovator.ui.authentication.LoginView;
import org.investovator.ui.dataplayback.admin.dashboard.AdminDashboardLoader;
import org.investovator.ui.dataplayback.user.dashboard.UserDashboardLoader;
import org.investovator.ui.main.AgentConfigDashboard;
import org.investovator.ui.nngaming.NNGamingDashBoard;
import org.investovator.ui.nngaming.config.NNGamingView;
import org.investovator.ui.utils.UIConstants;

import javax.servlet.annotation.WebServlet;

@Theme("dashboard")
//@PreserveOnRefresh
@SuppressWarnings("serial")
@Push(PushMode.MANUAL)
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "org.investovator.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    private Navigator navigator;


    /*static{
        try {
            ConfigLoader.loadProperties(ConfigHelper.getDatabaseConfigProperties());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }*/


    @Override
    protected void init(VaadinRequest request) {

        System.out.println(System.getProperty("org.investovator.core.data.cassandra.url"));


        this.setStyleName("main-view");

        getPage().setTitle("investovator | Gaming Framework");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        // Create and register the views
        navigator.addView("", new LoginView());
        navigator.addView(UIConstants.MAINVIEW, new AgentConfigDashboard());
        navigator.addView(UIConstants.DATAPLAY_USR_DASH, new UserDashboardLoader());
        navigator.addView(UIConstants.DATA_PLAYBACK_ADMIN_DASH,new AdminDashboardLoader());
        navigator.addView(UIConstants.NN_DASH_VIEW, new NNGamingDashBoard());
        navigator.addView(UIConstants.AGENT_DASH_VIEW, new AgentDashboard());


        //TODO: Uncaught Error Handling
        getUI().setErrorHandler(new DefaultErrorHandler(){
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                doDefault(event);
            }
        });



        //test JASA code
//        Main main=new Main();
//        String[] v=new String[1];
//        v[0]="d";
//        main.main(v);
    }



}
