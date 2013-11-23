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


package org.investovator.ui.dataplayback.admin.wizard;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.apache.commons.lang.time.DateUtils;
import org.investovator.controller.GameControllerFacade;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.exceptions.GameProgressingException;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.dataplaybackengine.utils.StockUtils;
import org.investovator.ui.dataplayback.gametype.GameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class NewDataPlaybackGameWizard extends Wizard implements WizardProgressListener {

    //window that this wizard is run
    Window window;



    public NewDataPlaybackGameWizard(Window window) {

        this.window = window;

        this.addStep(new FirstStep());
        this.addStep(new SecondStep());
        this.addStep(new ThirdStep());

        //use the same class to listen to the events from the wizard
        this.addListener(this);

    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent wizardStepActivationEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent wizardStepSetChangedEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent wizardCompletedEvent) {
        Notification.show("Complete");

        //initialize the necessary player
        DataPlayerFacade.getInstance().createPlayer(DataPlaybackEngineStates.gameConfig.getPlayerType(),
                DataPlaybackEngineStates.playingSymbols,
                DataPlaybackEngineStates.gameStartDate,
                DataPlaybackEngineStates.gameConfig.getInterestedAttributes(),
                DataPlaybackEngineStates.gameConfig.getAttributeToMatch(),
                DataPlaybackEngineStates.isMultiplayer);

        //start the game now
        if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.REAL_TIME_DATA_PLAYER){
            try {
                DataPlayerFacade.getInstance().getRealTimeDataPlayer().startPlayback(3);
                GameControllerFacade.getInstance().startGame(GameModes.PAYBACK_ENG,null);
            } catch (PlayerStateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (GameProgressingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        else {
            try {
                //if this is a multiplayer game
                if(DataPlaybackEngineStates.isMultiplayer==true){

                    DataPlayerFacade.getInstance().getDailySummaryDataPLayer().startMultiplayerGame(3);
                }
                else{
                    DataPlayerFacade.getInstance().getDailySummaryDataPLayer().startGame();

                }
                GameControllerFacade.getInstance().startGame(GameModes.PAYBACK_ENG,null);

            }  catch (PlayerStateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);

            } catch (GameProgressingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);

            } catch (GameAlreadyStartedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }

        this.window.close();
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent wizardCancelledEvent) {
        Notification.show("Cancelled");
        this.window.close();

    }


    class FirstStep implements WizardStep {

        OptionGroup gameTypes;
        OptionGroup multiplayer;

        FirstStep() {
            gameTypes= new OptionGroup();
            multiplayer=new OptionGroup();
        }

        @Override
        public String getCaption() {
            return "Basic Game Options";
        }

        @Override
        public Component getContent() {
            VerticalLayout content = new VerticalLayout();


            //add the game types

            content.addComponent(gameTypes);
            gameTypes.setMultiSelect(false);
            gameTypes.setHtmlContentAllowed(true);

            for(GameTypes type:GameTypes.values()){
                gameTypes.addItem(type);
                gameTypes.setItemCaption(type,
                        type.getDescription());
            }


            //default item
            gameTypes.select(GameTypes.values()[0]);

            //fire value change events immediately
            gameTypes.setImmediate(true);

            content.addComponent(multiplayer);
            multiplayer.setMultiSelect(true);
            multiplayer.setHtmlContentAllowed(true);
            multiplayer.addItem(1);
            multiplayer.setItemCaption(1,"Let others connect to the game");

            return content;
        }

        @Override
        public boolean onAdvance() {
            //set the selected state
            for(GameTypes type:GameTypes.values()){
                if(gameTypes.getValue()==type){
                    DataPlaybackEngineStates.gameConfig=type;
                    break;
                }
            }

            //set multiplayer or not
            if(((Set)multiplayer.getValue()).contains(1)){
                DataPlaybackEngineStates.isMultiplayer=true;

            }
            else{
                DataPlaybackEngineStates.isMultiplayer=false;
            }



            return true;
        }

        @Override
        public boolean onBack() {
            return true;
        }
    }

    class SecondStep implements WizardStep {

        TwinColSelect selector;

        SecondStep() {
            this.selector=new TwinColSelect();


        }

        @Override
        public String getCaption() {
            return "Select the Securities to play";
        }

        @Override
        public Component getContent() {
            VerticalLayout content = new VerticalLayout();

            content.addComponent(selector);
            content.setComponentAlignment(selector,Alignment.MIDDLE_CENTER);

            selector.setRows(6);
            selector.setWidth(90,Unit.PERCENTAGE);
            selector.setMultiSelect(true);
            selector.setImmediate(true);
            selector.setLeftColumnCaption("Available securities");
            selector.setRightColumnCaption("Selected securities");

            //select a default Item
            selector.select(1);

            try {
                HashMap<String,String> companyList= StockUtils.getStocksList();
                for(String stock:companyList.keySet()){
                    selector.addItem(stock+" ("+companyList.get(stock)+")");
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
            }


            return content;
        }

        @Override
        public boolean onAdvance() {

            //obtain the selected items
            Set selectedStocks= (Set) selector.getValue();

            //if there are selected stocks
            if(selectedStocks.size()>0){
                ArrayList<String> stocksList=new ArrayList<String>();
                for (Object items:selectedStocks){
                    String stock=((String)items).split(" ")[0];
                    stocksList.add(stock);
                }
                DataPlaybackEngineStates.playingSymbols=stocksList.toArray(new String[stocksList.size()]);
                return true;
            }
            else{
                Notification.show("Please select stocks to play");
                return false;
            }

        }

        @Override
        public boolean onBack() {
            return true;
        }


    }

    class ThirdStep implements WizardStep {

        InlineDateField datePicker;
        OptionGroup dateRangeType;




        ThirdStep() {
            datePicker=new InlineDateField();
            dateRangeType = new OptionGroup();

        }

        @Override
        public String getCaption() {
            return "Select the game start day";
        }

        @Override
        public Component getContent() {
            VerticalLayout content = new VerticalLayout();

            content.addComponent(datePicker);
            content.setComponentAlignment(datePicker,Alignment.MIDDLE_CENTER);
            datePicker.setValue(new Date());
            datePicker.setImmediate(true);
            //if this is a OHLC game
            if(DataPlaybackEngineStates.gameConfig.getPlayerType()== PlayerTypes.DAILY_SUMMARY_PLAYER){
                datePicker.setResolution(Resolution.DAY);

            }
            //else if this is a Ticker data based game
            else if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.REAL_TIME_DATA_PLAYER){
                datePicker.setResolution(Resolution.SECOND);

            }

            //select the date range type
            content.addComponent(dateRangeType);
            content.setComponentAlignment(dateRangeType,Alignment.MIDDLE_CENTER);
            dateRangeType.setMultiSelect(true);
            dateRangeType.setHtmlContentAllowed(true);
            dateRangeType.addItem(1);
            dateRangeType.setItemCaption(1, "<b>Show Common date range to all stocks</b>");


            //monitor the selected item
            dateRangeType.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    try {
                        setDateRange();
                    } catch (DataAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }


                }
            });



            //set selectable date range
            try {
                setDateRange();
            } catch (DataAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return content;
        }

        @Override
        public boolean onAdvance() {
            if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.DAILY_SUMMARY_PLAYER){
                //get only the date(ignore time)
                DataPlaybackEngineStates.gameStartDate= DateUtils.truncate(datePicker.getValue(), Calendar.DATE);
            }
            else{
                //save the default time zone first
                TimeZone defaultT=TimeZone.getDefault();
                //fixes the default timezone to GMT to correctly retrieve the time from calendar
                TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
                DataPlaybackEngineStates.gameStartDate= datePicker.getValue();
                //restore the default time zone
                TimeZone.setDefault(TimeZone.getTimeZone(defaultT.getID()));
            }

                return true;

        }

        @Override
        public boolean onBack() {
            return true;
        }




    private void setDateRange() throws DataAccessException {
        Date range[]=null;

        //if it is checked
        if(dateRangeType.getValue().equals(1)){
            //check the game type and request for date ranges
            if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.DAILY_SUMMARY_PLAYER){
                range= StockUtils.getCommonStartingAndEndDates(DataPlaybackEngineStates.playingSymbols,
                        CompanyStockTransactionsData.DataType.OHLC);

            }
            //else if this is a Ticker data based game
            else if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.REAL_TIME_DATA_PLAYER){
                range= StockUtils.getCommonStartingAndEndDates(DataPlaybackEngineStates.playingSymbols,
                        CompanyStockTransactionsData.DataType.TICKER);
            }

        }
        else{
            //check the game type and request for date ranges
            if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.DAILY_SUMMARY_PLAYER){
                range= StockUtils.getStartingAndEndDates(DataPlaybackEngineStates.playingSymbols,
                        CompanyStockTransactionsData.DataType.OHLC);

            }
            //else if this is a Ticker data based game
            else if(DataPlaybackEngineStates.gameConfig.getPlayerType()==PlayerTypes.REAL_TIME_DATA_PLAYER){
                range= StockUtils.getStartingAndEndDates(DataPlaybackEngineStates.playingSymbols,
                        CompanyStockTransactionsData.DataType.TICKER);
            }


        }
        datePicker.setRangeStart(range[0]);
        datePicker.setRangeEnd(range[1]);
        datePicker.setValue(range[0]);
    }
    }






}
