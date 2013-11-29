/*
 * investovator, Stock Market Gaming Framework
 *     Copyright (C) 2013  investovator
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.nngaming;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class AnalysisChart extends Chart {

    private String stockID;

    public AnalysisChart(String stockID){

        this.stockID = stockID;
        initChart();

    }

    public void initChart() {

        setHeight(65,Unit.MM);
        setWidth("100%");

        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.SPLINE);

        configuration.getxAxis().setType(AxisType.DATETIME);

        Axis yAxis = configuration.getyAxis();
        yAxis.setMin(0);
        yAxis.setTitle(new Title("Price (LKR)"));
        yAxis.getTitle().setVerticalAlign(VerticalAlign.HIGH);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        plotOptions.setAnimation(true);
        configuration.setPlotOptions(plotOptions);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.HORIZONTAL);
        legend.setHorizontalAlign(HorizontalAlign.CENTER);
        legend.setVerticalAlign(VerticalAlign.BOTTOM);
        legend.setBorderWidth(1);

        drawChart(configuration);

        getConfiguration().disableCredits();
        getConfiguration().getTitle().setText(stockID + " - Stock Prices");

    }

    public void addSeries(final String seriesName, ArrayList<ArrayList<Object>> graphData, int dataIndex){

        final DataSeries dataSeries = new DataSeries();
        int graphDataSize = graphData.get(0).size();

        for(int i = 0; i < graphDataSize; i++){

            dataSeries.add(new DataSeriesItem((Date)graphData.get(0).get(i), (Number) graphData.get(dataIndex).get(i)));

        }

        dataSeries.setName(seriesName);

        getConfiguration().addSeries(dataSeries);

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                getUI().push();
            }
        });

    }

}
