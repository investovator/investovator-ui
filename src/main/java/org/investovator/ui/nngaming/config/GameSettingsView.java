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

import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class GameSettingsView implements WizardStep {

    Label daysSliderValue = new Label();
    Label speedSliderValue = new Label();
    Slider daysSlider = new Slider(1, 20);
    Slider speedSlider = new Slider(1,5);
    HorizontalLayout speedLayout = new HorizontalLayout();
    HorizontalLayout daysLayout = new HorizontalLayout();

    double daysCount;
    double speedFactor = 1;

    VerticalLayout content;

    public GameSettingsView(){

        daysSlider.setOrientation(SliderOrientation.HORIZONTAL);
        daysSlider.setCaption("Select Game Duration");
        daysSlider.setResolution(0);
        daysSlider.setWidth(90, Sizeable.Unit.MM);
        daysSlider.setValue((double) 1);
        daysSlider.setImmediate(true);
        daysSlider.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                final Double value = Double.valueOf(String.valueOf(valueChangeEvent.getProperty().getValue()));
                daysCount = value;
                daysSliderValue.setValue(String.valueOf(value)+" Days");
            }
        });

        speedSlider.setOrientation(SliderOrientation.HORIZONTAL);
        speedSlider.setCaption("Select Game Speed");
        speedSlider.setResolution(0);
        speedSlider.setWidth(90, Sizeable.Unit.MM);
        speedSlider.setValue((double) 1);
        speedSlider.setImmediate(true);
        speedSlider.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                final Double value = (Double) valueChangeEvent.getProperty().getValue();
                speedFactor = value;
                speedSliderValue.setValue(String.valueOf(value)+"X");
            }
        });

        daysSliderValue.setValue("1 Day");
        daysSliderValue.setImmediate(true);

        speedSliderValue.setValue("1X");
        speedSliderValue.setImmediate(true);

        speedLayout.addComponent(speedSlider);
        speedLayout.addComponent(speedSliderValue);
        speedLayout.setSpacing(true);
        speedLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        speedLayout.setMargin(true);

        daysLayout.addComponent(daysSlider);
        daysLayout.addComponent(daysSliderValue);
        daysLayout.setSpacing(true);
        daysLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        daysLayout.setMargin(true);

        VerticalLayout finalLayout = new VerticalLayout();
        finalLayout.addComponent(daysLayout);
        finalLayout.addComponent(speedLayout);
        finalLayout.setMargin(true);

        content = new VerticalLayout();
        content.addComponent(finalLayout);
        content.setComponentAlignment(finalLayout, Alignment.MIDDLE_CENTER);

    }



    @Override
    public String getCaption() {
        return "Game Settings";
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
        return true;
    }

    public double getDaysCount(){
        return daysCount;
    }

    public double getSpeedFactor(){
        return speedFactor;
    }
}
