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
import com.vaadin.ui.UI;
import org.investovator.ui.agentgaming.ReportHelper;
import org.investovator.ui.agentgaming.beans.TimeSeriesNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class TimeSeriesChart extends Chart {

    private HashMap<String, DataSeries> series;
    private List<String> stocks;
    private String chartVariable;
    private int dataPoints = 50;

    String getChartVariable() {
        return chartVariable;
    }


    public void addStock(String stockID){
        if(!stocks.contains(stockID)){
            stocks.add(stockID);
        }
    }

    public void setStocks(String[] stockList){
        stocks.clear();
        for(String stock : stockList){
            stocks.add(stock);
        }
    }

    public TimeSeriesChart(String chartVariable) {

        this.chartVariable = chartVariable;

        series = new HashMap<String, DataSeries>();
        stocks = new ArrayList<>();

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
        configuration.disableCredits();

        setSizeUndefined();

    }

    public void update() {

        for (String stock : stocks) {

            if (!series.containsKey(stock)) {
                DataSeries tmp = new DataSeries();
                series.put(stock, tmp);
                getConfiguration().addSeries(tmp);
            }

            final DataSeries dataSeries = series.get(stock);
            dataSeries.setName(stock);

            final ArrayList<TimeSeriesNode> data = ReportHelper.getInstance().getTimeSeriesReport(stock, chartVariable, 50);

            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {

                    dataSeries.clear();

                    for (TimeSeriesNode node : data) {
                        dataSeries.add(new DataSeriesItem(node.getDate(), node.getValue()));
                    }

                    getUI().push();
                }
            });

            drawChart();

        }

    }


}