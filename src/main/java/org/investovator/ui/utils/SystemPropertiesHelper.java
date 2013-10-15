package org.investovator.ui.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Enumeration;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class SystemPropertiesHelper implements
        javax.servlet.ServletContextListener {
    private ServletContext context = null;

    public void contextInitialized(ServletContextEvent event) {
        System.out.println("*************************");
        System.out.println("*************************");
        System.out.println("*************************");
        System.out.println("*************************");

        System.out.println("Working...");
        System.out.println("*************************");
        System.out.println("*************************");
        System.out.println("*************************");
        System.out.println("*************************");


        context = event.getServletContext();
        Enumeration<String> params = context.getInitParameterNames();
        //TODO - check whether this is the correct parameter
        while (params.hasMoreElements()) {
            String param = (String) params.nextElement();
            String value =
                    context.getInitParameter(param);
            String realPath = context.getRealPath(value);
            System.out.println("Setting : " + param + " --> " + realPath);
            System.setProperty(param, realPath);


        }

        System.setProperty("DATA_FOL", context.getRealPath("data"));

        System.setProperty("jabm.config", "/home/amila/config/main.xml");
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
