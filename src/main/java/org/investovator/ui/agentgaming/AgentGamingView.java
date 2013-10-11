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

import java.util.Iterator;
import java.util.Set;

/**
 * @author Amila Surendra
 * @author Hasala Surasinghe
 * @version $Revision
 */

@SuppressWarnings("serial")
public class AgentGamingView extends GlobalView {


    String[] selectedStocks;
    String[] selectedAgents;
    ConfigGenerator configGenerator;


    public AgentGamingView(){

        super();

        authenticator = Authenticator.getInstance();

        stockSelectView = new VerticalLayout();
        agentSelectView = new VerticalLayout();

        setViews();
        createLayouts();

    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

                //Notification.show("Welcome to Agent Gaming Engine");

                String parameters = viewChangeEvent.getParameters();

                removeAllComponents();

                if(parameters.equals("")){
                    this.addComponent(stockSelectView);
                }else if(parameters.equals("agentSelect")){
                    this.addComponent(agentSelectView);
                }else if(parameters.equals("population")){
                    this.addComponent(agentPercentageView);
                }

    }


    private void createLayouts(){

        agentPercentageView = new VerticalLayout();
        agentPercentageView.addStyleName("outlined");
        agentPercentageView.setSizeFull();
        agentPercentageView.setSpacing(true);

        agentPctForm = new FormLayout();
        agentPercentageView.addComponent(agentPctForm);

        Button populationNxt = new Button();
        agentPercentageView.addComponent(populationNxt);


        Button agentSelectNxt = new Button("Next");
        agentSelectNxt.addClickListener(agentSelectNextBtnList);
        agentSelectView.addComponent(agentSelectNxt);

        agentSelectList.addValueChangeListener(agentSelectValueChangedEvent);

    }


    private void setViews(){

        TwinColSelect stockSelectList = new TwinColSelect("Select Stocks for Game");
        stockSelectList.addItem("SAMP");
        stockSelectList.addItem("GOOG");
        stockSelectList.addItem("IBM");


        stockSelectView.addStyleName("outlined");
        stockSelectView.addComponent(stockSelectList);


        agentSelectList = new TwinColSelect("Select Agents for Game");
        agentSelectView.addStyleName("outlined");
        agentSelectView.addComponent(agentSelectList);




        stockSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                Set<String> results = (Set) valueChangeEvent.getProperty().getValue();

                Iterator resultsItr = results.iterator();
                selectedStocks = new String[results.size()];

                int i = 0;

                for (Iterator<String> iterator = results.iterator(); iterator.hasNext(); ) {
                    String next = iterator.next();
                     selectedStocks[i] = next;
                    i++;
                }


            }
        });

        Button next = new Button("Next");

        next.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //To change body of implemented methods use File | Settings | File Templates.

                String outputPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/config";
                configGenerator = new ConfigGenerator(selectedStocks,outputPath);

                getUI().getNavigator().navigateTo(UIConstants.AGENTVIEW + "/agentSelect");
                setAgentSettingsView();
            }
        });

        stockSelectView.addComponent(next);
        stockSelectView.setComponentAlignment(next, Alignment.MIDDLE_RIGHT);
    }



    private void setAgentSettingsView(){

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        String  templateFile = basepath +"/WEB-INF/templates/model_template.xml";


        configGenerator.setModelTemlpateFile(templateFile);
        String[] availableAgents = configGenerator.getSupportedAgentTypes();


        int width = 0;

        for (int i = 0; i < availableAgents.length; i++) {
            String agent = availableAgents[i];
            if(width<agent.length()) width = agent.length();
            agentSelectList.addItem(agent);
        }

        agentSelectList.setWidth(width+10,Unit.EM);
    }


    private void setPopulationView(){

        for (int i = 0; i < selectedAgents.length; i++) {
            String selectedAgent = selectedAgents[i];

            agentPctForm.addComponent(new TextField(selectedAgent, Integer.toString(100/selectedAgents.length)));
        }

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

    Button.ClickListener agentSelectNextBtnList =  new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            setPopulationView();
            getUI().getNavigator().navigateTo(UIConstants.AGENTVIEW + "/population");
        }
    };



    //UI Widgets
    VerticalLayout agentSelectView ;
    VerticalLayout stockSelectView ;

    VerticalLayout agentPercentageView;
    FormLayout agentPctForm;


    TwinColSelect agentSelectList;
}

