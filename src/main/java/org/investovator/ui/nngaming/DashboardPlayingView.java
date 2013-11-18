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

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.investovator.ann.nngaming.MarketEventReceiver;
import org.investovator.ann.nngaming.NNGamingFacade;
import org.investovator.ann.nngaming.events.AddBidEvent;
import org.investovator.ann.nngaming.events.DayChangedEvent;
import org.investovator.ui.nngaming.beans.OrderBean;
import org.investovator.ui.nngaming.utils.GameDataHelper;
import org.investovator.ui.nngaming.utils.PlayableStockManager;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class DashboardPlayingView extends DashboardPanel implements Observer{


    //Layout Components
    VerticalLayout content;

    Table orderBookSell;
    Table orderBookBuy;
    Component currentPriceChart;
    QuoteUI quoteUI;

    private MarketEventReceiver marketEventReceiver;
    private NNGamingFacade nnGamingFacade;
    private PlayableStockManager playableStockManager;

    private ArrayList<String> playableStocks;
    private GameDataHelper gameDataHelper;
    private ArrayList<ArrayList<OrderBean>> stockBeanListBuy;
    private ArrayList<ArrayList<OrderBean>> stockBeanListSell;
    private int currentDay;

    boolean simulationRunning = false;

    public DashboardPlayingView() {

        createUI();

        gameDataHelper = GameDataHelper.getInstance();
        nnGamingFacade = NNGamingFacade.getInstance();
        playableStockManager = PlayableStockManager.getInstance();
        playableStocks = playableStockManager.getStockList();

        stockBeanListBuy = gameDataHelper.getStockBeanListBuy();
        stockBeanListSell = gameDataHelper.getStockBeanListSell();
        currentDay = gameDataHelper.getCurrentDay();

        marketEventReceiver = MarketEventReceiver.getInstance();
        marketEventReceiver.addObserver(this);

    }


    private void createUI(){

        //Setup Layout
        content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.setSpacing(true);

        HorizontalLayout row1 = new HorizontalLayout();
        HorizontalLayout row2 = new HorizontalLayout();

        row1.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        row2.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        row1.setWidth("100%");
        row2.setWidth("100%");

        row1.setHeight("60%");
        row2.setHeight("35%");

        content.addComponent(row1);
        content.addComponent(row2);

        content.setExpandRatio(row1, 55);
        content.setExpandRatio(row2, 45);

        HorizontalLayout orderBookLayout = new HorizontalLayout();

        orderBookSell = getSellSideTable();
        orderBookBuy = getBuySideTable();
        currentPriceChart = getChart();
        quoteUI = new QuoteUI();

        orderBookLayout.addComponent(orderBookSell);
        orderBookLayout.addComponent(orderBookBuy);

        row1.addComponent(currentPriceChart);
        row2.addComponent(orderBookLayout);
        row2.addComponent(quoteUI);

        orderBookLayout.addStyleName("center-caption");
        quoteUI.addStyleName("center-caption");
        currentPriceChart.addStyleName("center-caption");

        content.setSizeFull();

        this.setSizeFull();

        this.setContent(content);

    }

    private void updateTable(){


        final Container beansBuy = orderBookBuy.getContainerDataSource();
        final Container beansSell = orderBookSell.getContainerDataSource();

        if(!(beansBuy.size() == 0)){
             beansBuy.removeAllItems();
        }
        if(!(beansSell.size() == 0)){
            beansSell.removeAllItems();
        }

        updateStockOrders();    // updates sell & buy orders of each stock

        for(int i = 0; i < stockBeanListBuy.get(0).size();i++){

            beansBuy.addItem(stockBeanListBuy.get(0).get(i));

        }

        for(int i = 0; i < stockBeanListSell.get(0).size();i++){

            beansSell.addItem(stockBeanListSell.get(0).get(i));

        }

        if (orderBookBuy.isConnectorEnabled()) {
            getSession().lock();
            try {

                getUI().access(new Runnable() {
                    @Override
                    public void run() {

                        orderBookBuy.setContainerDataSource(beansBuy);
                        orderBookBuy.setSortContainerPropertyId("orderValue");
                        orderBookBuy.setSortAscending(false);
                        orderBookBuy.sort();
                        getUI().push();
                    }
                });

            } finally {
                getSession().unlock();
            }
        }

        if (orderBookSell.isConnectorEnabled()) {
            getSession().lock();
            try {

                getUI().access(new Runnable() {
                    @Override
                    public void run() {

                        orderBookSell.setContainerDataSource(beansSell);
                        orderBookSell.setSortContainerPropertyId("orderValue");
                        orderBookSell.setSortAscending(true);
                        orderBookSell.sort();
                        getUI().push();
                    }
                });

            } finally {
                getSession().unlock();
            }
        }

    }

    private void updateStockOrders(){

        ArrayList<OrderBean> beanListBuy;
        ArrayList<OrderBean> beanListSell;
        int stockCount = playableStocks.size();
        ArrayList<Float> values;
        Random randomGenerator = new Random();


        for(int i = 0; i < stockCount; i++)
        {

            int buyOrderCount = randomGenerator.nextInt(3) + 1;
            int sellOrderCount = randomGenerator.nextInt(3) + 1;

            values = nnGamingFacade.getGeneratedOrders(buyOrderCount,sellOrderCount,playableStocks.get(i),currentDay);


            beanListBuy = getBuyOrderList(playableStocks.get(i), buyOrderCount, values);

            stockBeanListBuy.add(beanListBuy);

            gameDataHelper.setStockBeanListBuy(stockBeanListBuy);

            beanListSell = getSellOrderList(playableStocks.get(i), buyOrderCount, sellOrderCount, values);

            stockBeanListSell.add(beanListSell);

            gameDataHelper.setStockBeanListSell(stockBeanListSell);
        }
    }

    private ArrayList<OrderBean> getBuyOrderList(String stockID, int buyOrderCount,
                                                 ArrayList<Float> values){

        ArrayList<OrderBean> beanListBuy = new ArrayList<>();
        Random randomGenerator = new Random();
        stockBeanListBuy = gameDataHelper.getStockBeanListBuy();

        for(int i = 0; i < buyOrderCount; i++){

            beanListBuy.add(new OrderBean(values.get(i),randomGenerator.nextInt(199) + 1));

        }

        if(!stockBeanListBuy.isEmpty()){

            ArrayList<OrderBean> temp = stockBeanListBuy.get(playableStocks.indexOf(stockID));

            for(int j = 0; j < beanListBuy.size(); j++){

                temp.add(beanListBuy.get(j));

            }

            return temp;
        }

        return beanListBuy;
    }

    private ArrayList<OrderBean> getSellOrderList(String stockID, int buyOrderCount, int sellOrderCount,
                                                  ArrayList<Float> values){

        ArrayList<OrderBean> beanListSell = new ArrayList<>();
        Random randomGenerator = new Random();
        stockBeanListSell = gameDataHelper.getStockBeanListSell();

        for(int i = buyOrderCount; i < (buyOrderCount+sellOrderCount); i++){

            beanListSell.add(new OrderBean(values.get(i), randomGenerator.nextInt(199) + 1));

        }

        if(!stockBeanListSell.isEmpty()){

            ArrayList<OrderBean> temp = stockBeanListSell.get(playableStocks.indexOf(stockID));

            for(int j = 0; j < beanListSell.size(); j++){

                temp.add(beanListSell.get(j));

            }
            return temp;
        }

        return beanListSell;
    }

    private Table getSellSideTable() {

        BeanItemContainer<OrderBean> beans = new BeanItemContainer<OrderBean>(OrderBean.class);
        Table orderBookSell = new Table("Sell Order Side", beans);

        orderBookSell.setHeight("100%");
        orderBookSell.setWidth("45%");
        orderBookSell.setSelectable(true);
        orderBookSell.setImmediate(true);

        return orderBookSell;
    }

    private Table getBuySideTable() {

        BeanItemContainer<OrderBean> beans = new BeanItemContainer<OrderBean>(OrderBean.class);

        Table orderBookBuy = new Table("Buy Order Side",beans);

        orderBookBuy.setHeight("100%");
        orderBookBuy.setWidth("45%");
        orderBookBuy.setSelectable(true);
        orderBookBuy.setImmediate(true);

        return orderBookBuy;
    }

    protected Component getChart() {

        BasicChart chart = new BasicChart();
        return chart.getChart();

    }

    @Override
    public void onEnter() {

        quoteUI.update();


        simulationRunning = true;

    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof DayChangedEvent){
            System.out.println("DayChanged");

            stockBeanListBuy.clear();
            gameDataHelper.setStockBeanListBuy(stockBeanListBuy);

            stockBeanListSell.clear();
            gameDataHelper.setStockBeanListSell(stockBeanListSell);

            currentDay++;
            gameDataHelper.setCurrentDay(currentDay);
            System.out.println("TableUpdated");
        }

        if(arg instanceof AddBidEvent){

            updateTable();

        }
    }
}
