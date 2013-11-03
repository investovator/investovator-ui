package org.investovator.ui.agentgaming.config;

import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AgentPctView implements WizardStep {

    VerticalLayout content;
    FormLayout agentPctForm;
    int agentCount;

    public AgentPctView() {
        content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        agentPctForm = new FormLayout();
        content.addComponent(agentPctForm);
    }

    @Override
    public String getCaption() {
        return "Select Agent Population";
    }

    @Override
    public Component getContent() {
        return content;
    }

    @Override
    public boolean onAdvance() {
        return true;
    }

    @Override
    public boolean onBack() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAgents(String[] agents){

        agentPctForm.removeAllComponents();
        agentCount = agents.length;

        for (int i = 0; i < agents.length; i++) {
            String selectedAgent = agents[i];

            agentPctForm.addComponent(new TextField(selectedAgent, Integer.toString(100/agents.length)));
        }
    }

    public HashMap<String,Integer> getAgentPopulation(){

       HashMap<String,Integer> agentPopulation = new HashMap<String, Integer>();

        for (int i = 0; i < agentCount; i++) {
            TextField agent = (TextField) agentPctForm.getComponent(i);
            agentPopulation.put(agent.getCaption(),Integer.parseInt(agent.getValue()));
        }

       return agentPopulation;
    }
}
