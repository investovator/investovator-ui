package org.investovator.ui.agentgaming.config;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.Notification;
import org.investovator.ui.GlobalView;
import org.investovator.controller.config.ConfigGenerator;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Amila Surendra
 * @author Hasala Surasinghe
 * @version $Revision
 */

@SuppressWarnings("serial")
public class AgentGamingView extends GlobalView implements WizardProgressListener {


    ConfigGenerator configGenerator;

    Wizard agentWiz = new Wizard();

    StockSelectView stockSelect;
    AgentSelectView agentSelect;
    AgentPctView agentPct;
    OtherSimulationSettingsView otherSettings;

    private String mainXmlOutputFile;

    public AgentGamingView() {

        super();


        agentWiz.setWidth(50, Unit.PERCENTAGE);
        agentWiz.getCancelButton().setVisible(false);

        stockSelect = new StockSelectView();
        agentSelect = new AgentSelectView();
        agentPct = new AgentPctView();
        otherSettings = new OtherSimulationSettingsView();

        agentWiz.addStep(stockSelect);
        agentWiz.addStep(agentSelect);
        agentWiz.addStep(agentPct);
        agentWiz.addStep(otherSettings);

        agentWiz.addListener(this);
        this.addComponent(agentWiz);

    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {


    }


    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
        WizardStep step = event.getActivatedStep();

        if (step instanceof AgentSelectView) {
            String outputPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/config";
            mainXmlOutputFile = outputPath + "/main.xml" ;
            configGenerator = new ConfigGenerator(stockSelect.getSelectedStocks(), outputPath);

            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            String templateFile = basepath + "/WEB-INF/templates/model_template.xml";
            String reportTemplateFile =  basepath + "/WEB-INF/templates/report_template.xml";
            String mainTemplateFile =  basepath + "/WEB-INF/templates/main_template.xml";
            String beanTemplateFile =  basepath + "/WEB-INF/templates/bean-config-template.xml";


            configGenerator.setModelTemlpateFile(templateFile);
            configGenerator.setReportTemlpateFile(reportTemplateFile);
            configGenerator.setMainTemplateFile(mainTemplateFile);
            configGenerator.setSpringBeanConfigTemplate(beanTemplateFile);

            String[] availableAgents = configGenerator.getSupportedAgentTypes();

            ((AgentSelectView) step).setAgents(availableAgents);
        }

        if (step instanceof AgentPctView) {

            ((AgentPctView) step).setAgents(agentSelect.getSelectedAgents());
        }


    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {

        HashMap<String, Integer> agentPopulation = agentPct.getAgentPopulation();

        Iterator<String> agentsSet = agentPopulation.keySet().iterator();

        //Adding reports is hard coded for now
        String[] types = configGenerator.getSupportedReports();
        String[] result = configGenerator.getDependencyReportBeans(types[0]);
        configGenerator.addDependencyReportBean(result);

        //Agent Percentages
        while (agentsSet.hasNext()) {
            String agent = agentsSet.next();
            configGenerator.addAgent(agent, agentPopulation.get(agent));
        }

        //Other agent Properties
        configGenerator.setInitialPrice(100);  //Should get this from history data.
        configGenerator.setNoOfDays(otherSettings.getNumberOfDays());
        configGenerator.setSpeedFactor(otherSettings.getSpeedFactor());


        configGenerator.createConfigs();

        System.setProperty("jabm.config", mainXmlOutputFile);

        Notification.show("Configuration for agent game created");
     }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

