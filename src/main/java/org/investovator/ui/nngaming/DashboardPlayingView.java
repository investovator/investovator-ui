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
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.investovator.ann.nngaming.MarketEventReceiver;
import org.investovator.ann.nngaming.NNGamingFacade;
import org.investovator.ann.nngaming.events.AddBidEvent;
import org.investovator.ann.nngaming.events.DayChangedEvent;
import org.investovator.ui.nngaming.beans.OrderBean;
import org.investovator.ui.nngaming.utils.GameDataHelper;
import org.investovator.ui.nngaming.utils.PlayableStockManager;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.*;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class DashboardPlayingView extends DashboardPanel implements Observer, AddOrderEvent{


    //Layout Components
    VerticalLayout content;

    Table orderBookSell;
    Table orderBookBuy;
    BasicChart currentPriceChart;
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

        stockBeanListBuy = gameDataHelper.getStockBeanListBuy();
        stockBeanListSell = gameDataHelper.getStockBeanListSell();
        currentDay = gameDataHelper.getCurrentDay();

        marketEventReceiver = MarketEventReceiver.getInstance();
        marketEventReceiver.addObserver(this);

        quoteUI.addAddOrderListener(this);

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

        currentPriceChart = new BasicChart();
        orderBookSell = getSellSideTable();
        orderBookBuy = getBuySideTable();
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

            int buyOrderCount = (randomGenerator.nextInt(2) + 1);
            int sellOrderCount = (randomGenerator.nextInt(2) + 1);

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

            beanListBuy.add(new OrderBean(values.get(i),(randomGenerator.nextInt(199) + 1)));

        }

        OrderBean comparator = new OrderBean(new Float(12.50),12);
        Collections.sort(beanListBuy, comparator);
        Collections.reverse(beanListBuy);

        if(!(stockBeanListBuy.isEmpty())){


            if(stockBeanListBuy.size() <= playableStocks.indexOf(stockID))
            {
                return beanListBuy;
            }
            else {

                ArrayList<OrderBean> temp = stockBeanListBuy.get(playableStocks.indexOf(stockID));

                for(int j = 0; j < beanListBuy.size(); j++){

                    temp.add(beanListBuy.get(j));

                }

                Collections.sort(temp, comparator);
                Collections.reverse(temp);

                return temp;

            }

        }

        return beanListBuy;
    }

    private ArrayList<OrderBean> getSellOrderList(String stockID, int buyOrderCount, int sellOrderCount,
                                                  ArrayList<Float> values){

        ArrayList<OrderBean> beanListSell = new ArrayList<>();
        Random randomGenerator = new Random();
        stockBeanListSell = gameDataHelper.getStockBeanListSell();

        for(int i = buyOrderCount; i < (buyOrderCount+sellOrderCount); i++){

            beanListSell.add(new OrderBean(values.get(i), (randomGenerator.nextInt(199) + 1)));

        }

        OrderBean comparator = new OrderBean(new Float(12.50),12);
        Collections.sort(beanListSell, comparator);

        if(!(stockBeanListSell.isEmpty())){

            if(stockBeanListSell.size() <= playableStocks.indexOf(stockID))
            {
                return beanListSell;
            }

            else{
                ArrayList<OrderBean> temp = stockBeanListSell.get(playableStocks.indexOf(stockID));

                for(int j = 0; j < beanListSell.size(); j++){

                    temp.add(beanListSell.get(j));

                }

                Collections.sort(temp, comparator);

                return temp;
            }
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

    @Override
    public void onEnter() {

        quoteUI.update();

        simulationRunning = true;

    }

    @Override
    public void update(Observable o, Object arg) {

        playableStockManager = PlayableStockManager.getInstance();
        playableStocks = playableStockManager.getStockList();

        if(arg instanceof DayChangedEvent){
            System.out.println("DayChanged");

            stockBeanListBuy.clear();
            gameDataHelper.setStockBeanListBuy(stockBeanListBuy);

            stockBeanListSell.clear();
            gameDataHelper.setStockBeanListSell(stockBeanListSell);

            currentDay++;
            gameDataHelper.setCurrentDay(currentDay);


            if (currentPriceChart.isConnectorEnabled()) {
                getSession().lock();
                try {

                    currentPriceChart.addPointToChart();

                } finally {
                    getSession().unlock();
                }
            }


            System.out.println("TableUpdated");
        }

        if(arg instanceof AddBidEvent){

            updateTable();


        }
    }

    @Override
    public void onAddOrder(boolean isBuy, String stockID, float orderPrice, int orderStockCount) {

        ArrayList<String> stockList = playableStockManager.getStockList();
        int stockIndex = stockList.indexOf(stockID);

        if(isBuy){
            ArrayList<ArrayList<OrderBean>> buyStockBeanList = gameDataHelper.getStockBeanListBuy();

            if(buyStockBeanList.size() <= stockIndex){
                Notification notification = new Notification("Order placing was not successful", Notification.Type.ERROR_MESSAGE);
                notification.setPosition(Position.BOTTOM_RIGHT);
                notification.show(Page.getCurrent());
            }

            else{

                ArrayList<OrderBean> buyBeanList = buyStockBeanList.get(stockList.indexOf(stockID));

                buyBeanList.add(new OrderBean(orderPrice, orderStockCount));

                OrderBean comparator = new OrderBean(new Float(12.50),12);
                Collections.sort(buyBeanList, comparator);
                Collections.reverse(buyBeanList);

                buyStockBeanList.set(stockIndex, buyBeanList);

                gameDataHelper.setStockBeanListBuy(buyStockBeanList);

                final Container beansBuy = orderBookBuy.getContainerDataSource();

                if(!(beansBuy.size() == 0)){
                    beansBuy.removeAllItems();
                }

                for(int i = 0; i < buyStockBeanList.get(stockIndex).size();i++){

                    beansBuy.addItem(buyStockBeanList.get(stockIndex).get(i));

                }

                if (orderBookBuy.isConnectorEnabled()) {
                    getSession().lock();
                    try {

                        getUI().access(new Runnable() {
                        @Override
                        public void run() {

                            orderBookBuy.setContainerDataSource(beansBuy);
                            getUI().push();
                        }
                    });

                    } finally {
                    getSession().unlock();
                }
            }
            }
        }

        else {
            ArrayList<ArrayList<OrderBean>> sellStockBeanList = gameDataHelper.getStockBeanListSell();

            if(sellStockBeanList.size() <= stockIndex){
                Notification notification = new Notification("Order placing was not successful", Notification.Type.ERROR_MESSAGE);
                notification.setPosition(Position.BOTTOM_RIGHT);
                notification.show(Page.getCurrent());
            }

            else {

                ArrayList<OrderBean> sellBeanList = sellStockBeanList.get(stockList.indexOf(stockID));

                sellBeanList.add(new OrderBean(orderPrice, orderStockCount));

                OrderBean comparator = new OrderBean(new Float(12.50),12);
                Collections.sort(sellBeanList, comparator);

                sellStockBeanList.set(stockIndex, sellBeanList);

                gameDataHelper.setStockBeanListSell(sellStockBeanList);

                final Container beansSell = orderBookSell.getContainerDataSource();

                if(!(beansSell.size() == 0)){
                    beansSell.removeAllItems();
                }

                for(int i = 0; i < sellStockBeanList.get(stockIndex).size();i++){

                    beansSell.addItem(sellStockBeanList.get(stockIndex).get(i));

                }

                if (orderBookSell.isConnectorEnabled()) {
                    getSession().lock();
                    try {

                        getUI().access(new Runnable() {
                        @Override
                        public void run() {

                                orderBookSell.setContainerDataSource(beansSell);
                                getUI().push();
                            }
                        });

                } finally {
                    getSession().unlock();
                }
            }
        }
        }

    }
}
