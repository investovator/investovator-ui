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
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.DataPlayerFacade;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.dataplaybackengine.utils.StockUtils;
import org.investovator.ui.dataplayback.DataPlaybackMainView;
import org.investovator.ui.dataplayback.util.DataPLaybackEngineGameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.util.*;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class NewDataPlaybackGameWizard extends Wizard implements WizardProgressListener {

    //window that this wizard is run
    Window window;
//    //Parent view class
//    DataPlaybackMainView mainView;
//    //to access the data
//    DataPlayer player;


    public NewDataPlaybackGameWizard(Window window, DataPlaybackMainView mainView) {

//        this.mainView = mainView;
        this.window = window;
//        this.player=new DataPlayer();

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

//        mainView.setUpGame(false);

        //todo - set these attributes from the wizard
        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes = new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        //initialize the necessary player
        DataPlayerFacade.getInstance().createPlayer(DataPlaybackEngineStates.currentGameMode,
                DataPlaybackEngineStates.playingSymbols,DataPlaybackEngineStates.gameStartDate,attributes,
                TradingDataAttribute.PRICE);

        this.window.close();
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent wizardCancelledEvent) {
        Notification.show("Cancelled");
        this.window.close();

    }


    class FirstStep implements WizardStep {

        OptionGroup gameTypes;

        FirstStep() {
            gameTypes= new OptionGroup();
        }

        @Override
        public String getCaption() {
            return "Decide the Game type";
        }

        @Override
        public Component getContent() {
            VerticalLayout content = new VerticalLayout();


            //add the game types

            content.addComponent(gameTypes);
            gameTypes.setMultiSelect(false);
            gameTypes.setHtmlContentAllowed(true);
            gameTypes.addItem(PlayerTypes.DAILY_SUMMARY_PLAYER);
            gameTypes.setItemCaption(PlayerTypes.DAILY_SUMMARY_PLAYER, "<b>OHLC price based game</b>");

            gameTypes.addItem(PlayerTypes.REAL_TIME_DATA_PLAYER);
            gameTypes.setItemCaption(PlayerTypes.REAL_TIME_DATA_PLAYER, "<b>Ticker data based game</b>");

            //default item
            gameTypes.select(DataPLaybackEngineGameTypes.OHLC_BASED);

            //fire value change events immediately
            gameTypes.setImmediate(true);

//            //monitor the selected item
//            gameTypes.addValueChangeListener(new Property.ValueChangeListener() {
//                @Override
//                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
//                    if ((valueChangeEvent.getProperty().getValue() == DataPLaybackEngineGameTypes.OHLC_BASED)) {
//
//                        DataPlaybackEngineStates.currentGameMode = DataPLaybackEngineGameTypes.OHLC_BASED;
//
//                    } else if ((valueChangeEvent.getProperty().getValue() == DataPLaybackEngineGameTypes.TICKER_BASED)) {
//
//                        DataPlaybackEngineStates.currentGameMode = DataPLaybackEngineGameTypes.TICKER_BASED;
//                    }
//                }
//            });

            return content;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean onAdvance() {
            //set the selected state
            if(gameTypes.getValue()==PlayerTypes.DAILY_SUMMARY_PLAYER){
                DataPlaybackEngineStates.currentGameMode = PlayerTypes.DAILY_SUMMARY_PLAYER;
            }
            if(gameTypes.getValue()==PlayerTypes.REAL_TIME_DATA_PLAYER){
                DataPlaybackEngineStates.currentGameMode = PlayerTypes.REAL_TIME_DATA_PLAYER;
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

            selector.setRows(6);
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

            //if there are selested stocks
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
            return true;  //To change body of implemented methods use File | Settings | File Templates.
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
            //TODO - set this to the starting/ending date of the data set
            datePicker.setValue(new Date());
            datePicker.setImmediate(true);
            datePicker.setTimeZone(TimeZone.getTimeZone("UTC"));
            datePicker.setLocale(Locale.US);
            //if this is a OHLC game
            if(DataPlaybackEngineStates.currentGameMode== PlayerTypes.DAILY_SUMMARY_PLAYER){
                datePicker.setResolution(Resolution.DAY);

            }
            //else if this is a Ticker data based game
            else if(DataPlaybackEngineStates.currentGameMode==PlayerTypes.REAL_TIME_DATA_PLAYER){
                datePicker.setResolution(Resolution.SECOND);

            }

            //select the date range type

            content.addComponent(dateRangeType);
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

            DataPlaybackEngineStates.gameStartDate=datePicker.getValue();

                return true;

        }

        @Override
        public boolean onBack() {
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }




    private void setDateRange() throws DataAccessException {
        Date range[]=null;

        //if it is checked
        if(dateRangeType.getValue().equals(1)){
            //check the game type and request for date ranges
            if(DataPlaybackEngineStates.currentGameMode==PlayerTypes.DAILY_SUMMARY_PLAYER){
                range= StockUtils.getCommonStartingAndEndDates(DataPlaybackEngineStates.playingSymbols,
                        CompanyStockTransactionsData.DataType.OHLC);

            }
            //else if this is a Ticker data based game
            else if(DataPlaybackEngineStates.currentGameMode==PlayerTypes.REAL_TIME_DATA_PLAYER){
                range= StockUtils.getCommonStartingAndEndDates(DataPlaybackEngineStates.playingSymbols,
                        CompanyStockTransactionsData.DataType.TICKER);
            }

        }
        else{
            //check the game type and request for date ranges
            if(DataPlaybackEngineStates.currentGameMode==PlayerTypes.DAILY_SUMMARY_PLAYER){
                range= StockUtils.getStartingAndEndDates(DataPlaybackEngineStates.playingSymbols,
                        CompanyStockTransactionsData.DataType.OHLC);

            }
            //else if this is a Ticker data based game
            else if(DataPlaybackEngineStates.currentGameMode==PlayerTypes.REAL_TIME_DATA_PLAYER){
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
