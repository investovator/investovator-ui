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
public class QuantityChart extends Chart {

    private PlayableStockManager playableStockManager;
    private EventBroadcaster eventBroadcaster;
    private ArrayList<String> stockList;
    private NNGamingFacade nnGamingFacade;
    private ArrayList<DataSeries> stockDataSeriesList;
    private ArrayList<float[]> predictedValues;
    private ArrayList<Date[]> dateValues;

    public QuantityChart(){

        nnGamingFacade = NNGamingFacade.getInstance();

        stockDataSeriesList = new ArrayList<>();

        eventBroadcaster = EventBroadcaster.getInstance();

        predictedValues = new ArrayList<>();
        dateValues = new ArrayList<>();

        playableStockManager = PlayableStockManager.getInstance();

        stockList = playableStockManager.getStockList();

        if(!(stockList.isEmpty())){
            initChart();
        }

        if(eventBroadcaster.getStockDataSeriesList().isEmpty()){
            eventBroadcaster.setStockDataSeriesList(stockDataSeriesList);
        }
    }

    public String getDescription() {
        return "Stock Trades Quantity Variation";
    }

    private void initChart() {

        setHeight(45,Unit.MM);
        setWidth("100%");

        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.COLUMN);
        configuration.setTitle("Stock Trades Quantity");

        configuration.getxAxis().setType(AxisType.DATETIME);

        Axis yAxis = configuration.getyAxis();
        yAxis.setMin(0);
        yAxis.setTitle(new Title("Trades Quantity"));
        yAxis.getTitle().setVerticalAlign(VerticalAlign.HIGH);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.HORIZONTAL);
        legend.setHorizontalAlign(HorizontalAlign.CENTER);
        legend.setVerticalAlign(VerticalAlign.BOTTOM);
        legend.setBorderWidth(1);

        Tooltip tooltip = configuration.getTooltip();
        tooltip.setFormatter(" +': '+ this.y");
        configuration.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);

        prepareDataSeriesLists();


        for(int i = 0; i < stockList.size(); i++){

            DataSeries dataSeries = stockDataSeriesList.get(i);
            dataSeries.setName(stockList.get(i));
            configuration.addSeries(dataSeries);

        }

        drawChart(configuration);

        getConfiguration().disableCredits();
        getConfiguration().getTitle().setText("Stock Trades Quantity");
    }

    public void addPointToChart(int currentIndex){

        if((predictedValues.isEmpty()) && (dateValues.isEmpty())){

            prepareChartData();

        }

        final int stockListSize = stockList.size();

        for(int i = 0; i < stockListSize; i++){

            String stock = stockList.get(i);

            float[] values = predictedValues.get(stockList.indexOf(stock));
            Date[] dates = dateValues.get(stockList.indexOf(stock));
            DataSeriesItem item =  stockDataSeriesList.get(stockList.indexOf(stock)).get(0);
            stockDataSeriesList.get(stockList.indexOf(stockList.get(i))).remove(item);
            stockDataSeriesList.get(stockList.indexOf(stock)).add(
                    new DataSeriesItem(dates[currentIndex], values[currentIndex]));

            getUI().access(new Runnable() {
                @Override
                public void run() {

                    getUI().push();

                }
            });

        }

    }

    private void prepareChartData(){

        int stockListSize = stockList.size();
        float[] stockPrices;
        Date[] dateArray;

        for(int i = 0; i < stockListSize; i++){
            stockPrices = nnGamingFacade.getPredictedPrices(stockList.get(i), TradingDataAttribute.TRADES);
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
        attributes.add(TradingDataAttribute.TRADES);

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
