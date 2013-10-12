package org.investovator.ui.agentgaming;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AgentPctView implements WizardStep {

    VerticalLayout content;
    FormLayout agentPctForm;

    public AgentPctView() {
        content = new VerticalLayout();

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

        for (int i = 0; i < agents.length; i++) {
            String selectedAgent = agents[i];

            agentPctForm.addComponent(new TextField(selectedAgent, Integer.toString(100/agents.length)));
        }
    }
}
