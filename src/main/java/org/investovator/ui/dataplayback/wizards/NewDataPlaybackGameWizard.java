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

import com.vaadin.ui.*;
import org.investovator.ui.dataplayback.DataPlaybackMainView;
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

    public NewDataPlaybackGameWizard(Window window,DataPlaybackMainView mainView ) {

        this.mainView=mainView;
        this.window=window;

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

//        this.setVisible(false);

    }


    class FirstStep implements WizardStep{

        @Override
        public String getCaption() {
            return "Decide the Game type";
        }

        @Override
        public Component getContent() {
            Panel content=new Panel();
            content.setContent(new Label("DDD"));
            return content;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean onAdvance() {
            System.out.println("Called");

            return true;
        }

        @Override
        public boolean onBack() {
            System.out.println("Called back");
            return true;
        }
    }

    class SecondStep implements WizardStep{

        @Override
        public String getCaption() {
            return "Set Game Parameters";
        }

        @Override
        public Component getContent() {
            Panel content=new Panel();
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
