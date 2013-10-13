package org.investovator.ui.dataplayback;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.utils.dashboard.BasicDashboard;

import java.util.LinkedHashMap;

/**
 * @author: Ishan
 */


@SuppressWarnings("serial")
public class DataPlaybackView extends BasicDashboard {


    public DataPlaybackView() {
        super("<span><center>investovator</center></span> Data Playback");
    }

    /**
     * Create the views for the buttons as you desire and add them to the menuItems hash map
     * This is the only method a developer needs to change.
     */
     @Override
    public LinkedHashMap<String, Panel> getMenuItems() {
        LinkedHashMap<String,Panel> map=new LinkedHashMap<String, Panel>();

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


        VerticalLayout panelContent = new VerticalLayout();
        panelContent.addComponent(chart);

        Panel panel = new Panel();
        panel.setContent(panelContent);
        map.put("main view", panel);



        /*
        Example Button 2
         */
        VerticalLayout panelContent2 = new VerticalLayout();
        panelContent2.addComponent(new Button("Test 2"));

        Panel panel2 = new Panel();
        panel2.setContent(panelContent2);
        map.put("test 2", panel2);
         /*
        End of Example Button 2
         */

        return map;
    }
}

