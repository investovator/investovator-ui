package org.investovator.ui.analysis;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.UI;
import org.investovator.ui.agentgaming.beans.TimeSeriesNode;

import java.util.*;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class MultiPlotTimeSeriesChart extends Chart {
    private HashMap<String, DataSeries> series = new HashMap<>();

    public MultiPlotTimeSeriesChart(String reportName){
        this();
        this.getConfiguration().setTitle(reportName);
    }

    public MultiPlotTimeSeriesChart(){
        setHeight("300px");
        setWidth("100%");

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


        for(Map.Entry<Date, Double> entry : data.entrySet()){
            dataSeries.add(new DataSeriesItem(entry.getKey(), entry.getValue()));
        }

        dataSeries.setName(seriesName);
        series.put(seriesName,dataSeries);

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                getConfiguration().addSeries(dataSeries);
                UI.getCurrent().push();
            }
        });

    }

    public void insertDataPoint(final String stockID, final Date date, final Number number){
        getUI().access(new Runnable() {
            @Override
            public void run() {
                 series.get(stockID).add(new DataSeriesItem(date, number),true,true);
                 getUI().push();
            }
        });
    }
}
