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
import org.investovator.ui.agentgaming.AgentGamingView;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.authentication.LoginView;
import org.investovator.ui.main.MainGamingView;
import org.investovator.ui.nngaming.NNGamingView;

import java.util.HashMap;
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

    CssLayout content = new CssLayout();


    Navigator nav;

    HashMap<String, Class<? extends View>> menuItems = new HashMap<String, Class<? extends View>>() {
        {
            put("agent games", AgentGamingView.class);
            put("ann games", NNGamingView.class);
//            put("/transactions", TransactionsView.class);
//            put("/reports", ReportsView.class);
//            put("/schedule", ScheduleView.class);
        }
    };



    public DataPlaybackView() {
    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to Data Playback Engine");

        nav=new Navigator(getParentUI(),content);
        //nav.addView("", MainGamingView.class);


        for (String item : menuItems.keySet()) {
            nav.addView(item, menuItems.get(item));
        }

        //add the exit link manually
        nav.addView("exit", LoginView.class);


        setUpBasicDashboard();
    }

    /**
     * Draws the basic dashboard componentes such as menus, backgrounds, buttons
     */
    public void setUpBasicDashboard(){
        //to make the dashboard take up the whole space of the browser
        this.setSizeFull();

        //stop re adding components in case of a reload
        root.removeAllComponents();
        menu.removeAllComponents();

        root.setSizeFull();
        addComponent(root);


        root.addStyleName("root");
        root.setSizeFull();


//        tf.setIcon(new ThemeResource("icons/user.png"));

        //addStyleName("main-view");

        root.addComponent(new HorizontalLayout() {
            {
                setSizeFull();
                addStyleName("main-view");
                addComponent(new VerticalLayout() {
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
                                        "<span><center>investovator</center></span> Data Playback",
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
                                Label userName = new Label("User");
                                userName.setSizeUndefined();
                                addComponent(userName);

                                MenuBar.Command cmd = new MenuBar.Command() {
                                    @Override
                                    public void menuSelected(
                                            MenuBar.MenuItem selectedItem) {
                                        Notification
                                                .show("Not implemented in this demo");
                                    }
                                };
                                MenuBar settings = new MenuBar();
                                MenuBar.MenuItem settingsMenu = settings.addItem("",
                                        null);
                                settingsMenu.setStyleName("icon-cog");
                                settingsMenu.addItem("Settings", cmd);
                                settingsMenu.addItem("Preferences", cmd);
                                settingsMenu.addSeparator();
                                settingsMenu.addItem("My Account", cmd);
                                addComponent(settings);

                                Button exit = new NativeButton("Exit");
                                exit.addStyleName("icon-cancel");
                                exit.setDescription("Sign Out");
                                addComponent(exit);
                                exit.addClickListener(new Button.ClickListener() {
                                    @Override
                                    public void buttonClick(Button.ClickEvent event) {
                                        getUI().getNavigator().navigateTo("exit");
                                    }
                                });
                            }
                        });
                    }
                });

                // Content
                addComponent(content);
                content.setSizeFull();
                content.addStyleName("view-content");
                setExpandRatio(content, 1);
            }
        });


        //String[] items = {"link1", "link2", "link3"};
        for (final String item : menuItems.keySet()) {
            Button b = new NativeButton(item.substring(0, 1).toUpperCase()
                    + item.substring(1).replace('-', ' '));
            //b.addStyleName("icon-" + item);

            //add the click listener
            b.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    //clear all the other selections
                    for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
                        Component next = it.next();
                        if (next instanceof NativeButton) {
                            next.removeStyleName("selected");
                        }
                    }
                    //mark as selected
                    clickEvent.getButton().addStyleName("selected");

                    if (!nav.getState().equals(item)){
                        nav.navigateTo(item);
                }
                }
            });

            menu.addComponent(b);

        }


        menu.addStyleName("no-vertical-drag-hints");
        menu.addStyleName("no-horizontal-drag-hints");

        menu.addStyleName("menu");
        menu.setHeight("100%");

        addComponent(root);

    }


}

