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

package org.investovator.ui.nngaming.eventobjects;

import org.investovator.ui.nngaming.beans.OrderBean;

import java.util.ArrayList;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class TableData {

    ArrayList<ArrayList<OrderBean>> stockBeanListBuy;
    ArrayList<ArrayList<OrderBean>> stockBeanListSell;
    ArrayList<String> stockList;

    public TableData(ArrayList<ArrayList<OrderBean>> stockBeanListBuy, ArrayList<ArrayList<OrderBean>> stockBeanListSell
    , ArrayList<String> stockList){

        this.stockBeanListBuy = stockBeanListBuy;
        this.stockBeanListSell = stockBeanListSell;
        this.stockList = stockList;

    }

    public ArrayList<ArrayList<OrderBean>> getStockBeanListBuy() {
        return stockBeanListBuy;
    }

    public void setStockBeanListBuy(ArrayList<ArrayList<OrderBean>> stockBeanListBuy) {
        this.stockBeanListBuy = stockBeanListBuy;
    }

    public ArrayList<ArrayList<OrderBean>> getStockBeanListSell() {
        return stockBeanListSell;
    }

    public void setStockBeanListSell(ArrayList<ArrayList<OrderBean>> stockBeanListSell) {
        this.stockBeanListSell = stockBeanListSell;
    }

    public ArrayList<String> getStockList() {
        return stockList;
    }

    public void setStockList(ArrayList<String> stockList) {
        this.stockList = stockList;
    }
}
