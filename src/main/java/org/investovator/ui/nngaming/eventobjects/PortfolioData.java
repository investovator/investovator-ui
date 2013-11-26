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

import org.investovator.core.commons.utils.Portfolio;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class PortfolioData {

    private Portfolio portfolio;
    private boolean orderExecuted;
    private String userName;

    public PortfolioData(Portfolio portfolio, boolean orderExecuted, String userName){

        this.portfolio = portfolio;
        this.orderExecuted = orderExecuted;
        this.userName = userName;

    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public boolean isOrderExecuted() {
        return orderExecuted;
    }

    public void setOrderExecuted(boolean orderExecuted) {
        this.orderExecuted = orderExecuted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
