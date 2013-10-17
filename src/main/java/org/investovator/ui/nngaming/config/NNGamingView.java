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

package org.investovator.ui.nngaming.config;

import com.vaadin.navigator.ViewChangeListener;
import org.investovator.ann.neuralnet.NNManager;
import org.investovator.ui.GlobalView;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.util.HashMap;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class NNGamingView extends GlobalView implements WizardProgressListener{

    Wizard nnWiz = new Wizard();

    StockSelectView stockSelect;
    ParameterSelectView parameterSelect;
    ParameterAddView parameterAdd;

    public NNGamingView() {

        super();

        nnWiz.setWidth(50, Unit.PERCENTAGE);
        nnWiz.getCancelButton().setVisible(false);

        stockSelect = new StockSelectView();
        parameterSelect = new ParameterSelectView();
        parameterAdd = new ParameterAddView();
       
        nnWiz.addStep(stockSelect);
        nnWiz.addStep(parameterSelect);
        nnWiz.addStep(parameterAdd);

        nnWiz.addListener(this);
        this.addComponent(nnWiz);

    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {


    }


    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
        WizardStep step = event.getActivatedStep();

        if (step instanceof StockSelectView) {
            //TODO
        }


    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {

    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {

        NNManager nnManager;

        HashMap<String, String> newParameters = parameterAdd.getAddedParameterList();
        String stockID = stockSelect.getSelectedStock();
        String [] selectedParams = parameterSelect.getSelectedParameters();

        if(newParameters.size() == 0)
        {
            nnManager = new NNManager(selectedParams,stockID);
        }
        else{
            nnManager = new NNManager(newParameters,selectedParams,stockID);
        }

        nnManager.createNeuralNetwork();

    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {

    }
}
