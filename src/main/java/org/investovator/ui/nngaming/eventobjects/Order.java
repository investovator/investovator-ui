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

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class Order {

    private String selectedStock;
    private boolean isBuy;
    private float orderPrice;
    private int orderStockCount;
    private String userName;

    public Order(String userName, String selectedStock, boolean isBuy, float orderPrice, int orderStockCount){

        this.selectedStock = selectedStock;
        this.isBuy = isBuy;
        this.orderPrice = orderPrice;
        this.orderStockCount = orderStockCount;
        this.userName = userName;

    }

    public String getSelectedStock() {
        return selectedStock;
    }

    public void setSelectedStock(String selectedStock) {
        this.selectedStock = selectedStock;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public float getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderStockCount() {
        return orderStockCount;
    }

    public void setOrderStockCount(int orderStockCount) {
        this.orderStockCount = orderStockCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
