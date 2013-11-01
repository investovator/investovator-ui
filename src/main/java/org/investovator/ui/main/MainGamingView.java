package org.investovator.ui.main;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import org.investovator.MyVaadinUI;
import org.investovator.controller.GameControllerFacade;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.controller.utils.enums.GameStates;
import org.investovator.ui.GlobalView;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.UIConstants;

/**
 *
 * @author: Hasala Surasinghe
 * @author : ishan
 * @version: ${Revision}
 *
 */
@SuppressWarnings("serial")

public class MainGamingView extends GlobalView{

    public MainGamingView(){
    }

    /**
     * Loads the necessary content to the main view up on the conditions
     */
    private void setWindowContent(){

        GameModes gameMode=GameControllerFacade.getInstance().getCurrentGameMode();
        GameStates gameState=GameControllerFacade.getInstance().getCurrentGameState();



        //if the user is an admin
        if(Authenticator.getInstance().getMyPrivileges()== Authenticator.UserType.ADMIN){
//            //if no game is running
//            if(gameState==GameStates.NEW){
//                this.addComponent(new AdminGameConfigLayout());
//            }
//            //if a game is running
//            else if(gameState==GameStates.RUNNING){
//                //if it's a data playback
//                if(gameMode==GameModes.PAYBACK_ENG){
//                    //implement properly
//                    getUI().getNavigator().navigateTo(UIConstants.DATAPLAY_USR_DASH);
//                }
//            }

            this.addComponent(new AdminGameConfigLayout());
        }
        else if(Authenticator.getInstance().getMyPrivileges()== Authenticator.UserType.ORDINARY){
            //if no game is running
            if(gameState==GameStates.NEW){
                Notification.show("No deployed game found. Contact Admin", Notification.Type.ERROR_MESSAGE);

            }
            //if a game is running
            else if(gameState==GameStates.RUNNING){
                //if it's a data playback  and it supports multiplayer
                if(gameMode==GameModes.PAYBACK_ENG ){
                    if(DataPlaybackEngineStates.isMultiplayer){

                        getUI().getNavigator().navigateTo(UIConstants.DATAPLAY_USR_DASH);
                    }
                    else{
                        Notification.show("Not a multiplayer game", Notification.Type.ERROR_MESSAGE);
                    }
                }
                if ((gameMode==GameModes.AGENT_GAME)){
                    getUI().getNavigator().navigateTo(UIConstants.AGENT_DASH_VIEW);
                }
            }
        }



    }



    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        this.removeAllComponents();


        //if not logged in
        if(!Authenticator.getInstance().isLoggedIn()){
            ((MyVaadinUI)MyVaadinUI.getCurrent()).getNavigator().navigateTo("");
        } else{
            Notification.show(Authenticator.getInstance().getCurrentUser());
            setWindowContent();

        }

    }


}
