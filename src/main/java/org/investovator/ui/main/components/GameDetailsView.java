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
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public abstract class GameDetailsView extends HorizontalLayout {

    protected String gameInstance;
    protected GameController controller;
    protected GameModes gameMode;

    private Image gameImage;
    private HorizontalLayout buttonLayout;
    private VerticalLayout descriptionLayout;

    private Label nameLabel;
    private Label descriptionLabel;


    public GameDetailsView(String instanceID, GameController controller){
        gameInstance = instanceID;
        this.controller = controller;
        setupLayout();
    }

    public GameDetailsView(GameModes gameMode, GameController controller){
        this.gameMode = gameMode;
        this.controller = controller;
        setupLayout();
    }

    private void setupLayout(){

        setMargin(true);

        gameImage = getImage();


        buttonLayout = new HorizontalLayout();
        for(Button button : getButtons()){
            buttonLayout.addComponent(button);
        }

        descriptionLayout = new VerticalLayout();


        if(getName()!=null){
            nameLabel = new Label(getName());
            nameLabel.addStyleName("game-name");
            descriptionLayout.addComponent(nameLabel);
        }

        descriptionLabel = new Label(getDescription());
        descriptionLayout.addComponent(descriptionLabel);

        this.addComponent(gameImage);
        this.addComponent(descriptionLayout);
        this.addComponent(buttonLayout);

        this.setComponentAlignment(gameImage,Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(descriptionLayout,Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(buttonLayout,Alignment.MIDDLE_CENTER);

        this.setExpandRatio(gameImage,1);
        this.setExpandRatio(descriptionLayout,5);
        this.setExpandRatio(buttonLayout,1);

    }

    public void setRatios(float icon, float description, float button){
        this.setExpandRatio(gameImage,icon);
        this.setExpandRatio(descriptionLayout,description);
        this.setExpandRatio(buttonLayout,button);
    }

    public String getName(){
        return controller.getName(gameInstance);
    }

    public String getDescription(){
        return controller.getDescription(gameInstance);
    }

    public Image getImage(){
        FileResource resource = new FileResource(new File(ConfigHelper.getImagePath()+"game_icon.jpg"));
        Image img = new Image(null,resource);
        img.setHeight("50px");
        img.setWidth("50px");
        return img;
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
