package org.investovator.ui.analysis;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import org.investovator.ui.agentgaming.beans.TimeSeriesNode;

import java.util.*;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class MultiPlotTimeSeriesChart extends Chart {
    private static final int numberOfPoints = 100;
    private HashMap<String, DataSeries> series = new HashMap<>();

    public MultiPlotTimeSeriesChart(String reportName){
        this();
        this.getConfiguration().setTitle(reportName);
    }

    public MultiPlotTimeSeriesChart(){
        setHeight("300px");
        setWidth("90%");
        setCaption("Watchlist Summary");

        final Configuration configuration = new Configuration();
        configuration.setTitle("Last Traded Price");

        configuration.getChart().setType(ChartType.SPLINE);
        configuration.disableCredits();
        configuration.getxAxis().setType(AxisType.DATETIME);

        drawChart(configuration);
    }


    public void addSeries(String seriesName, HashMap<Date, Double> data){

        if(series.containsKey(seriesName)) return;

        final DataSeries dataSeries = new DataSeries();

        //Show last "numberOfPoints" number of data points
        int startIndex = data.size() <= numberOfPoints ? 0 : ( data.size() - 1) - numberOfPoints;

        for(Map.Entry<Date, Double> entry : data.entrySet()){
            dataSeries.add(new DataSeriesItem(entry.getKey(), entry.getValue()));
        }

       /* for(int count = startIndex; count<data.size(); count++){
            dataSeries.add(new DataSeriesItem(data.get(count).getDate(), data.get(count).getValue()));
        }*/

        dataSeries.setName(seriesName);
        series.put(seriesName,dataSeries);

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
