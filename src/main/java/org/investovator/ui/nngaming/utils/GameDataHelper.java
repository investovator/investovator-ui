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

package org.investovator.ui.nngaming.utils;

import org.investovator.ui.nngaming.beans.OrderBean;

import java.util.ArrayList;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class GameDataHelper {

    private static GameDataHelper instance;
    private ArrayList<ArrayList<OrderBean>> stockBeanListBuy;
    private ArrayList<ArrayList<OrderBean>> stockBeanListSell;
    private int currentDay = 1;

    private GameDataHelper(){

        if(stockBeanListBuy == null){
            stockBeanListBuy = new ArrayList<>();
        }
        if(stockBeanListSell == null){
            stockBeanListSell = new ArrayList<>();
        }

    }

    public static GameDataHelper getInstance() {
        if(instance == null){
            synchronized(GameDataHelper.class){
                if(instance == null)
                    instance = new GameDataHelper();
            }
        }
        return instance;
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

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
