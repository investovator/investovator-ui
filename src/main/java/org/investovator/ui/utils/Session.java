package org.investovator.ui.utils;

import com.vaadin.server.VaadinSession;
import org.investovator.ui.authentication.Authenticator;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class Session {

    private static String userName="userName";
    private static String userType="userType";
    private static String isLoggedIn="loggedIn";
    private static String instance="instance";


    public static void setCurrentGameInstance(String gameInstance){
        VaadinSession.getCurrent().setAttribute(instance,gameInstance);
    }

    public static String getCurrentGameInstance(){
        return VaadinSession.getCurrent().getAttribute(instance).toString();
    }

    public static String getCurrentUser(){

        if((VaadinSession.getCurrent().getAttribute(userName))!=null){
            return VaadinSession.getCurrent().getAttribute(userName).toString();

        }
        else{
            return "";
        }
    }

    public static Authenticator.UserType getMyPrivileges(){
        String user=getCurrentUser();
        if(user.equalsIgnoreCase("admin")){
            return Authenticator.UserType.ADMIN;
        }
        if(user.equalsIgnoreCase("testUser1")) {
            return Authenticator.UserType.ORDINARY;
        }

        return (Authenticator.UserType) VaadinSession.getCurrent().getAttribute(userType);
    }

    public static void setUser(String user) {
        VaadinSession.getCurrent().setAttribute(userName,user);
    }

    public static void setUserType(Authenticator.UserType type){
        VaadinSession.getCurrent().setAttribute(userType,type);
    }

    public static void setLoggedIn(boolean loggedInStatus){
        VaadinSession.getCurrent().setAttribute(isLoggedIn,loggedInStatus);

    }

    public static boolean isLoggedIn() {
        if(VaadinSession.getCurrent().getAttribute(isLoggedIn)!=null){
            boolean status= (boolean)VaadinSession.getCurrent().getAttribute(isLoggedIn);
            if(status){

                return true;
            }
        }
        return false;

    }

}
