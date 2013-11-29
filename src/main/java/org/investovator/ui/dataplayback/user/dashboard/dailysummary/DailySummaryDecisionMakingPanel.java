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


package org.investovator.ui.dataplayback.user.dashboard.dailysummary;

import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataPlayerCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.core.commons.events.GameEvent;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryDecisionMakingPanel extends DecisionMakerPanel {

    Random rn = new Random(new Date().getTime());


    @Override
    protected String[] getStocksList() {
        return DataPlaybackEngineStates.playingSymbols;
    }

    @Override
    public HashMap<String, HashMap<Date, Double>> getInitialData() {

        HashMap<String,HashMap<Date, Double>> map=new HashMap<>();

        for(String stock: DataPlaybackEngineStates.playingSymbols){
            Date date=DataPlaybackEngineStates.gameStartDate;
            //generate bogus data
            HashMap<Date, Double> dataMap=new HashMap<>();
            for(int i=1;i<40;i++){
                Date newDate= DateUtils.decrementTimeByDays(i, date);
                dataMap.put(newDate,genRandomNumber());
                date=newDate;
            }
            map.put(stock,dataMap);

        }

        return map;

    }

    @Override
    public void eventOccurred(GameEvent event) {
        if(event instanceof StockUpdateEvent){
            StockUpdateEvent update=(StockUpdateEvent)event;
            String stock=update.getStockId();
            Date date=update.getTime();
            //todo - dynamically set the attribute
            //if this contains data
            if(update.getData()!=null && !update.getData().isEmpty()){

                Double price= Double.valueOf(update.getData().get(TradingDataAttribute.CLOSING_PRICE));
                updateStockTable(stock,date,price);
            }

        }

    }

    @Override
    public void addAsAListener() {
        try {
            String userName= Authenticator.getInstance().getCurrentUser();


            GameController controller= GameControllerImpl.getInstance();
            GetDataPlayerCommand command=new GetDataPlayerCommand();
            controller.runCommand(Session.getCurrentGameInstance(),command );
            DataPlayer player=(DailySummaryDataPLayer)command.getPlayer();

            //join the game if the user has not already done so
//            if(!player.hasUserJoined(this.userName)){
            player.setObserver(this);
//            }
        }
        catch (CommandExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CommandSettingsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private Double genRandomNumber(){
        int maximum=1000;
        int minimum=100;
        int n = maximum - minimum + 1;
        int k = rn.nextInt() % n;
        if(k<0) k=k*-1;
        float randomNum =  minimum + k;

        return Double.valueOf(randomNum);
    }



}
