package org.investovator.ui.dataplayback;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
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
@Theme("mytheme")
public class DataPlaybackView extends GlobalView{
    Authenticator authenticator;

    CssLayout menu = new CssLayout();

    CssLayout root = new CssLayout();

    //CssLayout content = new CssLayout();

    VerticalLayout loginLayout;

    public DataPlaybackView(){
    }


    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to Data Playback Engine");


        this.setSizeFull();
        root.setSizeFull();
        addComponent(root);

        //////////////////////////////////////////////////////////////////////

        //setContent(root);
        root.addStyleName("root");
        root.setSizeFull();
        //root.setHeight("100%");


        //addComponent(menu);
        //setExpandRatio(menu, 1);

        TextField tf = new TextField("A TextField");
        TextField tf1 = new TextField("A TextField");
        TextField tf2 = new TextField("A TextField");
        //tf.setIcon(new ThemeResource("icons/user.png"));

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
                        // addComponent(new Image(null, new
                        // ThemeResource(
                        // "img/branding.png")));
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
        }) ;

        menu.addComponent(tf);
        menu.addComponent(tf1);
        menu.addComponent(tf2);

        String view="TESt";
        Button b = new NativeButton(view.substring(0, 1).toUpperCase()
                + view.substring(1).replace('-', ' '));
        b.addStyleName("icon-" + view);

        menu.addComponent(b);
        menu.addStyleName("no-vertical-drag-hints");
        menu.addStyleName("no-horizontal-drag-hints");




//

        menu.addStyleName("menu");
        menu.setHeight("100%");
       // root.addComponent(tf);

        addComponent(root);

        //////////////////////////////////////////////////////////////////////

//        buildLoginView(true);
//        buildMainView();
        //addComponent(getChart());




    }



    private void buildLoginView(boolean exit) {
        if (exit) {
            root.removeAllComponents();
        }
//        helpManager.closeAll();
//        HelpOverlay w = helpManager
//                .addOverlay(
//                        "Welcome to the Dashboard Demo Application",
//                        "<p>This application is not real, it only demonstrates an application built with the <a href=\"http://vaadin.com\">Vaadin framework</a>.</p><p>No username or password is required, just click the ‘Sign In’ button to continue. You can try out a random username and password, though.</p>",
//                        "login");
//        w.center();
//        addWindow(w);

        addStyleName("login");

        loginLayout = new VerticalLayout();
        loginLayout.setSizeFull();
        loginLayout.addStyleName("login-layout");
        root.addComponent(loginLayout);

        final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        loginPanel.addComponent(labels);

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label("QuickTickets Dashboard");
        title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Username");
        username.focus();
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Password");
        fields.addComponent(password);

        final Button signin = new Button("Sign In");
        signin.addStyleName("default");
        fields.addComponent(signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        final ShortcutListener enter = new ShortcutListener("Sign In",
                ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        };

        signin.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (username.getValue() != null
                        && username.getValue().equals("")
                        && password.getValue() != null
                        && password.getValue().equals("")) {
                    signin.removeShortcutListener(enter);
                    buildMainView();
                } else {
                    if (loginPanel.getComponentCount() > 2) {
                        // Remove the previous error message
                        loginPanel.removeComponent(loginPanel.getComponent(2));
                    }
                    // Add new error message
                    Label error = new Label(
                            "Wrong username or password. <span>Hint: try empty values</span>",
                            ContentMode.HTML);
                    error.addStyleName("error");
                    error.setSizeUndefined();
                    error.addStyleName("light");
                    // Add animation
                    error.addStyleName("v-animate-reveal");
                    loginPanel.addComponent(error);
                    username.focus();
                }
            }
        });

        signin.addShortcutListener(enter);

        loginPanel.addComponent(fields);

        loginLayout.addComponent(loginPanel);
        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    }

    private void buildMainView() {

//        nav = new Navigator(this, content);
//
//        for (String route : routes.keySet()) {
//            nav.addView(route, routes.get(route));
//        }
//
//        helpManager.closeAll();
        removeStyleName("login");
        root.removeComponent(loginLayout);

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
                                        "<span>QuickTickets</span> Dashboard",
                                        ContentMode.HTML);
                                logo.setSizeUndefined();
                                addComponent(logo);
                                // addComponent(new Image(null, new
                                // ThemeResource(
                                // "img/branding.png")));
                            }
                        });

                        // Main menu
                        addComponent(menu);
                        setExpandRatio(menu, 1);

                        // User menu
                        addComponent(new VerticalLayout() {
                            {
                                setSizeUndefined();
                                addStyleName("user");
                                Image profilePic = new Image(
                                        null,
                                        new ThemeResource("img/profile-pic.png"));
                                profilePic.setWidth("34px");
                                addComponent(profilePic);
                                Label userName = new Label("ss");
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
                                        buildLoginView(true);
                                    }
                                });
                            }
                        });
                    }
                });
                // Content
