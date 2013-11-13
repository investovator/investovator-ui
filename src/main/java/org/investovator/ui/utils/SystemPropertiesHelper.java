package org.investovator.ui.utils;

import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.data.api.*;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.core.data.rssexplorer.RSSManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;
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

        System.setProperty("org.investovator.core.data.cassandra.url", "localhost:9160" );
        System.setProperty("org.investovator.core.data.cassandra.username", "admin" );
        System.setProperty("org.investovator.core.data.cassandra.password", "admin" );


        System.setProperty("org.investovator.core.data.mysql.url", "localhost:3306" );
        System.setProperty("org.investovator.core.data.mysql.username", "root" );
        System.setProperty("org.investovator.core.data.mysql.password", "root" );
        System.setProperty("org.investovator.core.data.mysql.database", "investovator_data" );
        System.setProperty("org.investovator.core.data.mysql.driverclassname", "com.mysql.jdbc.Driver" );


        String realPath = context.getRealPath("/WEB-INF/configuration/investovator.sql");
        System.setProperty("org.investovator.core.data.mysql.ddlscriptpath", realPath );
        System.out.println("SQL Path : " + realPath);

        //clearOldData();

        //UnComment this
        addTestConfig();

    }


    private void clearOldData(){
        CompanyStockTransactionsData historyData = new CompanyStockTransactionsDataImpl();
        try {
            historyData.clearAllTradingData(CompanyStockTransactionsData.DataType.OHLC);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void addTestConfig(){

        CompanyStockTransactionsData historyData = new CompanyStockTransactionsDataImpl();
        try {
            String filePath = context.getRealPath("/WEB-INF/testdata/sampath.csv");
            historyData.importCSV(CompanyStockTransactionsData.DataType.OHLC,"SAMP","MM/dd/yyyy",new File(filePath));
            new CompanyDataImpl().addCompanyData("SAMP", "Sampath Bank", 100000);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }



        try {
            UserData userData =  new UserDataImpl();
            userData.addToWatchList("testUser1", "SAMP");

            Portfolio portfolio = new PortfolioImpl("testUser1", 1000000, 0);
            userData.updateUserPortfolio("testUser1",portfolio);


        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
