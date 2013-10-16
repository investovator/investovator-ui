package org.investovator.ui.agentgaming.adapters;

import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.exeptions.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class CompanyDataTestImpl implements CompanyData {
    @Override
    public HashMap<String, String> getCompanyIDsNames() throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HashMap<String, Integer> getCompanyIDsTotalShares() throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCompanyName(String symbol) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCompanyNoOfShares(String symbol) throws DataAccessException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getInfo(String infotype, String symbol) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ArrayList<String> getAvailableStockIds() throws DataAccessException {

        ArrayList<String> stocks = new ArrayList<String>();
        stocks.add("GOOG");
        stocks.add("SAMP");
        stocks.add("IBM");

        return stocks;

    }
}
