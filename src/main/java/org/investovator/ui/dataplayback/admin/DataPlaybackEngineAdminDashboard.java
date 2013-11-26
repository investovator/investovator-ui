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
 *//*



package org.investovator.ui.dataplayback.admin;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.events.PlaybackEvent;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.ui.dataplayback.beans.PlayerInformationBean;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.gametype.GameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.text.SimpleDateFormat;
import java.util.*;

*/
/**
 * @author: ishan
 * @version: ${Revision}
 *//*

public class DataPlaybackEngineAdminDashboard extends DashboardPanel implements PlaybackEventListener {

    GridLayout content;
    DataPlayer player;

    //pie chart
    Chart stockPieChart;

    public DataPlaybackEngineAdminDashboard() {
        this.content = new GridLayout(3,2);
        this.setContent(this.content);
    }

    @Override
    public void onEnter() {
        //set the data player
        this.player= DataPlayerFacade.getInstance().getCurrentPlayer();

        //add the UI elements
        addUIElements();

        //set as an observer
        player.setObserver(this);

    }

    private void addUIElements(){
        //add the game config
        VerticalLayout gameConfigLayout=new VerticalLayout();
        gameConfigLayout.addComponent(setupGameConfigBox());
        gameConfigLayout.setMargin(new MarginInfo(true,true,true,true));
//        gameConfigLayout.setCaption("User Portfolio");
//        gameConfigLayout.addStyleName("center-caption");
        this.content.addComponent(gameConfigLayout,1,0);

        //add the game stats
        VerticalLayout gameStatsLayout=new VerticalLayout();
        gameStatsLayout.addComponent(setupGameStatsBox());
        gameStatsLayout.setMargin(new MarginInfo(true,true,true,true));
        this.content.addComponent(gameStatsLayout,2,0);

        //add players table
        VerticalLayout tableLayout=new VerticalLayout();
        Table leaderboard=setupLeaderBoard();
        tableLayout.addComponent(leaderboard);
        tableLayout.setMargin(new MarginInfo(false,true,true,true));
//        tableLayout.setCaption("User Portfolio");
//        tableLayout.addStyleName("center-caption");
        this.content.addComponent(tableLayout,0,0,0,1);

        //add the pie chart
        this.stockPieChart=setupPieChart();
        VerticalLayout pieChartLayout=new VerticalLayout();
        pieChartLayout.addComponent(this.stockPieChart);
        pieChartLayout.setMargin(new MarginInfo(true,true,true,true));
//        pieChartLayout.setCaption("User Portfolio");
//        pieChartLayout.addStyleName("center-caption");
        this.content.addComponent(pieChartLayout,1,1,2,1);
    }

    public Component setupGameConfigBox(){
        FormLayout layout=new FormLayout();
        layout.setCaption("Game Configuration");
        layout.addStyleName("center-caption");

        GameTypes config=DataPlaybackEngineStates.gameConfig;

        //show the game description
        Label gameDescription=new Label(config.getDescription());
        layout.addComponent(gameDescription);
        gameDescription.setContentMode(ContentMode.HTML);
        gameDescription.setWidth(320, Unit.PIXELS);
        gameDescription.setCaption("Game: ");


        //add the player type
        Label playerType=new Label();
        layout.addComponent(playerType);
        playerType.setCaption("Player Type: ");
        playerType.setValue(player.getName());

        //show the attributes
        Label attributes=new Label(config.getInterestedAttributes().toString());
        layout.addComponent(attributes);
        attributes.setContentMode(ContentMode.HTML);
        attributes.setCaption("Attributes: ");

        //matching attribute
        Label matchingAttr=new Label(config.getAttributeToMatch().toString());
        layout.addComponent(matchingAttr);
        matchingAttr.setContentMode(ContentMode.HTML);
        matchingAttr.setCaption("Played On: ");

        return layout;

    }

    public Component setupGameStatsBox(){
        FormLayout layout=new FormLayout();
        layout.setCaption("Game Stats");
        layout.addStyleName("center-caption");

        //show game runtime
        final Label runTime=new Label();
        //push the changes
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
//                while(true){
                    TimeZone defaultTz=TimeZone.getDefault();
                    TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
                    SimpleDateFormat sDate = new SimpleDateFormat("HH:mm:ss");
                    runTime.setValue(sDate.format(new Date(player.getGameRuntime())));
                    TimeZone.setDefault(defaultTz);
                    getUI().push();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                }
            }
        });

        runTime.setCaption("Up Time: ");
        layout.addComponent(runTime);
        //start the time updating thread
        new Thread()
        {
            public void run() {
                //update forever
                while(true){
                    UI.getCurrent().access(new Runnable() {
                        @Override
                        public void run() {
                            TimeZone defaultTz=TimeZone.getDefault();
                            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
                            SimpleDateFormat sDate = new SimpleDateFormat("HH:mm:ss");
                            runTime.setValue(sDate.format(new Date(player.getGameRuntime())));
                            TimeZone.setDefault(defaultTz);
                            getUI().push();
                        }
                    });

                    //wait before updating
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();


        //show market turnover
        Label turnover=new Label(Float.toString(player.getMarketTurnover()));
        turnover.setCaption("Market Turnover: ");
        layout.addComponent(turnover);

        //show total trades
        Label totalTrades=new Label(Integer.toString(player.getTotalTrades()));
        totalTrades.setCaption("Total Trades: ");
        layout.addComponent(totalTrades);

        return layout;
    }

    public Table setupLeaderBoard(){
        BeanContainer<String,PlayerInformationBean> beans =
                new BeanContainer<String,PlayerInformationBean>(PlayerInformationBean.class);
        beans.setBeanIdProperty("userName");

//        HashMap<String,Portfolio> portfolios=player.getAllPortfolios();
//        Iterator it=portfolios.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry entry=(Map.Entry)it.next();
//            Portfolio portfolio =(Portfolio)entry.getValue();
//
//            beans.addBean(new PlayerInformationBean(portfolio,player));
//        }

        ArrayList<Portfolio> portfolios=player.getAllPortfolios();
        for(Portfolio portfolio:portfolios){
            beans.addBean(new PlayerInformationBean(portfolio,player));
        }



        Table table=new Table("Player Information",beans);
        table.setCaption("Users List");
        table.addStyleName("center-caption");
        table.setSelectable(true);
        //restrict the maximum number of viewable entries to 20
        table.setPageLength(20);

        //set the column order
        table.setVisibleColumns(new Object[]{"userName", "cashBalance", "totalExpense", "totalStocks",
                "equityPosition"});
        table.setColumnHeader("userName","Player");
        table.setColumnHeader("cashBalance","Cash Balance");
        table.setColumnHeader("totalExpense","Total Expense");
        table.setColumnHeader("totalStocks","Total Stocks");
        table.setColumnHeader("equityPosition","Equity Position");

        //add a click listener to the table
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
           @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                updatePieChart(itemClickEvent.getItemId().toString());
            }
        });

        return table;
    }

    public Chart setupPieChart(){

        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        //set the number of decimals to be shown to 2
        conf.getTooltip().setValueDecimals(2);

        conf.getTitle().setText(null);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
//        plotOptions.setSize("120%");

        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setColor(new SolidColor(0, 0, 0));
        dataLabels.setConnectorColor(new SolidColor(0, 0, 0));
        dataLabels
                .setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        series.setName("Total valuation: ");
        //if the stock items has been set
//        if(DataPlaybackEngineStates.playingSymbols!=null){
//            for(String stock:DataPlaybackEngineStates.playingSymbols){
//                series.add(new DataSeriesItem(stock, 0));
//            }
//        }
        conf.setSeries(series);

        conf.disableCredits();


        chart.drawChart(conf);
        //turn off animation
        conf.getChart().setAnimation(false);
        chart.setWidth(95,Unit.PERCENTAGE);
        chart.setHeight(100,Unit.MM);



        chart.setCaption("User Portfolio");
        chart.addStyleName("center-caption");

//        chart.setWidth(75,Unit.PERCENTAGE);
//        chart.setHeight(55,Unit.MM);


        return chart;
    }

    public void updatePieChart(String userName){
        Portfolio portfolio= null;
        try {
            portfolio = this.player.getMyPortfolio(userName);
            //since we know that there's only one data series
            final DataSeries dSeries = (DataSeries) stockPieChart.getConfiguration().getSeries().get(0);

            //remove all the items from the series
            dSeries.clear();


            //add the new items
            HashMap<String,HashMap<String,Double>> stockValues=portfolio.getShares();
            Iterator it=stockValues.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry=(Map.Entry)it.next();
                HashMap<String,Double> values =(HashMap<String,Double>)entry.getValue();

                //get stock quantity
                float quantity=values.get(Terms.QNTY).floatValue();
                //get stock price
                float price = values.get(Terms.PRICE).floatValue();

                //add the item to data series
                dSeries.add(new DataSeriesItem(entry.getKey().toString(),quantity*price));

            }


                //update chart caption
            stockPieChart.setCaption("User Portfolio of "+userName);
                stockPieChart.drawChart();

            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    getUI().push();

                }
            });

        } catch (UserJoinException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    @Override
    public void eventOccurred(PlaybackEvent event) {
//        this.content.removeAllComponents();
//
//        addUIElements();

    }
}
*/
