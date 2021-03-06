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


package org.investovator.ui.dataplayback.beans;

import java.io.Serializable;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class StockNamePriceBean implements Serializable {

    String stockID;
    float price;
    boolean priceIncreased=false;

    public StockNamePriceBean(String stockID, float price) {
        this.stockID = stockID;
        this.price = price;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isPriceIncreased() {
        return priceIncreased;
    }

    public void setPriceIncreased(boolean priceIncreased) {
        this.priceIncreased = priceIncreased;
    }
}
