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
    }

    private void setupLayout(){

        gameImage = getImage(controller.getGameMode(gameInstance));

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

    }

    private Image getImage(GameModes type){
        FileResource resource = new FileResource(new File(ConfigHelper.getImagePath()+"gameIcon.png"));
        return new Image();
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
