package org.investovator.ui.utils.dashboard;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.ui.GlobalView;
import org.investovator.ui.utils.UIConstants;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 *
 *
 * This is a sample template for a dashboard. getMenuItems() is the only method a developer needs to override.
 *
 */
public abstract class BasicDashboard extends GlobalView {

    //contains the side bar
    private CssLayout menu;
    //contains the whole layout
    private CssLayout root;
    //contains the content in the right pane
    private CssLayout content;
    //dashboards name
    private String dashboardName;


    //used to store the buttons of the menu bar and their respective panels
    private LinkedHashMap<IconLoader, DashboardPanel> menuItems;

    public BasicDashboard(String name) {
        super();

        this.menu = new CssLayout();
        this.root = new CssLayout();
        this.content = new CssLayout();


        this.dashboardName=name;

    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        if(!authenticator.isLoggedIn()){
            getUI().getNavigator().navigateTo(UIConstants.LOGIN_VIEW);
            return;
        }

        this.menuItems = getMenuItems();
        //to make the dashboard take up the whole space of the browser
        this.setSizeFull();

        if(menuItems.isEmpty()){
            setupUI(viewChangeEvent);
            return;
        }

        content.removeAllComponents();
        setUpBasicDashboard(dashboardName);
        setupUI(viewChangeEvent);


        DashboardPanel addedPanel = menuItems.get(menuItems.keySet().iterator().next());
        addedPanel.addStyleName("selected");
        content.addComponent(addedPanel);
        addedPanel.onEnter();

    }


    /**
     * Guidelines - Create your components here. The keys you add to hash map
     * will be used as the text in the menu buttons.
     *
     * Also the icon names for those buttons should be in the form of "icon-<KEY_NAME>"
     * @return
     */
    public abstract LinkedHashMap<IconLoader, DashboardPanel> getMenuItems();

    /**
     * Draws the basic dashboard components such as menus, backgrounds, buttons
     */
    private void setUpBasicDashboard(final String name) {
        //to make the dashboard take up the whole space of the browser
        this.setSizeFull();

        //stop re adding components in case of a reload
        root.removeAllComponents();
        menu.removeAllComponents();

        root.setSizeFull();
        addComponent(root);


        root.addStyleName("root");
        root.setSizeFull();

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
                                        name,
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
                                addStyleName("user");
                                Image profilePic = new Image(
                                        null,
                                        new ThemeResource("img/profile-pic.png"));
                                profilePic.setWidth("34px");
                                addComponent(profilePic);
                                Label userName = new Label(authenticator.getCurrentUser());
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
                                        getUI().getNavigator().navigateTo(UIConstants.LOGIN_VIEW);
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

        //add the buttons to the menu bar
        for (final IconLoader item : menuItems.keySet()) {

            Button b = new NativeButton(IconLoader.getName(item));
            b.addStyleName(IconLoader.nameToIcon(item));

            //add the click listener
            b.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    //clear all the other selections
                    for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext(); ) {
                        Component next = it.next();
                        if (next instanceof NativeButton) {
                            next.removeStyleName("selected");
                        }
                    }
                    //mark as selected
                    clickEvent.getButton().addStyleName("selected");
                    //navigate to the view

                    content.removeAllComponents();
                    DashboardPanel addedPanel = menuItems.get(item);
                    content.addComponent(addedPanel);
                    addedPanel.onEnter();

                }
            });

            menu.addComponent(b);


            //select the first item in the menu initially
            if(menu.getComponentCount()==1){
                b.addStyleName("selected");
                //content.removeAllComponents();
                //DashboardPanel addedPanel =menuItems.get(b.getCaption().toLowerCase());
                //content.addComponent(addedPanel);
                //addedPanel.onEnter();
            }

        }


        menu.addStyleName("no-vertical-drag-hints");
        menu.addStyleName("no-horizontal-drag-hints");

        menu.addStyleName("menu");
        menu.setHeight("100%");

        addComponent(root);

    }

}
