package org.investovator.ui.agentgaming.config;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class OtherSimulationSettingsView implements WizardStep {

    VerticalLayout content;
    FormLayout settingsForm;

    private float initialPrice;
    private int numberOfDays;
    private float speedFactor;

    TextField numberOfDaysField;
    TextField speedFactorField;


    public OtherSimulationSettingsView() {
        content = new VerticalLayout();
        settingsForm = new FormLayout();
        content.addComponent(settingsForm);

        numberOfDaysField = new TextField("Number of Days", "1");
        speedFactorField = new TextField("Speed Facctor", "1");


        settingsForm.addComponent(numberOfDaysField);
        settingsForm.addComponent(speedFactorField);

    }

    @Override
    public String getCaption() {
        return "Other Simulation Settings";
    }

    @Override
    public Component getContent() {
        return content;
    }

    @Override
    public boolean onAdvance() {

        numberOfDays = Integer.parseInt( numberOfDaysField.getValue() );
        speedFactor = Integer.parseInt(speedFactorField.getValue());

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onBack() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public float getSpeedFactor() {
        return speedFactor;
    }
}
