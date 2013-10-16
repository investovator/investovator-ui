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


package org.investovator.ui.dataplayback.wizards;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.ui.dataplayback.DataPlaybackMainView;
import org.investovator.ui.dataplayback.util.DataPLaybackEngineGameTypes;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class NewDataPlaybackGameWizard extends Wizard implements WizardProgressListener {

    //window that this wizard is run
    Window window;
    //Parent view class
    DataPlaybackMainView mainView;


    public NewDataPlaybackGameWizard(Window window, DataPlaybackMainView mainView) {

        this.mainView = mainView;
        this.window = window;

        this.addStep(new FirstStep());
        this.addStep(new SecondStep());

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

        this.window.close();
//        this.setVisible(false);
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent wizardCancelledEvent) {
        Notification.show("Cancelled");
        this.window.close();

        System.out.println(getUI().getParent());
    }


    class FirstStep implements WizardStep {

        @Override
        public String getCaption() {
            return "Decide the Game type";
        }

        @Override
        public Component getContent() {
            VerticalLayout content = new VerticalLayout();


            //add the game types
            OptionGroup gameTypes = new OptionGroup();
            content.addComponent(gameTypes);
            gameTypes.setMultiSelect(false);
            gameTypes.setHtmlContentAllowed(true);
            gameTypes.addItem(DataPLaybackEngineGameTypes.OHLC_BASED);
            gameTypes.setItemCaption(DataPLaybackEngineGameTypes.OHLC_BASED, "<b>OHLC price based game</b>");

            gameTypes.addItem(DataPLaybackEngineGameTypes.TICKER_BASED);
            gameTypes.setItemCaption(DataPLaybackEngineGameTypes.TICKER_BASED, "<b>Ticker data based game</b>");

            //default item
            gameTypes.select(DataPLaybackEngineGameTypes.OHLC_BASED);

            //fire value change events immediately
            gameTypes.setImmediate(true);

            //monitor the selected item
            gameTypes.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if ((valueChangeEvent.getProperty().getValue() == DataPLaybackEngineGameTypes.OHLC_BASED)) {

                        DataPlaybackEngineStates.currentGameMode = DataPLaybackEngineGameTypes.OHLC_BASED;

                    } else if ((valueChangeEvent.getProperty().getValue() == DataPLaybackEngineGameTypes.TICKER_BASED)) {

                        DataPlaybackEngineStates.currentGameMode = DataPLaybackEngineGameTypes.TICKER_BASED;
                    }
                }
            });

            return content;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean onAdvance() {

            return true;
        }

        @Override
        public boolean onBack() {
            return true;
        }
    }

    class SecondStep implements WizardStep {

        @Override
        public String getCaption() {
            return "Set Game Parameters";
        }

        @Override
        public Component getContent() {
            Panel content = new Panel();
            content.setContent(new Label("2222"));
            return content;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean onAdvance() {
            System.out.println("Called");

            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean onBack() {
            System.out.println("Called back");
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }


    }


}
