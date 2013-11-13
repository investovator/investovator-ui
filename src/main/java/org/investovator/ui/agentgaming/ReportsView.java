package org.investovator.ui.agentgaming;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import org.investovator.ui.utils.dashboard.DashboardPanel;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class ReportsView extends DashboardPanel {

    GridLayout layout;
    TimeSeriesChart chart;
    TimeSeriesChart chart2;

    public ReportsView() {

        layout = new GridLayout();
        layout.setColumns(2);
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        chart = new TimeSeriesChart("market price");
        layout.addComponent(chart);


        chart2 = new TimeSeriesChart("market spread");
        layout.addComponent(chart2);

        this.setContent(layout);
    }

    @Override
    public void onEnter() {

        chart.update();
        chart2.update();

    }


}


class TimeSeriesChart extends Chart {

    private HashMap<String, DataSeries> series;
    private List<String> stocks;
    private String chartVariable;
    private int dataPoints = 50;

    String getChartVariable() {
        return chartVariable;
    }

    public TimeSeriesChart(String chartVariable) {

        this.chartVariable = chartVariable;

        series = new HashMap<String, DataSeries>();
        stocks = new ArrayList<>();
        stocks.add("SAMP");

        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.LINE);
        configuration.setTitle(chartVariable);


        configuration.getxAxis().setType(AxisType.DATETIME);

        PlotOptionsLine plotOptionsLine = new PlotOptionsLine();
        plotOptionsLine.setMarker(new Marker(false));
        plotOptionsLine.setShadow(false);
        plotOptionsLine.setAnimation(false);
        configuration.setPlotOptions(plotOptionsLine);


        drawChart(configuration);

    }

    public void update() {

        for (String stock : stocks) {

            DataSeries dataSeries = null;

            if (!series.containsKey(stock)) {
                DataSeries tmp = new DataSeries();
                series.put(stock, tmp);
                getConfiguration().addSeries(tmp);
            }

            dataSeries = series.get(stock);

            ArrayList<TimeSeriesNode> data = ReportHelper.getInstance().getTimeSeriesReport("SAMP", chartVariable, 50);

            dataSeries.clear();

            synchronized (UI.getCurrent()) {

                for (TimeSeriesNode node : data) {
                    dataSeries.add(new DataSeriesItem(node.getDate(), node.getValue()));
                }

            }


                /*getConfiguration().getSeries().clear();

                synchronized (UI.getCurrent()){
                    getConfiguration().addSeries(series.values().iterator().next());
                }*/


        }

    }


}