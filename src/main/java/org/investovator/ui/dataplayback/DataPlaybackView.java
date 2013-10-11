package org.investovator.ui.dataplayback;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.ui.GlobalView;
import org.investovator.ui.authentication.Authenticator;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class DataPlaybackView extends GlobalView {
    Authenticator authenticator;

    CssLayout menu = new CssLayout();

    CssLayout root = new CssLayout();


    VerticalLayout loginLayout;

    public DataPlaybackView() {
    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to Data Playback Engine");


        //to make the dashboard take up the whole space of the browser
        this.setSizeFull();

        root.setSizeFull();
        addComponent(root);


        root.addStyleName("root");
        root.setSizeFull();


        TextField tf = new TextField("A TextField");
        TextField tf1 = new TextField("A TextField");
        TextField tf2 = new TextField("A TextField");
//        tf.setIcon(new ThemeResource("icons/user.png"));

        addStyleName("main-view");

        root.addComponent(new VerticalLayout() {
            // Sidebar
            {
                addStyleName("sidebar");
                setWidth(null);
                setHeight("100%");

                // Branding element
                addComponent(new CssLayout() {
                    {
                        addStyleName("branding");
                        Label logo = new Label(
                                "<span>QuickTickets</span> Dashboard",
                                ContentMode.HTML);
                        logo.setSizeUndefined();
                        addComponent(logo);
//                        addComponent(new Image(null, new
//                                ThemeResource(
//                                "img/branding.png")));
                    }
                });

                // Main menu
                addComponent(menu);
                setExpandRatio(menu, 1);

                // User menu
                addComponent(new VerticalLayout() {
                    {
//                        setSizeUndefined();
                        addStyleName("user");
                        Image profilePic = new Image(
                                null,
                                new ThemeResource("img/profile-pic.png"));
                        profilePic.setWidth("34px");
                        addComponent(profilePic);
                        Label userName = new Label("hh");
                        userName.setSizeUndefined();
                        addComponent(userName);

//                        MenuBar.Command cmd = new MenuBar.Command() {
//                            @Override
//                            public void menuSelected(
//                                    MenuBar.MenuItem selectedItem) {
//                                Notification
//                                        .show("Not implemented in this demo");
//                            }
//                        };
//                        MenuBar settings = new MenuBar();
//                        MenuBar.MenuItem settingsMenu = settings.addItem("",
//                                null);
//                        settingsMenu.setStyleName("icon-cog");
//                        settingsMenu.addItem("Settings", cmd);
//                        settingsMenu.addItem("Preferences", cmd);
//                        settingsMenu.addSeparator();
//                        settingsMenu.addItem("My Account", cmd);
//                        addComponent(settings);

//                        Button exit = new NativeButton("Exit");
//                        exit.addStyleName("icon-cancel");
//                        exit.setDescription("Sign Out");
//                        addComponent(exit);
//                        exit.addClickListener(new Button.ClickListener() {
//                            @Override
//                            public void buttonClick(Button.ClickEvent event) {
//                                buildLoginView(true);
//                            }
//                        });
                    }
                });
            }
        });

        menu.addComponent(tf);
        menu.addComponent(tf1);
        menu.addComponent(tf2);

        String view = "TESt";
        Button b = new NativeButton(view.substring(0, 1).toUpperCase()
                + view.substring(1).replace('-', ' '));
        b.addStyleName("icon-" + view);

        menu.addComponent(b);
        menu.addStyleName("no-vertical-drag-hints");
        menu.addStyleName("no-horizontal-drag-hints");


//

        menu.addStyleName("menu");
        menu.setHeight("100%");

        addComponent(root);


//


    }


}

