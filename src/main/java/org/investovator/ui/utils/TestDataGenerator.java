package org.investovator.ui.utils;

import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class TestDataGenerator {


    public static void registerUser(String user, String instance, double funds){
        try {
            UserData userData =  new UserDataImpl();
            userData.addUserToGameInstance(instance,user);
            Portfolio portfolio = new PortfolioImpl(user, funds, 0);
            userData.updateUserPortfolio(instance,user,portfolio);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public static void createTestData(String instance, String user, String[] stocks){

        try {
            UserData userData =  new UserDataImpl();

            userData.addUserToGameInstance(instance,user);

            for( String stock : stocks  ){
                userData.addToWatchList(instance,user, stock);
            }

            Portfolio portfolio = new PortfolioImpl(user, 1000000, 0);
            userData.updateUserPortfolio(instance,user,portfolio);


        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }

}
