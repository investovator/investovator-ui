package org.investovator.ui.authentication;

import org.investovator.core.auth.DirectoryDAO;
import org.investovator.core.auth.DirectoryDAOImpl;
import org.investovator.core.auth.exceptions.AuthenticationException;
import org.investovator.ui.utils.Session;

import javax.jcr.SimpleCredentials;
import java.util.HashMap;

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




    private Authenticator(){
        Session.setLoggedIn(false);
    }

    public static synchronized Authenticator getInstance()
    {
        if (authenticator == null)
            authenticator = new Authenticator();

        return authenticator;
    }



    public boolean authenticate(String username, String password) throws AuthenticationException{
        boolean success=false;

        //TODO: Test users, Remove after testing.
        if(username.isEmpty() && password.isEmpty()){
                //set the user as a standard user
                Session.setUser("testUser1");
                Session.setLoggedIn(true);
                return  true;
        }
        //user name for admin
        else if(username.equalsIgnoreCase("a")){
            Session.setUser("admin");
            Session.setLoggedIn(true);
            return true;
        }
        //End of Test code


        if(username.isEmpty() || password.isEmpty()){
            return false;
        }

        SimpleCredentials credentials = new SimpleCredentials(username, password.toCharArray());
        DirectoryDAO directoryDAO = new DirectoryDAOImpl();

        HashMap<Object, Object> userData = directoryDAO.bindUser(credentials);
        if(userData!= null) {
            success = true;
            UserType type  = (boolean)userData.get(DirectoryDAO.UserRole.ADMIN) ? UserType.ADMIN  :UserType.ORDINARY;
            Session.setUser(username);
            Session.setUserType(type);
        }

        if(success){Session.setLoggedIn(true);}

        return Session.isLoggedIn();

    }


    public String getCurrentUser(){
        return Session.getCurrentUser();
    }

    public boolean isLoggedIn(){
        return Session.isLoggedIn();
    }

    public UserType getMyPrivileges(){
        return Session.getMyPrivileges();
    }
}
