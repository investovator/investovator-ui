package org.investovator.ui.agentgaming;

import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Component;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import org.investovator.controller.config.ConfigGenerator;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Iterator;
import java.util.Set;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AgentSelectView implements WizardStep {

    String[] selectedAgents;
    TwinColSelect agentSelectList;
    VerticalLayout content;

    public void setGenerator() {
    }


    public AgentSelectView() {

        content = new VerticalLayout();


        agentSelectList = new TwinColSelect("Select Agents for Game");
        agentSelectList.addValueChangeListener(agentSelectValueChangedEvent);

        content.addStyleName("outlined");
        content.addComponent(agentSelectList);


    }



    @Override
    public String getCaption() {
        return "Select Agents";
    }

    @Override
    public Component getContent() {
        return  content;
    }

    @Override
    public boolean onAdvance() {
        return true;
    }

    @Override
    public boolean onBack() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String[] getSelectedAgents(){
        return selectedAgents;
    }


    public void setAgents(String[] agents){

        int width = 0;

        for (int i = 0; i < agents.length; i++) {
            String agent = agents[i];
            if(width<agent.length()) width = agent.length();
            agentSelectList.addItem(agent);
        }

        agentSelectList.setWidth(width+10, Sizeable.Unit.EM);
    }


    Property.ValueChangeListener agentSelectValueChangedEvent =  new Property.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

            Set<String> results = (Set) valueChangeEvent.getProperty().getValue();

            Iterator resultsItr = results.iterator();
            selectedAgents = new String[results.size()];

            int i = 0;

            for (Iterator<String> iterator = results.iterator(); iterator.hasNext(); ) {
                String next = iterator.next();
                selectedAgents[i] = next;
                i++;
            }


        }
    };
}
