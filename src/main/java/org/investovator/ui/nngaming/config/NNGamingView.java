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
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.exceptions.GameCreationException;
import org.investovator.controller.utils.exceptions.GameProgressingException;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.ui.nngaming.utils.GameDataHelper;
import org.investovator.ui.utils.ConfigHelper;
import org.investovator.ui.utils.Session;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.util.ArrayList;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class NNGamingView extends Window implements WizardProgressListener{

    private int SETUP_PARAM_SIZE = 5;
    Wizard nnWiz = new Wizard();

    StockSelectView stockSelect;
    GameSettingsView settingsView;
    //ParameterAddView parameterAdd;
    ParameterSelectView parameterSelect;

    public NNGamingView(String caption) {

        super(caption);

        nnWiz.setWidth(50, Unit.PERCENTAGE);

        stockSelect = new StockSelectView();
        settingsView = new GameSettingsView();
        //parameterAdd = new ParameterAddView();
        parameterSelect = new ParameterSelectView();
       
        nnWiz.addStep(stockSelect);
        nnWiz.addStep(settingsView);
        //nnWiz.addStep(parameterAdd);
        nnWiz.addStep(parameterSelect);

        stockSelect.update();
        parameterSelect.update();

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


    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {

    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {

        ConfigReceiver configReceiver = new ConfigReceiver();
        String path = ConfigHelper.getBasepath();
        configReceiver.setBasePath(path);

        ArrayList<String> selectedStockIDs = stockSelect.getSelectedStocks();
        ArrayList<String> analysisParameters = parameterSelect.getSelectedParameters();
        int daysCount = (int) settingsView.getDaysCount();
        int speedFactor = (int) settingsView.getSpeedFactor();

        ArrayList<TradingDataAttribute > inputParam = new ArrayList<>();
        inputParam.add(TradingDataAttribute.HIGH_PRICE);
        inputParam.add(TradingDataAttribute.LOW_PRICE);
        inputParam.add(TradingDataAttribute.CLOSING_PRICE);
        inputParam.add(TradingDataAttribute.SHARES);
        inputParam.add(TradingDataAttribute.TRADES);
        inputParam.add(TradingDataAttribute.TURNOVER);

        GameDataHelper.getInstance().setStocks(selectedStockIDs);
        GameDataHelper.getInstance().setAnalysisParameters(analysisParameters);

        /*ProgressWindow test = new ProgressWindow("Creating Game....Please Wait!!");
        GameControllerFacade.getInstance().registerListener(test);
        getUI().addWindow(test);

        test.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent closeEvent) {
                closeWindow();
            }
        });*/

        GameController gameController = GameControllerImpl.getInstance();

        Object[] setUpParams = new Object[SETUP_PARAM_SIZE];
        setUpParams[0] = inputParam;
        setUpParams[1] = selectedStockIDs;
        setUpParams[2] = analysisParameters;
        setUpParams[3] = daysCount;
        setUpParams[4] = speedFactor;

        try {
            String instance = gameController.createGameInstance(GameModes.NN_GAME);
            Session.setCurrentGameInstance(instance);

            gameController.setupGame(instance, setUpParams);

            try {
                gameController.startGame(instance);
            } catch (GameProgressingException e) {
                e.printStackTrace();
            }
        } catch (GameCreationException e) {
            e.printStackTrace();
        }


        this.closeWindow();

    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        this.close();
    }

    private void closeWindow(){
        this.close();
    }
}
