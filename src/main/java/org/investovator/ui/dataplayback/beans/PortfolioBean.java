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

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class PortfolioBean {
    String stockID;
    double AverageCost;
    int numOfStocks;
    double totCost;

    public PortfolioBean(String stockID, double averageCost, int numOfStocks) {
        this.stockID = stockID;
        AverageCost = averageCost;
        this.numOfStocks = numOfStocks;
        this.totCost=averageCost*numOfStocks;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public double getAverageCost() {
        return AverageCost;
    }

    public void setAverageCost(double averageCost) {
        AverageCost = averageCost;
    }

    public int getNumOfStocks() {
        return numOfStocks;
    }

    public void setNumOfStocks(int numOfStocks) {
        this.numOfStocks = numOfStocks;
    }

    public double getTotCost() {
        return totCost;
    }

    public void setTotCost(double totCost) {
        this.totCost = totCost;
    }
}
