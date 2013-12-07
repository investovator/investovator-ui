package org.investovator.ui.analysis;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.UI;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        configuration.getChart().setType(ChartType.LINE);
        configuration.disableCredits();
        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getChart().setZoomType(ZoomType.XY);

        drawChart(configuration);
    }


    public void addSeries(final String seriesName, final HashMap<Date, Double> data){

        if(series.containsKey(seriesName)) return;

            final DataSeries dataSeries = new DataSeries();


        for(Map.Entry<Date, Double> entry : data.entrySet()){
            dataSeries.add(new DataSeriesItem(entry.getKey(), entry.getValue()));
        }

        dataSeries.setName(seriesName);

        getConfiguration().addSeries(dataSeries);
        series.put(seriesName,dataSeries);

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                getUI().push();
            }
        });

    }

    public void insertDataPoint(final String seriesName, final Date date, final Number number){
        getUI().access(new Runnable() {
            @Override
            public void run() {
                 series.get(seriesName).add(new DataSeriesItem(date, number),true,true);
                 getUI().push();
            }
        });
    }
}
