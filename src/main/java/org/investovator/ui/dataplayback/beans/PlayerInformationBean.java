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

import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.dataplaybackengine.player.DataPlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class PlayerInformationBean {

    private String userName;
    private float cashBalance;
    private float totalExpense;
    private int totalStocks;
    private float equityPosition;

    public PlayerInformationBean(Portfolio portfolio, DataPlayer player) {
        this.userName=portfolio.getUsername();
        this.cashBalance=(float)portfolio.getCashBalance();

        //calculate other variables
        this.totalStocks=0;
        Iterator it=portfolio.getShares().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry=(Map.Entry)it.next();
            HashMap<String, Double> map =(HashMap<String, Double>)entry.getValue();

            //increase the stock count
            totalStocks+=map.get(Terms.QNTY);
            //increase total expense
            totalExpense+=map.get(Terms.QNTY)*map.get(Terms.PRICE);
            //calculate equity position
            equityPosition+=map.get(Terms.QNTY)*player.getStockPrice((String)entry.getKey());
        }



    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(float cashBalance) {
        this.cashBalance = cashBalance;
    }

    public float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(float totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int getTotalStocks() {
        return totalStocks;
    }

    public void setTotalStocks(int totalStocks) {
        this.totalStocks = totalStocks;
    }

    public float getEquityPosition() {
        return equityPosition;
    }

    public void setEquityPosition(float equityPosition) {
        this.equityPosition = equityPosition;
    }
}
