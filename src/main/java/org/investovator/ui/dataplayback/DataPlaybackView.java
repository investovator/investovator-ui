package org.investovator.ui.dataplayback;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.investovator.ui.GlobalView;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author: Ishan Thilina
 */


@SuppressWarnings("serial")
public class DataPlaybackView extends GlobalView {

    //contains the side bar
    CssLayout menu;
    //contains the whole layout
    CssLayout root;
    //contains the content in the right pane
    CssLayout content;

    //used to store the buttons of the menu bar and their respective panels
    LinkedHashMap<String, Panel> menuItems;

    public DataPlaybackView() {
        this.menu = new CssLayout();
        this.root = new CssLayout();
        this.content = new CssLayout();
        this.menuItems = new LinkedHashMap<String, Panel>();
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to Data Playback Engine");

        setUpButtons();
        setUpBasicDashboard();

    }

    /**
     * Draws the basic dashboard componentes such as menus, backgrounds, buttons
     */
    public void setUpBasicDashboard() {
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
                                        getUI().getNavigator().navigateTo("");
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
        for (final String item : menuItems.keySet()) {

            Button b = new NativeButton(item.substring(0, 1).toUpperCase()
                    + item.substring(1).replace('-', ' '));
            b.addStyleName("icon-sales");

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
                    content.addComponent(menuItems.get(clickEvent.getButton().getCaption().toLowerCase()));

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

    /**
     * Create the views for the buttons as you desire and add them to the menuItems hash map
     * This is the only method a developer needs to change.
     */
    public void setUpButtons() {

        Chart chart = new Chart();
        chart.setHeight("450px");
        chart.setWidth("100%");

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        tooltip.setHeaderFormat("{point.key}");
        tooltip.setPointFormat("");
        tooltip.setFooterFormat("{series.name}: 	{point.y} EUR");

        Configuration configuration = new Configuration();
        configuration.setTooltip(tooltip);

        configuration.getxAxis().setCategories("Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        ListSeries ls = new ListSeries();
        ls.setName("Short");
        ls.setData(29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,
                194.1, 95.6, 54.4);
        configuration.addSeries(ls);
        ls = new ListSeries();
        ls.setName("Long named series");
        Number[] data = new Number[] { 129.9, 171.5, 106.4, 129.2, 144.0,
                176.0, 135.6, 148.5, 216.4, 194.1, 195.6, 154.4 };
        for (int i = 0; i < data.length / 2; i++) {
            Number number = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = number;
        }

        ls.setData(data);
        configuration.addSeries(ls);

        chart.drawChart(configuration);


        /*
        Example Button 2
         */
        VerticalLayout panelContent2 = new VerticalLayout();
        panelContent2.addComponent(chart);

        Panel panel2 = new Panel();
        panel2.setContent(panelContent2);
        menuItems.put("test 2", panel2);
         /*
        End of Example Button 1
         */
    }


}

