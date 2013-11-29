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

import java.util.ArrayList;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class GameDataHelper {

    private static GameDataHelper instance;
    private ArrayList<String> stockList;
    private ArrayList<String> analysisParameters;

    private GameDataHelper(){

        if(stockList == null){
            stockList = new ArrayList<>();
        }
        if(analysisParameters == null){
            analysisParameters = new ArrayList<>();
        }

    }

    public static GameDataHelper getInstance() {
        if(instance == null){
            synchronized(GameDataHelper.class){
                if(instance == null)
                    instance = new GameDataHelper();
            }
        }
        return instance;
    }

    public void setStocks(ArrayList<String> stockIDs){

        this.stockList = stockIDs;

    }

    public ArrayList<String> getStockList(){

        return stockList;

    }

    public ArrayList<String> getAnalysisParameters() {
        return analysisParameters;
    }

    public void setAnalysisParameters(ArrayList<String> analysisParameters) {
        this.analysisParameters = analysisParameters;
    }
}
