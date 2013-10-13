package org.investovator.ui.authentication;

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

    public boolean authenticate(){

        setLoggedIn(true);
        return isLoggedIn();

    }

}
