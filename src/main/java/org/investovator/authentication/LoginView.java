package org.investovator.authentication;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class LoginView extends View
{
    private Button btnLogin = new Button("Login");
    private TextField login = new TextField( "Username");
    private PasswordField password = new PasswordField ( "Password");


    public LoginView ()
    {
        super("Authentication Required !");
        setName("login");
        initUI();
    }

    private void initUI ()
    {
        addComponent ( new Label("Please login in order to use the application") );
        addComponent ( new Label () );
        addComponent ( login );
        addComponent ( password );
        addComponent ( btnLogin );
    }
}
