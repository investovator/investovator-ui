/*
 * investovator, Stock Market Gaming framework
 * Copyright (C) 2013  investovator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.agentgaming;

import net.sourceforge.jabm.report.Report;
import net.sourceforge.jabm.report.SimEventReport;
import net.sourceforge.jasa.agent.valuation.GeometricBrownianMotionPriceProcess;
import net.sourceforge.jasa.report.CurrentPriceReportVariables;
import net.sourceforge.jasa.report.timeseries.CurrentPriceReportTimeseriesVariables;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.agentsimulation.api.JASAFacade;

import java.util.*;

import org.investovator.agentsimulation.api.JASAFacade;
import org.investovator.agentsimulation.api.MarketFacade;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class ReportHelper {

    int lastIndex = 0;
    boolean reportsReady = false;
    HashMap<String,ArrayList<Report>> reports;

    HashMap<String,CurrentPriceReportVariables> currentPriceReports = new HashMap<String, CurrentPriceReportVariables>();

    CompanyData companyData;


    private MarketFacade simulationFacade = JASAFacade.getMarketFacade();
    private static ReportHelper instance;


    private String GBM;


    public static ReportHelper getInstance(){
        if(instance == null) instance=new ReportHelper();
        return instance;
    }



    public ReportHelper() {
        //Add Current Time Reports

    }


    public void initReports(){

        try {
            companyData = new CompanyDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        reports = simulationFacade.getReports();

        try {
            Collection<String> stocks =  companyData.getAvailableStockIds();
            for(String stock : stocks){
                currentPriceReports.put(stock, getCurrentPriceReport(stock));
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        reportsReady = true;

    }


    private CurrentPriceReportVariables getCurrentPriceReport(String stockID){

        ArrayList<Report> allReports = reports.get(stockID);
        CurrentPriceReportVariables currentPriceReport = null;

        for (int i = 0; i < allReports.size(); i++) {
            Report tmp = allReports.get(i);
            if(tmp.getName().equals("current")){
                currentPriceReport = (CurrentPriceReportVariables) tmp;
            }
        }

        return currentPriceReport;
    }


    private HashMap<String,CurrentPriceReportTimeseriesVariables> getTimeSeriesReports(String stockID){
        ArrayList<Report> allReports = reports.get(stockID);
        HashMap<String,CurrentPriceReportTimeseriesVariables> reports = new HashMap<String, CurrentPriceReportTimeseriesVariables>();

        for (int i = 0; i < allReports.size(); i++) {
            Report tmp = allReports.get(i);

            if(tmp instanceof CurrentPriceReportTimeseriesVariables){
                reports.put(tmp.getName(),(CurrentPriceReportTimeseriesVariables)tmp);
            }
        }
        return reports;
    }



    public int getValue(){

//        if(googGBM==null){
//           viewReport();
//           return 0;
//        }
//
//        else{
       // return googGBM.getY(0).intValue();
//        }
        return 0;
    }


       /* public int[] getValues(){

        List<Integer> values = new ArrayList<Integer>();

        if(googGBM==null){
            viewReport();
            int[] tmp = {};
            return tmp;
        }

        else{

            int count = lastIndex;

            while(true){

                googGBM.get
                Number num = googGBM.getY(count);
                if(num==null) break;

                values.add(num.intValue());
                lastIndex++;

            }
        }

        int[] result = new int[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i);
        }

        return result;
    }*/



    public float getCurrentPrice(String stockID){

        CurrentPriceReportVariables report = currentPriceReports.get(stockID);
        return report.getY(0).floatValue();

    }


    public StockItemBean getStockUpdates(String stockID){

       return  null;
    }


    /**
     *
     * @param stockId symbol of the stock
     * @param report report type
     * @param maxRecords number of records to return. if maxRecords is 0 all records are returned.
     * @return
     */
    public ArrayList<TimeSeriesNode> getTimeSeriesReport(String stockId, String report, int maxRecords){

        ArrayList<TimeSeriesNode> data = new ArrayList<TimeSeriesNode>();

        CurrentPriceReportTimeseriesVariables reportVar =  getTimeSeriesReports(stockId).get(report);

        for (int index = 0; index < reportVar.getTimeseriesVariableBindings().get(reportVar.getName() + ".t").size(); index++) {
            TimeSeriesNode tmp = new TimeSeriesNode();
            tmp.setDate(getDate(index));
            tmp.setValue((double)reportVar.getTimeseriesVariableBindings().get(reportVar.getName()+".price").get(index));
            data.add(tmp);
        }

        return data;
    }


    //TODO:Should get from config
    static Date startDate = new Date();


    //TODO:Should goto JASA
    private Date getDate(int tickCount){
        return new Date(startDate.getTime() + tickCount);
    }


}





