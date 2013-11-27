/*
 * investovator, Stock Market Gaming framework
 * Copyright (C) 2013  investovator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.main.components;

import com.vaadin.server.FileResource;
import com.vaadin.ui.*;
import org.investovator.controller.GameController;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.ui.utils.ConfigHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public abstract class GameDetailsView extends HorizontalLayout {

    protected String gameInstance;
    protected GameController controller;

    private Image gameImage;
    private VerticalLayout buttonLayout;
    private VerticalLayout descriptionLayout;

    private Label nameLabel;
    private Label descriptionLabel;


    public GameDetailsView(String instanceID, GameController controller){
        gameInstance = instanceID;
        this.controller = controller;
        setupLayout();
    }

    private void setupLayout(){

        gameImage = getImage(controller.getGameMode(gameInstance));
        gameImage.setHeight("150px");
        gameImage.setWidth("150px");

        buttonLayout = new VerticalLayout();
        for(Button button : getButtons()){
            buttonLayout.addComponent(button);
        }

        descriptionLayout = new VerticalLayout();

        nameLabel = new Label(controller.getName(gameInstance));
        descriptionLabel = new Label(controller.getDescription(gameInstance));

        descriptionLayout.addComponent(nameLabel);
        descriptionLayout.addComponent(descriptionLabel);

        this.addComponent(gameImage);
        this.addComponent(descriptionLayout);
        this.addComponent(buttonLayout);

        this.setComponentAlignment(gameImage,Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(descriptionLayout,Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(buttonLayout,Alignment.MIDDLE_CENTER);

    }

    private Image getImage(GameModes type){
        FileResource resource = new FileResource(new File(ConfigHelper.getImagePath()+"game_icon.jpg"));
        return new Image(null,resource);
    }



    public void update(){
        buttonLayout.removeAllComponents();
        for(Button button : getButtons()){
            buttonLayout.addComponent(button);
        }
    }


    /**
     * Concrete class should extend and provide necessary buttons and behaviour.
     * @return
     */
    public abstract List<Button> getButtons();
}