//                addComponent(content);
//                content.setSizeFull();
//                content.addStyleName("view-content");
//                setExpandRatio(content, 1);
            }

        });

        menu.removeAllComponents();

        for (final String view : new String[] { "dashboard", "sales",
                "transactions", "reports", "schedule" }) {
            Button b = new NativeButton(view.substring(0, 1).toUpperCase()
                    + view.substring(1).replace('-', ' '));
            b.addStyleName("icon-" + view);
//            b.addClickListener(new Button.ClickListener() {
//                @Override
//                public void buttonClick(Button.ClickEvent event) {
//                    clearMenuSelection();
//                    event.getButton().addStyleName("selected");
//                    if (!nav.getState().equals("/" + view))
//                        nav.navigateTo("/" + view);
//                }
//            });

            if (view.equals("reports")) {
                // Add drop target to reports button
                DragAndDropWrapper reports = new DragAndDropWrapper(b);
                reports.setDragStartMode(DragAndDropWrapper.DragStartMode.NONE);
//                reports.setDropHandler(new DropHandler() {
//
//                    @Override
//                    public void drop(DragAndDropEvent event) {
//                        clearMenuSelection();
//                        viewNameToMenuButton.get("/reports").addStyleName(
//                                "selected");
//                        autoCreateReport = true;
//                        items = event.getTransferable();
//                        nav.navigateTo("/reports");
//                    }
//
//                    @Override
//                    public AcceptCriterion getAcceptCriterion() {
//                        return AbstractSelect.AcceptItem.ALL;
//                    }
//
//                });
                menu.addComponent(reports);
                menu.addStyleName("no-vertical-drag-hints");
                menu.addStyleName("no-horizontal-drag-hints");
            } else {
                menu.addComponent(b);
            }

//            viewNameToMenuButton.put("/" + view, b);
        }
        menu.addStyleName("menu");
        menu.setHeight("100%");

//        viewNameToMenuButton.get("/dashboard").setHtmlContentAllowed(true);
//        viewNameToMenuButton.get("/dashboard").setCaption(
//                "Dashboard<span class=\"badge\">2</span>");

        String f = Page.getCurrent().getUriFragment();
        if (f != null && f.startsWith("!")) {
            f = f.substring(1);
        }
//        if (f == null || f.equals("") || f.equals("/")) {
//            nav.navigateTo("/dashboard");
//            menu.getComponent(0).addStyleName("selected");
//            helpManager.showHelpFor(DashboardView.class);
//        } else {
//            nav.navigateTo(f);
//            helpManager.showHelpFor(routes.get(f));
//            viewNameToMenuButton.get(f).addStyleName("selected");
//        }

//        nav.addViewChangeListener(new ViewChangeListener() {
//
//            @Override
//            public boolean beforeViewChange(ViewChangeEvent event) {
//                helpManager.closeAll();
//                return true;
//            }
//
//            @Override
//            public void afterViewChange(ViewChangeEvent event) {
//                View newView = event.getNewView();
//                helpManager.showHelpFor(newView);
//                if (autoCreateReport && newView instanceof ReportsView) {
//                    ((ReportsView) newView).autoCreate(2, items, transactions);
//                }
//                autoCreateReport = false;
//            }
//        });

    }

    private void clearMenuSelection() {
        for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                // Wow, this is ugly (even uglier than the rest of the code)
                ((DragAndDropWrapper) next).iterator().next()
                        .removeStyleName("selected");
            }
        }
    }


    protected Component getChart() {
        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();

        conf.setTitle(new Title("Total fruit consumtion, grouped by gender"));

        XAxis xAxis = new XAxis();
        xAxis.setCategories(new String[] { "Apples", "Oranges", "Pears",
                "Grapes", "Bananas" });
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setAllowDecimals(false);
        yAxis.setMin(0);
        yAxis.setTitle(new Title("Number of fruits"));
        conf.addyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("function() { return ''+ this.x +''"+ "+this.series.name +': '+ this.y +''+'Total: '+ this.point.stackTotal; }");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plotOptions = new PlotOptionsColumn();
        plotOptions.setStacking(Stacking.NORMAL);
        conf.setPlotOptions(plotOptions);

        ListSeries serie = new ListSeries("John",
                new Number[] { 5, 3, 4, 7, 2 });
        serie.setStack("male");
        conf.addSeries(serie);

        serie = new ListSeries("Joe", new Number[] { 3, 4, 4, 2, 5 });
        serie.setStack("male");
        conf.addSeries(serie);

        serie = new ListSeries("Jane", new Number[] { 2, 5, 6, 2, 1 });
        serie.setStack("female");
        conf.addSeries(serie);

        serie = new ListSeries("Janet", new Number[] { 3, 0, 4, 4, 3 });
        serie.setStack("female");
        conf.addSeries(serie);

        chart.drawChart(conf);

        return chart;
    }

}

