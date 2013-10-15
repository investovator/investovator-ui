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

package org.investovator.ui.agentgaming;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class WatchList implements Runnable{

    private List<StockChangedEvent> listners;
    private HashMap<String,StockItemBean> watchList;
    private ReportHelper helper;


    public WatchList(ReportHelper helper) {
        listners = new ArrayList<StockChangedEvent>();
        watchList = new HashMap<String,StockItemBean>();
        this.helper = helper;
    }

    public void addStockChangeListener(StockChangedEvent listener){
        this.listners.add(listener);
    }

    public void addStockBean(String stockId, StockItemBean stock){
        watchList.put(stockId,stock);
    }

    public Iterable<String> getWatchListItems(){
        return watchList.keySet();
    }

    public StockItemBean getStockBean(String stockID){
        return watchList.get(stockID);
    }

    private void notifyListeners(StockItemBean stockChanged){
        for (int i = 0; i < listners.size(); i++) {
            listners.get(i).onStockChange(stockChanged);
        }
    }


    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        throw new NotImplementedException();
    }
}


class StockItemBean{

    private String stockID;
    private float marketPrice;
    private float lastBid;
    private float lastAsk;

    String getStockID() {
        return stockID;
    }

    void setStockID(String stockID) {
        this.stockID = stockID;
    }

    float getMarketPrice() {
        return marketPrice;
    }

    void setMarketPrice(float marketPrice) {
        this.marketPrice = marketPrice;
    }

    float getLastBid() {
        return lastBid;
    }

    void setLastBid(float lastBid) {
        this.lastBid = lastBid;
    }

    float getLastAsk() {
        return lastAsk;
    }

    void setLastAsk(float lastAsk) {
        this.lastAsk = lastAsk;
    }
}
