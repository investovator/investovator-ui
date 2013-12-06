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


package org.investovator.ui.dataplayback.util;

import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataPlayerCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.dashboard.dataplayback.BasicGameOverWindow;

import java.util.ArrayList;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlaybackGameOverWindow extends BasicGameOverWindow{

    public DataPlaybackGameOverWindow(String username, Authenticator.UserType userType) {
        super(username, userType);
    }

    @Override
    public Portfolio[] getPortfolios() {
//        ArrayList<Portfolio> portfolios=new ArrayList<>();

        GameController controller= GameControllerImpl.getInstance();
        GetDataPlayerCommand command=new GetDataPlayerCommand();
        try {
            controller.runCommand(Session.getCurrentGameInstance(),command );
            DataPlayer player=command.getPlayer();
            ArrayList<Portfolio> portfolios=player.getAllPortfolios();
            return  portfolios.toArray(new Portfolio[portfolios.size()]);
        } catch (CommandSettingsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CommandExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//        DataPlayer player =DataPlayerFacade.getInstance().getCurrentPlayer();
//        Iterator itr=player.getAllPortfolios().entrySet().iterator();
//        while (itr.hasNext()){
//            Portfolio portfolio= (Portfolio) ((Map.Entry)itr.next()).getValue();
//            portfolios.add(portfolio);
//        }

//        return portfolios.toArray(new Portfolio[portfolios.size()]);
//        return (Portfolio[])player.getAllPortfolios().entrySet().toArray();

        return null;
    }

    @Override
    public Portfolio getMyPortfolio(String username) {

        GameController controller= GameControllerImpl.getInstance();
        GetDataPlayerCommand command=new GetDataPlayerCommand();

//        DataPlayer player =DataPlayerFacade.getInstance().getCurrentPlayer();
        try {
            controller.runCommand(Session.getCurrentGameInstance(),command );
            DataPlayer player=command.getPlayer();
            return player.getMyPortfolio(username);
        } catch (UserJoinException e) {
            e.printStackTrace();
        } catch (CommandExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CommandSettingsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
