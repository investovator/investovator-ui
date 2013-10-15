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

import java.io.Serializable;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class StockItemBean implements Serializable {

    private String stockID;
    private float marketPrice;
    private float lastBid;
    private float lastAsk;

    public StockItemBean() {
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public float getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(float marketPrice) {
        this.marketPrice = marketPrice;
    }

    public float getLastBid() {
        return lastBid;
    }

    public void setLastBid(float lastBid) {
        this.lastBid = lastBid;
    }

    public float getLastAsk() {
        return lastAsk;
    }

    public void setLastAsk(float lastAsk) {
        this.lastAsk = lastAsk;
    }
}
