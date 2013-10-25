package org.investovator.ui.authentication;

import org.investovator.MyVaadinUI;

import javax.swing.*;

/**
 *
 * @author: Hasala Surasinghe
 * @author : ishan
 * @author : amila
 * @version: ${Revision}
 *
 */
public class Authenticator {

    public enum UserType{
        ADMIN,
        ORDINARY
    }

    private static Authenticator authenticator;
    private boolean loggedIn;

    private Authenticator(){
        loggedIn = false;
    }

    public static synchronized Authenticator getInstance()
    {
        if (authenticator == null)
            authenticator = new Authenticator();

        return authenticator;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean authenticate(String username, String password){
        boolean success=false;

        if(username.isEmpty()){
            if(password.isEmpty()){
                //set the user as a standard user
                ((MyVaadinUI)MyVaadinUI.getCurrent()).setUser("testUser1");
                success=true;

            }

//            ((MyVaadinUI)MyVaadinUI.getCurrent()).setUser("");
//            success=true;


        }
        //user name for admin
        else if(username.equalsIgnoreCase("a")){

            ((MyVaadinUI)MyVaadinUI.getCurrent()).setUser("admin");
            success=true;
        }

        if(success){

            setLoggedIn(true);
        }

        return isLoggedIn();

    }

    public String getCurrentUser(){

        //TODO: implement after getting rajja's user API
        return ((MyVaadinUI)MyVaadinUI.getCurrent()).getUser();
    }

    public UserType getMyPrivileges(){
        String user=getCurrentUser();
        if(user.equalsIgnoreCase("admin")){
            return UserType.ADMIN;
        }
        else {
            return UserType.ORDINARY;
        }

    }

}
