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

package org.investovator.ui.agentgaming.user.components;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import org.investovator.ui.agentgaming.beans.TimeSeriesNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class MultiStockChart extends Chart{

    private static final int numberOfPoints = 10;
    private HashMap<String, DataSeries> series = new HashMap<>();

    public MultiStockChart(){
        setHeight("300px");
        setWidth("90%");
        setCaption("Watchlist Summary");

        final Configuration configuration = new Configuration();
        configuration.setTitle("Last Traded Price");

        configuration.getChart().setType(ChartType.SPLINE);
        configuration.disableCredits();
        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getyAxis().setTitle("Price");
        configuration.getxAxis().setTitle("Time");

        drawChart(configuration);
    }


    public void addStock(String stockID, ArrayList<TimeSeriesNode> data){

        if(series.containsKey(stockID)) return;

        final DataSeries dataSeries = new DataSeries();

        //Show last "numberOfPoints" number of data points
        int startIndex = data.size() <= numberOfPoints ? 0 : ( data.size() - 1) - numberOfPoints;

        for(int count = startIndex; count<data.size(); count++){
            dataSeries.add(new DataSeriesItem(data.get(count).getDate(), data.get(count).getValue()));
        }

        dataSeries.setName(stockID);
        series.put(stockID,dataSeries);

        getUI().access(new Runnable() {
            @Override
            public void run() {
                getConfiguration().addSeries(dataSeries);
                getUI().push();
            }
        });

    }

    public void insertDataPoint(final String stockID, final Date date, final Number number){
        getUI().access(new Runnable() {
            @Override
            public void run() {
                if(series.get(stockID).size() < numberOfPoints){
                    series.get(stockID).add(new DataSeriesItem(date, number));
                    getUI().push();
                }else{
                    series.get(stockID).add(new DataSeriesItem(date, number),true,true);
                    getUI().push();
                }
            }
        });
    }

}
