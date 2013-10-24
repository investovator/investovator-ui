package org.investovator.ui.authentication;

import org.investovator.MyVaadinUI;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Authenticator {

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
        return true;
    }

    private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean authenticate(String username, String password){

        ((MyVaadinUI)MyVaadinUI.getCurrent()).setUser(username);

        setLoggedIn(true);
        return isLoggedIn();

    }

    public String getCurrentUser(){

        //TODO: implement after getting rajja's user API
        return ((MyVaadinUI)MyVaadinUI.getCurrent()).getUser();
    }

}
