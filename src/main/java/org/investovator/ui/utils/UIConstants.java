package org.investovator.ui.utils;

import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataPlayerCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.controller.utils.enums.GameModes;
import org.investovator.ui.authentication.Authenticator;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class UIConstants {

    //vadin UI views
    public static final String MAINVIEW = "main";
    public static final String AGENTVIEW = "agentView";
    public static final String DATAPLAY_USR_DASH = "dataPlaybackDashboard";
    public static final String NNVIEW = "nnView";
    public static final String NN_DASH_VIEW = "nnDashboard";
    public static final String AGENT_DASH_VIEW = "agentDashboard";
    public static final String DATA_PLAYBACK_ADMIN_DASH="dataPlaybackAdminDashboard";
    public static final String USER_VIEW="user";


    public static String getUserDashboardURL(Authenticator.UserType userType, GameModes mode){
        switch (mode){
            case AGENT_GAME:return AGENT_DASH_VIEW;
            case NN_GAME:return NN_DASH_VIEW;
            case PAYBACK_ENG:
                GameController controller= GameControllerImpl.getInstance();
                //todo - switch to IsMultiplayer command
                GetDataPlayerCommand command=new GetDataPlayerCommand();

                try {
                    controller.runCommand(Session.getCurrentGameInstance(),command);
                    boolean multiplayer=command.getPlayer().isMultiplayer();

                    //check whether this is a multiplayer game or not
                    if(multiplayer){
                        if(userType== Authenticator.UserType.ADMIN){
                            return DATA_PLAYBACK_ADMIN_DASH;
                        }
                        else{
                            return DATAPLAY_USR_DASH;
                        }
                    }
                    else{
                        return DATAPLAY_USR_DASH;
                    }
                } catch (CommandSettingsException e) {
                    e.printStackTrace();
                } catch (CommandExecutionException e) {
                    e.printStackTrace();
                }

                //if this is an admin
        }
        return null;
    }




}
