package org.investovator.ui.authentication;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.investovator.ui.utils.UIConstants;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */

    @SuppressWarnings("serial")
    public class LoginView extends VerticalLayout implements View {
        Authenticator authenticator;

        public LoginView() {
            authenticator = Authenticator.getInstance();
            init();
        }

        private void init(){
            final Label userName = new Label("User Name");
            final Label password = new Label("Password");
            final com.vaadin.ui.TextField userField = new com.vaadin.ui.TextField();
            final PasswordField pwdField = new PasswordField();

            FormLayout layout = new FormLayout();

            Button loginButton = new Button("Login",
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            boolean loginStatus = authenticator.authenticate();
                            if(loginStatus){
                                getUI().getNavigator().navigateTo(UIConstants.MAINVIEW);
                            }
                            else{
                                Notification.show("Incorrect UserName or Password");
                            }
                        }
                    });
            layout.addComponents(userName,userField,password,pwdField,loginButton);
            addComponent(layout);

        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            Notification.show("Welcome to Investovator");
        }
    }