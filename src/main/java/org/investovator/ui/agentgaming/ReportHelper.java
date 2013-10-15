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
import net.sourceforge.jasa.agent.valuation.GeometricBrownianMotionPriceProcess;
import org.investovator.jasa.api.JASAFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.investovator.jasa.api.JASAFacade;
import org.investovator.jasa.api.MarketFacade;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class ReportHelper {

    int lastIndex = 0;
    boolean reportsReady = false;
    HashMap<String,ArrayList<Report>> reports;
    GeometricBrownianMotionPriceProcess googGBM =null;

    private MarketFacade simulationFacade = JASAFacade.getMarketFacade();

    public void viewReport(){

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reports = simulationFacade.getReports();
        if(reports == null) return;

        ArrayList<Report> goog = reports.get("GOOG");



        for (int i = 0; i < goog.size(); i++) {
            Report tmp = goog.get(i);
            if(tmp.getName().equals("GBM")){
                googGBM = (GeometricBrownianMotionPriceProcess) tmp;
            }
        }

    }


    public int getValue(){

//        if(googGBM==null){
//           viewReport();
//           return 0;
//        }
//
//        else{
        return googGBM.getY(0).intValue();
//        }
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

}





