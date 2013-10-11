package org.investovator.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.authentication.Authenticator;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public abstract class GlobalView extends VerticalLayout implements View
{
    protected Authenticator authenticator;


    public GlobalView(){
        this.setWidth(500,Unit.PIXELS);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        authenticator = Authenticator.getInstance();

        if(!authenticator.isLoggedIn()){
            getUI().getNavigator().navigateTo("");
        }

        setupUI(viewChangeEvent);
    }

    public abstract void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent);

}

