package org.investovator.ui.utils;

import org.investovator.controller.utils.enums.GameModes;

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


    public static String getUserDashboardURL(GameModes mode){
        switch (mode){
            case AGENT_GAME:return AGENT_DASH_VIEW;
            case NN_GAME:return NN_DASH_VIEW;
            case PAYBACK_ENG:return DATAPLAY_USR_DASH;
        }
        return null;
    }


}
