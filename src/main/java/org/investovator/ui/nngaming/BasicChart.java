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
import org.investovator.ann.nngaming.NNGamingFacade;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.ui.nngaming.utils.PlayableStockManager;

import java.util.*;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class BasicChart extends Chart{

    private PlayableStockManager playableStockManager;
    private ArrayList<String> stockList;
    private NNGamingFacade nnGamingFacade;
    private ArrayList<DataSeries> stockDataSeriesList;
    private ArrayList<float[]> predictedValues;
    private ArrayList<Date[]> dateValues;
    private int currentIndex = 0;

    public BasicChart(){

        playableStockManager = PlayableStockManager.getInstance();
        stockList = playableStockManager.getStockList();
        nnGamingFacade = NNGamingFacade.getInstance();
        stockDataSeriesList = new ArrayList<>();

        predictedValues = new ArrayList<>();
        dateValues = new ArrayList<>();



    }


    public String getDescription() {
        return "Stock Price Variation";
    }

    public Chart getChart() {

        Chart chart = new Chart();
        chart.setHeight("300px");
        chart.setWidth("100%");

        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.LINE);
        configuration.getChart().setMarginRight(130);
        configuration.getChart().setMarginBottom(25);

        configuration.getxAxis().setType(AxisType.DATETIME);

        Axis yAxis = configuration.getyAxis();
        yAxis.setMin(-5d);
        yAxis.setTitle(new Title("Price (LKR)"));
        yAxis.getTitle().setVerticalAlign(VerticalAlign.HIGH);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        configuration.setPlotOptions(plotOptions);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setHorizontalAlign(HorizontalAlign.RIGHT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(-10d);
        legend.setY(100d);
        legend.setBorderWidth(0);

        prepareDataSeriesLists();

        for(int i = 0; i < stockList.size(); i++){

            DataSeries dataSeries = stockDataSeriesList.get(i);
            dataSeries.setName(stockList.get(i));
            configuration.addSeries(dataSeries);

        }

        chart.drawChart(configuration);

        chart.getConfiguration().disableCredits();
        chart.getConfiguration().getTitle().setText("Stock Prices");

        return chart;

    }

    public void addPointToChart(){

        if((predictedValues.isEmpty()) && (dateValues.isEmpty())){

            prepareChartData();

        }

        final int stockListSize = stockList.size();

        getUI().access(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < stockListSize; i++){

                    String stock = stockList.get(i);
                    float[] values = predictedValues.get(stockList.indexOf(stock));
                    Date[] dates = dateValues.get(stockList.indexOf(stock));
                    stockDataSeriesList.get(stockList.indexOf(stock)).add(
                            new DataSeriesItem(dates[currentIndex],values[currentIndex]));
                }
                getUI().push();

            }
        });

        currentIndex++;

    }

    private void prepareChartData(){

        int stockListSize = stockList.size();
        float[] stockPrices;
        Date[] dateArray;

        for(int i = 0; i < stockListSize; i++){
            stockPrices = nnGamingFacade.getPredictedPrices(stockList.get(i));
            predictedValues.add(stockPrices);

            int stockPriceLength = stockPrices.length;
            dateArray = new Date[stockPriceLength];            //same length as stock prices array per stock

            Calendar calendar = Calendar.getInstance();

            Date[] dates = nnGamingFacade.getDateRange(stockList.get(i));    //get date range
            calendar.setTime(dates[1]);

            for(int k = 0; k < stockPriceLength; k++){

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                if (dayOfWeek == Calendar.FRIDAY) {
                    calendar.add(Calendar.DATE, 3);
                } else if (dayOfWeek == Calendar.SATURDAY) {
                    calendar.add(Calendar.DATE, 2);
                } else {
                    calendar.add(Calendar.DATE, 1);
                }

                Date nextBusinessDay = calendar.getTime();

                dateArray[k] = nextBusinessDay;

                calendar.setTime(nextBusinessDay);
            }

            dateValues.add(dateArray);

        }
    }

    private void prepareDataSeriesLists(){

        Calendar calendar = Calendar.getInstance();

        ArrayList<TradingDataAttribute> attributes = new ArrayList<>();
        attributes.add(TradingDataAttribute.CLOSING_PRICE);

        for(int i = 0; i < stockList.size(); i++){

            Date[] dates = nnGamingFacade.getDateRange(stockList.get(i));    //get date range
            calendar.setTime(dates[1]);
            calendar.add(Calendar.DATE, -18);

            Date start = calendar.getTime();

            HashMap<Date, String> dataMap = nnGamingFacade.getChartData(start, dates[1], stockList.get(i), attributes);

            Set<Date> dateList = nnGamingFacade.getDates(start, dates[1], stockList.get(i), attributes);

            DataSeries dataSeries = new DataSeries();

            for (Iterator<Date> iterator = dateList.iterator(); iterator.hasNext(); ) {
                Date next = iterator.next();
                dataSeries.add(new DataSeriesItem(next,Float.valueOf(dataMap.get(next))));
            }
            stockDataSeriesList.add(dataSeries);

        }



    }
}
