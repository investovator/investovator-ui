package org.investovator.ui.agentgaming.adapters;

import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class UserInfoTestImpl implements UserData {

    @Override
    public void addUser(String username, String firstname, String lastname, String emailaddress, String password) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUserPassword(String username) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HashMap<String, String> getUserDetails(String username) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Portfolio getUserPortfolio(String username) throws DataAccessException {
        //return null;  //To change body of implemented methods use File | Settings | File Templates.

        Portfolio testUser1 = new PortfolioImpl("testUser1",100000);

        if(username.equals("testUser1")) return testUser1;

        return null;
    }


    ArrayList<String> testUser1WatchList = new ArrayList<String>();

    @Override
    public ArrayList<String> getWatchList(String username) {
        return testUser1WatchList;
    }
}
