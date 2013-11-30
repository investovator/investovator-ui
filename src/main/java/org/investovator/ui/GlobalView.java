package org.investovator.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.UIConstants;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public abstract class GlobalView extends VerticalLayout implements View
{
    protected Authenticator authenticator;

    protected GlobalView() {
        authenticator = Authenticator.getInstance();
        this.setStyleName("main-view");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {


        if(!authenticator.isLoggedIn()){
            getUI().getNavigator().navigateTo(UIConstants.LOGIN_VIEW);
        }

        setupUI(viewChangeEvent);
    }

    public abstract void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent);

}

