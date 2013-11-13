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

import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.ui.*;
import org.investovator.ann.nngaming.NNGamingFacade;
import org.investovator.ann.nngaming.util.GameTypes;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class DashboardPlayingView extends DashboardPanel {


    //Layout Components
    GridLayout content;
    GridLayout orderBookContent;
    GridLayout footerContent;
    Table orderBookSell;
    Table orderBookBuy;
    Component currentPriceChart;
    QuoteUI quoteUI;
    NNGamingFacade nnGamingFacade;

    boolean simulationRunning = false;

    public DashboardPlayingView() {

        createUI();
        nnGamingFacade = new NNGamingFacade(GameTypes.TRADING_GAME);

    }


    private void createUI(){

        //Setup Layout
        content = new GridLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.setRows(2);
        content.setColumns(1);

        footerContent = new GridLayout();
        footerContent.setRows(1);
        footerContent.setColumns(2);
        footerContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        orderBookContent = new GridLayout();
        orderBookContent.setRows(1);
        orderBookContent.setColumns(2);
        orderBookContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        Button start = new Button("Start");
        Button stop = new Button("Stop");


        start.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                System.out.println("Started");
                simulationRunning = true;
                updateTable();

            }
        });


        stop.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                System.out.println("Terminated");
                simulationRunning = false;
            }
        });


        orderBookSell = getTable(OrderSide.SELL);
        orderBookBuy = getTable(OrderSide.BUY);
        currentPriceChart = getChart();
        quoteUI = new QuoteUI();

        orderBookContent.addComponent(orderBookSell);
        orderBookContent.addComponent(orderBookBuy);

        footerContent.addComponent(orderBookContent);
        footerContent.addComponent(quoteUI);

        VerticalLayout buttons = new VerticalLayout();
        buttons.addComponent(start);
        buttons.addComponent(stop);

        //Adding to main layout
        content.addComponent(currentPriceChart);
        content.addComponent(footerContent);
        content.setComponentAlignment(currentPriceChart,Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(footerContent, Alignment.MIDDLE_CENTER);

        //content.addComponent(buttons);
       // content.setComponentAlignment(buttons,Alignment.MIDDLE_CENTER);

        content.setSizeFull();

        this.setSizeFull();

        this.setContent(content);

    }

    protected Table getTable(OrderSide orderSide) {

        Table table = new Table();

        table.setHeight("100%");
        table.setWidth("45%");
        table.setSelectable(true);
        table.setImmediate(true);

        if(orderSide == OrderSide.SELL){
            table.addContainerProperty("Sell Orders", Float.class, null);
            table.addContainerProperty("Order Count", Integer.class, null);
        }
        else{
            table.addContainerProperty("Buy Orders",  Float.class, null);
            table.addContainerProperty("Order Count", Integer.class, null);
        }


        return table;
    }


        ListSeries series = new ListSeries("Traded Price",3,456,6,6,3,2,4,4);



    protected Component getChart() {

        /*final Chart chart = new Chart();
        chart.setHeight("350px");
        chart.setWidth("90%");
        chart.setCaption("Share Price Summary");

        final Configuration configuration = new Configuration();
        configuration.setTitle("Last Traded Price");

        configuration.getChart().setType(ChartType.LINE);
        configuration.disableCredits();

        //series.addData(2,true,true);

        configuration.setSeries(series);

        chart.drawChart(configuration);


        return chart;*/

        MasterDetailChart chart = new MasterDetailChart();
        return chart.getChart();
    }

    private void updateTable(){

        //todo
        ArrayList<Float> values;
        values = nnGamingFacade.getGeneratedOrders(2,3,"SAMP",1);

        for(int i = 0; i < 3; i++){

            if(i < 2)
            orderBookSell.addItem(new Object[]{values.get(i),new Integer(100)},i) ;
            orderBookBuy.addItem(new Object[]{values.get(i + 2), new Integer(50)},i);

        }

        orderBookBuy.setPageLength(0);
        orderBookSell.setPageLength(0);

        /*NNGamingFacade nnGamingFacade = new NNGamingFacade(GameTypes.TRADING_GAME);
        ArrayList<Float> orders;

        orders = nnGamingFacade.getGeneratedOrders(4,2,"SAMP",4);

        for(int i = 0;i < orders.size();i++){

            System.out.println(orders.get(i));

        }*/



    }

    @Override
    public void onEnter() {



        quoteUI.update();

        //updateTable();  //set system property values and check todo

        simulationRunning = true;

        Collection<String> availableStocks = null;
        try {
            availableStocks = new UserDataImpl().getWatchList(Authenticator.getInstance().getCurrentUser());
            for(String stock : availableStocks){
                //simulationFacade.addListener(stock,watchList);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }


}
