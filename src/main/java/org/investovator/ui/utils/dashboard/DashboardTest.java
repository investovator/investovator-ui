package org.investovator.ui.utils.dashboard;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.util.LinkedHashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 *
 * Just a test class to test the dashboard inheritance
 *
 * TODO - delete this class, this is only a test
 */
public class DashboardTest extends BasicDashboard {

    public DashboardTest() {
        super("<span><center>investovator</center></span> Data Playback");
    }

    @Override
    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        System.out.println("Kewl!");
    }

    @Override
    public LinkedHashMap<String, DashboardPanel> getMenuItems() {
        LinkedHashMap<String, DashboardPanel> menu=new LinkedHashMap<String, DashboardPanel>();

        /*
        Example Button 1
         */

        //create the components you need
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSizeFull();
        Button but = new Button("Test 1");
        but.setSizeFull();
        panelContent.addComponent(but);
        panelContent.addComponent(new Button("Another button!"));

        DashboardPanel panel = new DashboardPanel() {
            @Override
            public void onEnter() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        panel.setSizeFull();

        //add everything to a panel
        panel.setContent(panelContent);

        //add the panel to the hash map
        menu.put("test 1", panel);
         /*
        End of Example Button 1
         */

        /*
        Example Button 2
         */
        VerticalLayout panelContent2 = new VerticalLayout();
        panelContent2.addComponent(new Button("Test 2"));

        DashboardPanel panel2 = new DashboardPanel() {
            @Override
            public void onEnter() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        panel2.setContent(panelContent2);
        menu.put("test 2", panel2);
         /*
        End of Example Button 2
         */

        return menu;


    }


}
