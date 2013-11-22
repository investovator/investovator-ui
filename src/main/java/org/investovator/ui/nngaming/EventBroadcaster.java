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

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import org.investovator.ann.nngaming.MarketEventReceiver;
import org.investovator.ann.nngaming.NNGamingFacade;
import org.investovator.ann.nngaming.events.AddBidEvent;
import org.investovator.ann.nngaming.events.DayChangedEvent;
import org.investovator.ui.nngaming.beans.OrderBean;
import org.investovator.ui.nngaming.eventinterfaces.BroadcastEvent;
import org.investovator.ui.nngaming.eventobjects.GraphData;
import org.investovator.ui.nngaming.eventobjects.Order;
import org.investovator.ui.nngaming.eventobjects.TableData;
import org.investovator.ui.nngaming.utils.PlayableStockManager;

import java.util.*;
import java.util.EventListener;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class EventBroadcaster implements EventListener,Observer{

    private static EventBroadcaster instance;
    private List<BroadcastEvent> broadcastListeners;
    private NNGamingFacade nnGamingFacade;
    private ArrayList<String> playableStocks;
    private ArrayList<ArrayList<OrderBean>> stockBeanListBuy;
    private ArrayList<ArrayList<OrderBean>> stockBeanListSell;
    private int currentDay;
    private int currentIndex;
    private MarketEventReceiver marketEventReceiver;
    private PlayableStockManager playableStockManager;
    private ArrayList<DataSeries> stockDataSeriesList;

    private EventBroadcaster(){

        broadcastListeners = new ArrayList<>();
        nnGamingFacade = NNGamingFacade.getInstance();

        stockBeanListBuy = new ArrayList<>();
        stockBeanListSell = new ArrayList<>();

        currentDay = 1;
        currentIndex = 0;

        marketEventReceiver = MarketEventReceiver.getInstance();
        marketEventReceiver.addObserver(this);

        playableStockManager = PlayableStockManager.getInstance();

        stockDataSeriesList = new ArrayList<>();
    }

    public static EventBroadcaster getInstance() {
        if(instance == null){
            synchronized(EventBroadcaster.class){
                if(instance == null)
                    instance = new EventBroadcaster();
            }
        }
        return instance;
    }

    public void addListener(BroadcastEvent listener){
        this.broadcastListeners.add(listener);
    }

    private void notifyListeners(Object object){
        for (int i = 0; i < broadcastListeners.size(); i++) {
            broadcastListeners.get(i).onBroadcast(object);
        }
    }

    public void setEvent(Object object){

        if(object instanceof Order) {

            int stockIndex = playableStocks.indexOf(((Order) object).getSelectedStock());

            if(((Order) object).isBuy()){

                if(stockBeanListBuy.size() <= stockIndex){
                    Notification notification = new Notification("Order placing was not successful", Notification.Type.ERROR_MESSAGE);
                    notification.setPosition(Position.BOTTOM_RIGHT);
                    notification.show(Page.getCurrent());
                }

                else{

                    int status = checkExecutableStatus(((Order) object).isBuy(), ((Order) object).getSelectedStock(),
                            ((Order) object).getOrderPrice());

                    if(status == 0){

                        ArrayList<OrderBean> buyBeanList = stockBeanListBuy.get(stockIndex);

                        buyBeanList.add(new OrderBean(((Order) object).getOrderPrice(), ((Order) object).getOrderStockCount()));

                        OrderBean comparator = new OrderBean(new Float(12.50),12);
                        Collections.sort(buyBeanList, comparator);
                        Collections.reverse(buyBeanList);

                        stockBeanListBuy.set(stockIndex, buyBeanList);
                    }

                    else if(status == 1){

                    }

                    else{
                        Notification notification = new Notification("Order placing was not successful", Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.BOTTOM_RIGHT);
                        notification.show(Page.getCurrent());
                    }
                }
            }

            else {

                if(stockBeanListSell.size() <= stockIndex){
                    Notification notification = new Notification("Order placing was not successful", Notification.Type.ERROR_MESSAGE);
                    notification.setPosition(Position.BOTTOM_RIGHT);
                    notification.show(Page.getCurrent());
                }

                else {

                    int status = checkExecutableStatus(((Order) object).isBuy(), ((Order) object).getSelectedStock(),
                            ((Order) object).getOrderPrice());

                    if(status == 0){
                        ArrayList<OrderBean> sellBeanList = stockBeanListSell.get(stockIndex);

                        sellBeanList.add(new OrderBean(((Order) object).getOrderPrice(), ((Order) object).getOrderStockCount()));

                        OrderBean comparator = new OrderBean(new Float(12.50),12);
                        Collections.sort(sellBeanList, comparator);

                        stockBeanListSell.set(stockIndex, sellBeanList);
                    }
                    else if(status == 1){

                    }

                    else{
                        Notification notification = new Notification("Order placing was not successful", Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.BOTTOM_RIGHT);
                        notification.show(Page.getCurrent());
                    }

                }
            }

            notifyListeners(new TableData(stockBeanListBuy, stockBeanListSell, playableStocks));

        }

        if(object instanceof Object){

            notifyListeners(new TableData(stockBeanListBuy, stockBeanListSell, playableStocks));

        }

    }

    public void setStocks(ArrayList<String> stocks){

        this.playableStocks = stocks;

    }

    public ArrayList<String> getStocks(){

        return playableStocks;

    }

    @Override
    public void update(Observable o, Object arg) {
        playableStocks = playableStockManager.getStockList();

        if(arg instanceof DayChangedEvent){
            System.out.println("DayChanged");

            stockBeanListBuy.clear();

            stockBeanListSell.clear();

            notifyListeners(new TableData(stockBeanListBuy,stockBeanListSell,playableStocks));

            notifyListeners(new GraphData(currentIndex));

            currentIndex++;

            currentDay++;


            System.out.println("TableUpdated");
        }

        if(arg instanceof AddBidEvent){

            updateStockOrders();

            notifyListeners(new TableData(stockBeanListBuy, stockBeanListSell, playableStocks));

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

            beanListSell = getSellOrderList(playableStocks.get(i), buyOrderCount, sellOrderCount, values);

            stockBeanListSell.add(beanListSell);

            System.out.println("Working");

            System.out.println(stockBeanListBuy.size()+" "+stockBeanListSell.size());
        }

    }

    private ArrayList<OrderBean> getBuyOrderList(String stockID, int buyOrderCount,
                                                 ArrayList<Float> values){

        ArrayList<OrderBean> beanListBuy = new ArrayList<>();
        Random randomGenerator = new Random();

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

    public ArrayList<DataSeries> getStockDataSeriesList() {
        return stockDataSeriesList;
    }

    public void setStockDataSeriesList(ArrayList<DataSeries> stockDataSeriesList) {
        this.stockDataSeriesList = stockDataSeriesList;
    }

    private int checkExecutableStatus(boolean isBuy, String selectedStock, float orderPrice){

        int status = -1;
        ArrayList<String> stockList = playableStocks;

        if(isBuy){

            if(stockBeanListSell.size() <= stockList.indexOf(selectedStock)){
                status = -1;
            }

            else{
                ArrayList<OrderBean> sellBeanList = stockBeanListSell.get(stockList.indexOf(selectedStock));

                if(sellBeanList.get(0).getOrderValue() <= orderPrice){
                    status = 1;
                }
                else {
                    status = 0;
                }
            }

        }
        else{

            if(stockBeanListBuy.size() <= stockList.indexOf(selectedStock)){
                status = -1;
            }
            else{
                ArrayList<OrderBean> buyBeanList = stockBeanListBuy.get(stockList.indexOf(selectedStock));

                if(buyBeanList.get(0).getOrderValue() >= orderPrice){
                    status = 1;
                }
                else {
                    status = 0;
                }
            }
        }

        return status;
    }

}
