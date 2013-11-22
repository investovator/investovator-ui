package org.investovator.ui.authentication;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import org.investovator.MyVaadinUI;
import org.investovator.core.auth.DirectoryDAO;
import org.investovator.core.auth.DirectoryDAOImpl;
import org.investovator.core.auth.exceptions.AuthenticationException;
import org.investovator.core.auth.utils.LdapUtils;

import javax.jcr.SimpleCredentials;
import javax.swing.*;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    private static String userName="userName";
    private static String userType="userType";
    private static String isLoggedIn="loggedIn";


    private Authenticator(){
        setLoggedIn(false);
    }

    public static synchronized Authenticator getInstance()
    {
        if (authenticator == null)
            authenticator = new Authenticator();

        return authenticator;
    }

    public boolean isLoggedIn() {
        if(VaadinSession.getCurrent().getAttribute(isLoggedIn)!=null){
            boolean status= (boolean)VaadinSession.getCurrent().getAttribute(isLoggedIn);
            if(status){

                return true;
            }
        }
            return false;

    }

    public boolean authenticate(String username, String password) throws AuthenticationException{
        boolean success=false;

        //TODO: Test users, Remove after testing.
        if(username.isEmpty() && password.isEmpty()){
                //set the user as a standard user
                setUser("testUser1");
                setLoggedIn(true);
                return  true;
        }
        //user name for admin
        else if(username.equalsIgnoreCase("a")){
            setUser("admin");
            setLoggedIn(true);
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
            setUser(username);
            setUserType(type);
        }

        if(success){setLoggedIn(true);}

        return isLoggedIn();

    }

    public String getCurrentUser(){

        //TODO: implement after getting rajja's user API
        if((VaadinSession.getCurrent().getAttribute(userName))!=null){
            return VaadinSession.getCurrent().getAttribute(userName).toString();

        }
        else{
            return "";
        }
    }

    public UserType getMyPrivileges(){
        String user=getCurrentUser();
        if(user.equalsIgnoreCase("admin")){
            return UserType.ADMIN;
        }
        if(user.equalsIgnoreCase("testUser1")) {
            return UserType.ORDINARY;
        }

       return (UserType) VaadinSession.getCurrent().getAttribute(userType);
    }

    public void setUser(String user) {
        VaadinSession.getCurrent().setAttribute(userName,user);
    }

    public void setUserType(UserType type){
        VaadinSession.getCurrent().setAttribute(userType,type);
    }

    public void setLoggedIn(boolean loggedInStatus){
        VaadinSession.getCurrent().setAttribute(isLoggedIn,loggedInStatus);

    }

}
