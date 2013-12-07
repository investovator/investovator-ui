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
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class GameSettingsView implements WizardStep {

    TextField days;
    TextField speed;

    private int MAX_SPEED_FACTOR = 5;
    private int daysCount = 1;
    private int speedFactor = 1;

    VerticalLayout content;

    public GameSettingsView(){

        days = new TextField();
        days.setCaption("Select Game Duration");
        days.setValue(String.valueOf(daysCount));
        days.setImmediate(true);
        days.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            try {
                daysCount = Integer.parseInt(days.getValue());
            } catch (NumberFormatException e){
                Notification.show("Please enter a valid input value", Notification.Type.TRAY_NOTIFICATION);
            }

            }
        });


        speed = new TextField();
        speed.setCaption("Select Game Speed");
        speed.setValue(String.valueOf(speedFactor));
        speed.setImmediate(true);
        speed.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            try{
                speedFactor = Integer.parseInt(speed.getValue());
            } catch (NumberFormatException e){
                Notification.show("Please enter a valid input value", Notification.Type.TRAY_NOTIFICATION);
            }

            }
        });

        VerticalLayout finalLayout = new VerticalLayout();
        finalLayout.addComponent(days);
        finalLayout.addComponent(speed);
        finalLayout.setSpacing(true);
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

        if(daysCount <= 0 || speedFactor <= 0 || days.getValue().isEmpty() ||
                speed.getValue().isEmpty()){
            Notification.show("Please enter a valid input value", Notification.Type.TRAY_NOTIFICATION);
            return false;
        }

        try{
            daysCount = Integer.parseInt(days.getValue());
            speedFactor = Integer.parseInt(speed.getValue());

            if(speedFactor > MAX_SPEED_FACTOR)  {
                Notification.show("Please enter a Game Speed within range 1 - 5", Notification.Type.TRAY_NOTIFICATION);
                return false;
            }
            else {
                return true;
            }

        }   catch (NumberFormatException e){
            Notification.show("Please enter a valid input value", Notification.Type.TRAY_NOTIFICATION);
            return false;
        }

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
