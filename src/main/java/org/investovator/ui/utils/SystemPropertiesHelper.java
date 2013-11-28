package org.investovator.ui.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.investovator.core.commons.configuration.ConfigLoader;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.data.api.*;
import org.investovator.core.data.exeptions.DataAccessException;

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

        try {
            ConfigLoader.loadProperties(context.getRealPath("/WEB-INF/configuration/database.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        //SQL Config
        String realPath = context.getRealPath("/WEB-INF/configuration/investovator.sql");
        System.setProperty("org.investovator.core.data.mysql.ddlscriptpath", realPath );


        //Game Configuration
        realPath = context.getRealPath("/WEB-INF/configuration/game.properties");
        System.setProperty("game_properties_url", realPath );




//        //UnComment these once
        clearOldData();
        addTestConfig();

    }


    private void clearOldData(){

        try {
            DataStorage storage = new DataStorageImpl();
            storage.resetDataStorage();

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void addTestConfig(){

        CompanyStockTransactionsData historyData = new CompanyStockTransactionsDataImpl();
        try {

            String filePath = context.getRealPath("/WEB-INF/testdata/SAMP.csv");
            historyData.importCSV(CompanyStockTransactionsData.DataType.OHLC,"SAMP","MM/dd/yyyy",new File(filePath));
            new CompanyDataImpl().addCompanyData("SAMP", "Sampath Bank", 100000);


            filePath = context.getRealPath("/WEB-INF/testdata/RCL.csv");
            historyData.importCSV(CompanyStockTransactionsData.DataType.OHLC,"RCL","MM/dd/yyyy",new File(filePath));
            new CompanyDataImpl().addCompanyData("RCL", "Royal Ceramic Lanka", 100000);


            filePath = context.getRealPath("/WEB-INF/testdata/HASU.csv");
            historyData.importCSV(CompanyStockTransactionsData.DataType.OHLC,"HASU","MM/dd/yyyy",new File(filePath));
            new CompanyDataImpl().addCompanyData("HASU", "HNB Assurance", 100000);



            //sampath ticker data
            filePath = context.getRealPath("/WEB-INF/testdata/SAMP_ticker.csv");
            historyData.importCSV(CompanyStockTransactionsData.DataType.TICKER,"SAMP","MM/dd/yyyy HH:mm:ss.SSS",
                    new File(filePath));

            //HASU ticker data
            filePath = context.getRealPath("/WEB-INF/testdata/HASU_ticker.csv");
            historyData.importCSV(CompanyStockTransactionsData.DataType.TICKER,"HASU","MM/dd/yyyy HH:mm:ss.SSS",
                    new File(filePath));

        } catch (DataAccessException e) {
            e.printStackTrace();
        }



        try {
            UserData userData =  new UserDataImpl();
//            userData.addToWatchList("testUser1", "SAMP");
//            userData.addToWatchList("testUser1", "RCL");
//
//            Portfolio portfolio = new PortfolioImpl("testUser1", 1000000, 0);
//            userData.updateUserPortfolio("testUser1",portfolio);


        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
