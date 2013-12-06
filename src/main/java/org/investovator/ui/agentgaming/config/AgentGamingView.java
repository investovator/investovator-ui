package org.investovator.ui.agentgaming.config;

//<<<<<<< HEAD
//import com.vaadin.server.VaadinService;
//import com.vaadin.ui.Window;
//import org.investovator.controller.GameControllerFacade;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Window;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.config.ConfigGenerator;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.exceptions.GameCreationException;
import org.investovator.controller.utils.exceptions.GameProgressingException;
import org.investovator.ui.dataplayback.util.ProgressWindow;
import org.investovator.ui.utils.TestDataGenerator;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.*;

import java.util.HashMap;
import java.util.Iterator;

//=======
//>>>>>>> newFacadeStructure

/**
 * @author Amila Surendra
 * @author Hasala Surasinghe
 * @version $Revision
 */

@SuppressWarnings("serial")
public class AgentGamingView extends Window implements WizardProgressListener {


    ConfigGenerator configGenerator;

    Wizard agentWiz = new Wizard();

    StockSelectView stockSelect;
    AgentSelectView agentSelect;
    AgentPctView agentPct;
    OtherSimulationSettingsView otherSettings;

    private String mainXmlOutputFile;

    public AgentGamingView() {

        agentWiz.setWidth("90%");
        agentWiz.setHeight("90%");

        stockSelect = new StockSelectView();
        agentSelect = new AgentSelectView();
        agentPct = new AgentPctView();
        otherSettings = new OtherSimulationSettingsView();

        agentWiz.addStep(stockSelect);
        agentWiz.addStep(agentSelect);
        agentWiz.addStep(agentPct);
        agentWiz.addStep(otherSettings);

        agentWiz.addListener(this);

        this.setContent(agentWiz);
    }


    public void update() {
         stockSelect.update();
    }

    boolean configSet=false;

    private void setConfigGeneratorProp(){

        String outputPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/config";
        mainXmlOutputFile = outputPath + "/main.xml" ;
        configGenerator = new ConfigGenerator(stockSelect.getSelectedStocks(), outputPath);

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        String templateFile = basepath + "/WEB-INF/templates/model_template.xml";
        String reportTemplateFile =  basepath + "/WEB-INF/templates/report_template.xml";
        String mainTemplateFile =  basepath + "/WEB-INF/templates/main_template.xml";
        String beanTemplateFile =  basepath + "/WEB-INF/templates/bean-config-template.xml";
        String propertiesFile = basepath +  "/WEB-INF/configuration/config.properties";
        String stockPropertiesFile = basepath + "/WEB-INF/templates/simulation.properties";

        configGenerator.setModelTemlpateFile(templateFile);
        configGenerator.setReportTemlpateFile(reportTemplateFile);
        configGenerator.setMainTemplateFile(mainTemplateFile);
        configGenerator.setSpringBeanConfigTemplate(beanTemplateFile);
        configGenerator.setProperties(propertiesFile);
        configGenerator.addProperties("Default","file:"+stockPropertiesFile);

        configSet=true;
    }


    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
        WizardStep step = event.getActivatedStep();

        if (step instanceof AgentSelectView) {

            if (!configSet) setConfigGeneratorProp();

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
        configGenerator.setInitialPrice(500);  //Should get this from history data.
        configGenerator.setNoOfDays(otherSettings.getNumberOfDays());
        configGenerator.setSpeedFactor(otherSettings.getSpeedFactor());


        configGenerator.createConfigs();

        System.setProperty("jabm.config", mainXmlOutputFile);

        //Notification.show("Configuration for agent game created");

        GameController controller = GameControllerImpl.getInstance();

        ProgressWindow test = new ProgressWindow("Creating Agent Game");

        try {
            String instance = controller.createGameInstance(GameModes.AGENT_GAME);

            //TODO: Remove - Generate test data
            String[] stocks = new String[stockSelect.getSelectedStocks().length - 1];
            for (int i = 0; i < stocks.length ; i++) {
                stocks[i] = stockSelect.getSelectedStocks()[i];
            }

            TestDataGenerator.createTestData(instance,"testUser1", stocks);
            TestDataGenerator.createTestData(instance,"admin", stocks);
            TestDataGenerator.createTestData(instance,"dexter", stocks);
            TestDataGenerator.createTestData(instance,"saman", stocks);
            TestDataGenerator.createTestData(instance,"white", stocks);
            TestDataGenerator.createTestData(instance,"monica", stocks);
            TestDataGenerator.createTestData(instance,"patrick", stocks);
            TestDataGenerator.createTestData(instance,"quinn", stocks);

            controller.registerListener(instance, test);
            getUI().addWindow(test);
            controller.startGame(instance);

            test.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent closeEvent) {
                    closeWindow();
                }
            });

        } catch (GameCreationException e) {
            e.printStackTrace();
        } catch (GameProgressingException e) {
            e.printStackTrace();
        }

    }


    private void closeWindow(){
        this.close();
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        closeWindow();
    }
}

