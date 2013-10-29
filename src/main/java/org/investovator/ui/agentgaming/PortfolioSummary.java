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

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.sourceforge.jabm.event.EventListener;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jasa.event.TransactionExecutedEvent;
import org.investovator.controller.utils.events.GameEvent;
import org.investovator.controller.utils.events.GameEventListener;
import org.investovator.controller.utils.events.PortfolioChangedEvent;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.authentication.Authenticator;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class PortfolioSummary extends VerticalLayout implements GameEventListener{

    //External Data
    UserData userData;

    //Layout Components
    Label accountBalance;
    Label blockedAmount;

    public PortfolioSummary() {
        setupUI();
    }

    public void setupUI(){

        accountBalance=new Label();
        accountBalance.setCaption("Cash Balance");

        blockedAmount = new Label();
        blockedAmount.setCaption("Blocked Amount");




        this.setCaption("Portfolio Summary");
        this.addComponent(accountBalance);
        this.addComponent(blockedAmount);
    }


    public void update(){

        if(userData == null) try {
            userData = new UserDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        try {
            String currentUser = Authenticator.getInstance().getCurrentUser();
            accountBalance.setValue(Double.toString(userData.getUserPortfolio(currentUser).getCashBalance()));
            blockedAmount.setValue(Double.toString(userData.getUserPortfolio(currentUser).getBlockedCash()));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }


    public void updatePortfolio(Portfolio portfolio){

        if (this.isConnectorEnabled()) {
            getSession().lock();
            try {
                accountBalance.setValue(Double.toString(portfolio.getCashBalance()));
                blockedAmount.setValue(Double.toString(portfolio.getBlockedCash()));
            } finally {
                getSession().unlock();
            }
        }
    }

    @Override
    public void eventOccurred(GameEvent event) {
        if (event instanceof PortfolioChangedEvent){
            updatePortfolio(((PortfolioChangedEvent) event).getPortfolio());
        }
    }
}
