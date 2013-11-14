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

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.investovator.ann.config.ConfigReceiver;
import org.investovator.ann.neuralnet.NNManager;
import org.investovator.controller.GameControllerFacade;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.exceptions.GameProgressingException;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.ui.utils.ConfigHelper;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.util.ArrayList;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class NNGamingView extends Window implements WizardProgressListener{

    Wizard nnWiz = new Wizard();

    StockSelectView stockSelect;
    ParameterSelectView parameterSelect;
    ParameterAddView parameterAdd;

    public NNGamingView(String caption) {

        super(caption);

        nnWiz.setWidth(50, Unit.PERCENTAGE);

        stockSelect = new StockSelectView();
        parameterSelect = new ParameterSelectView();
        parameterAdd = new ParameterAddView();
       
        nnWiz.addStep(stockSelect);
        nnWiz.addStep(parameterSelect);
        nnWiz.addStep(parameterAdd);

        stockSelect.update();

        nnWiz.addListener(this);
        nnWiz.setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(nnWiz);
        setContent(layout);

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

        ConfigReceiver configReceiver = new ConfigReceiver();
        String path = ConfigHelper.getBasepath();
        configReceiver.setBasePath(path);


        /*NNManager nnManager;

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

        nnManager.createNeuralNetwork();*/

        ArrayList<TradingDataAttribute> inputParam = new ArrayList<TradingDataAttribute>();
        inputParam.add(TradingDataAttribute.HIGH_PRICE);
        inputParam.add(TradingDataAttribute.LOW_PRICE);
        inputParam.add(TradingDataAttribute.CLOSING_PRICE);
        inputParam.add(TradingDataAttribute.SHARES);
        inputParam.add(TradingDataAttribute.TRADES);
        inputParam.add(TradingDataAttribute.TURNOVER);

        NNManager nnManager = new NNManager(inputParam,stockSelect.getSelectedStock());
        nnManager.createNeuralNetwork();

        try {
            GameControllerFacade.getInstance().startGame(GameModes.NN_GAME,null);
        } catch (GameProgressingException e) {
            e.printStackTrace();
        }

        closeWindow();

    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        this.close();
    }

    private void closeWindow(){
        this.close();
    }
}
