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

import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.dashboard.dataplayback.BasicGameOverWindow;

import java.util.ArrayList;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class NNGameOverWindow extends BasicGameOverWindow{

    private UserData userData;
    private ArrayList<String> users;

    public NNGameOverWindow(String username) {
        super(username);
        try {
            userData = new UserDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        users = new ArrayList<>();
    }

    @Override
    public Portfolio[] getPortfolios() {

        try {
            users = userData.getGameInstanceUsers(Session.getCurrentGameInstance());

            int usersCount = users.size();

            Portfolio[] userPortfolios = new Portfolio[usersCount];

            for(int i = 0; i < usersCount; i++){
                userPortfolios[i] = userData.getUserPortfolio(Session.getCurrentGameInstance(),users.get(i));
            }

            return userPortfolios;

        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Portfolio getMyPortfolio(String username) {

        try {
            return userData.getUserPortfolio(Session.getCurrentGameInstance(),Session.getCurrentUser());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
