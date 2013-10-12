package org.investovator.ui.agentgaming;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.GlobalView;
import com.vaadin.ui.*;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.controller.config.ConfigGenerator;
import org.investovator.ui.utils.UIConstants;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.util.Iterator;
import java.util.Set;

/**
 * @author Amila Surendra
 * @author Hasala Surasinghe
 * @version $Revision
 */

@SuppressWarnings("serial")
public class AgentGamingView extends GlobalView implements WizardProgressListener {


    ConfigGenerator configGenerator;

    Wizard agentWiz = new Wizard();

    StockSelectView stockSelect ;
    AgentSelectView agentSelect ;
    AgentPctView agentPct;

    public AgentGamingView(){

        super();


        agentWiz.setWidth(50, Unit.PERCENTAGE);
        agentWiz.getCancelButton().setVisible(false);

        stockSelect = new StockSelectView();
        agentSelect = new AgentSelectView();
        agentPct = new AgentPctView();

        agentWiz.addStep(stockSelect);
        agentWiz.addStep(agentSelect);
        agentWiz.addStep(agentPct);

        agentWiz.addListener(this);
        this.addComponent(agentWiz);

    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {



    }




    //UI Widgets
    VerticalLayout agentSelectView ;
    VerticalLayout stockSelectView ;

    VerticalLayout agentPercentageView;
    FormLayout agentPctForm;


    TwinColSelect agentSelectList;

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
        WizardStep step =  event.getActivatedStep();

        if(step instanceof AgentSelectView) {
            String outputPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/config";
            configGenerator = new ConfigGenerator(stockSelect.getSelectedStocks(),outputPath);

            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            String  templateFile = basepath +"/WEB-INF/templates/model_template.xml";


            configGenerator.setModelTemlpateFile(templateFile);
            String[] availableAgents = configGenerator.getSupportedAgentTypes();

            agentSelect.setAgents(availableAgents);
        }

        if(step instanceof AgentPctView){

            ((AgentPctView) step).setAgents(agentSelect.getSelectedAgents());
        }


    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

