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

package org.investovator.ui.agentgaming.events;

import net.sourceforge.jabm.event.EventListener;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jasa.agent.TradingAgent;
import net.sourceforge.jasa.event.OrderPlacedEvent;
import net.sourceforge.jasa.event.TransactionExecutedEvent;
import net.sourceforge.jasa.market.Order;
import org.investovator.controller.utils.events.PortfolioChangedEvent;
import org.investovator.core.commons.events.GameEvent;
import org.investovator.core.commons.events.GameEventListener;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.agentsimulation.api.utils.HollowTradingAgent;
import org.investovator.ui.utils.Session;

import java.util.ArrayList;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AgentUIUpdater implements EventListener {

    private ArrayList<GameEventListener> listeners;
    private UserData userData;

    private static AgentUIUpdater instance;

    private AgentUIUpdater() {
        listeners = new ArrayList<GameEventListener>();

        try {
            userData = new UserDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }


    public static AgentUIUpdater getInstance() {
        if (instance == null) {
            synchronized (AgentUIUpdater.class) {
                if (instance == null)
                    instance = new AgentUIUpdater();
            }
        }
        return instance;
    }

    public void addListener(GameEventListener listener) {
        this.listeners.add(listener);
    }

    private void notifyListeners(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.eventOccurred(event);
        }
    }

    @Override
    public void eventOccurred(SimEvent simEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
        updatePortfolio(simEvent);

    }


    private void updatePortfolio(SimEvent simEvent) {

        if (simEvent instanceof TransactionExecutedEvent) {

            TransactionExecutedEvent transactionEvent = (TransactionExecutedEvent) simEvent;

            Order ask = ((TransactionExecutedEvent) simEvent).getAsk();
            Order bid = ((TransactionExecutedEvent) simEvent).getBid();

            TradingAgent buyer = bid.getAgent();
            TradingAgent seller = ask.getAgent();

            if (buyer instanceof HollowTradingAgent) {

                String buyingUser = ((HollowTradingAgent) buyer).getUserName();

                try {
                    Portfolio buyerPortfolio = userData.getUserPortfolio(Session.getCurrentGameInstance(),buyingUser);
                    String stockID = ((TransactionExecutedEvent) simEvent).getAuction().getStockID();
                    buyerPortfolio.boughtShares(stockID, transactionEvent.getQuantity(), (float) transactionEvent.getPrice());

                    userData.updateUserPortfolio(Session.getCurrentGameInstance(),buyingUser, buyerPortfolio);
                    notifyListeners(new PortfolioChangedEvent(buyerPortfolio));

                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }


            if (seller instanceof HollowTradingAgent) {

                String sellingUser = ((HollowTradingAgent) seller).getUserName();

                try {
                    Portfolio sellerPortfolio = userData.getUserPortfolio(Session.getCurrentGameInstance(),sellingUser);
                    String stockID = ((TransactionExecutedEvent) simEvent).getAuction().getStockID();
                    sellerPortfolio.soldShares(stockID, transactionEvent.getQuantity(), (float) transactionEvent.getPrice());

                    userData.updateUserPortfolio(Session.getCurrentGameInstance(),sellingUser, sellerPortfolio);
                    notifyListeners(new PortfolioChangedEvent(sellerPortfolio));

                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }

        }


        if (simEvent instanceof OrderPlacedEvent) {

            Order order = ((OrderPlacedEvent) simEvent).getOrder();
            TradingAgent agent = order.getAgent();

            if (agent instanceof HollowTradingAgent) {

                String user = ((HollowTradingAgent) agent).getUserName();
                Portfolio portfolio = null;

                try {
                    portfolio = userData.getUserPortfolio(Session.getCurrentGameInstance(),user);

                    if (((OrderPlacedEvent) simEvent).getOrder().isBid()) {
                        portfolio.setCashBalance(portfolio.getCashBalance() - order.getPrice()*order.getQuantity());
                        portfolio.setBlockedCash(portfolio.getBlockedCash() + order.getPrice()*order.getQuantity());
                    } else {

                    }

                    userData.updateUserPortfolio(Session.getCurrentGameInstance(),user, portfolio);
                    notifyListeners(new PortfolioChangedEvent(portfolio));

                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
