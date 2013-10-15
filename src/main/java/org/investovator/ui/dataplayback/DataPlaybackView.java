package org.investovator.ui.dataplayback;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.*;
import org.investovator.dataPlayBackEngine.DataPlayer;
import org.investovator.ui.utils.dashboard.BasicDashboard;

import java.util.LinkedHashMap;

/**
 * @author: Ishan
 */


@SuppressWarnings("serial")
public class DataPlaybackView extends BasicDashboard {

    DataPlayer player;
    //used for counting data iteration number
    int a=0;



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

        map.put("main view", buildMainPanel());



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


    private Panel buildMainPanel(){
        Panel panel = new Panel();

        //add the main graph
        VerticalLayout panelContent = new VerticalLayout();

        HorizontalLayout topBar=new HorizontalLayout();
        HorizontalLayout topButtonContainer=new HorizontalLayout();
        topButtonContainer.setStyleName("sidebar");
        topBar.addComponent(topButtonContainer);
        //to set the alignment of the buttons
        topBar.setComponentAlignment(topButtonContainer,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setSizeFull();

        panelContent.addComponent(topBar);
        final Chart mainChart=buildMainChart();
        panelContent.addComponent(mainChart);

        //create the buttons
        Button addGameButton=new Button("New Game");
        Button playGameButton=new Button("Play Game");
        Button pauseGameButton=new Button("Pause Game");
        Button stopGameButton=new Button("Stop Game");
        topButtonContainer.addComponent(addGameButton);
        topButtonContainer.addComponent(playGameButton);
        topButtonContainer.addComponent(pauseGameButton);
        topButtonContainer.addComponent(stopGameButton);

        //set alignments of the buttons
        topButtonContainer.setComponentAlignment(addGameButton,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(playGameButton,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(pauseGameButton,Alignment.MIDDLE_RIGHT);
        topButtonContainer.setComponentAlignment(stopGameButton,Alignment.MIDDLE_RIGHT);
//        topButtonContainer.setSpacing(false);

        //add the action listeners for buttons
        addGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //TODO- fix this static value
                String[] stocks=new String[2];
                stocks[0]="GOOG";
                stocks[1]="APPL";
                player=new DataPlayer(stocks);
            }
        });
        playGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
//                Notification.show(Float.toString(player.getOHLCPrice("Goog","2012-10-3-19-45-3"+Integer.toString(a))));
                ListSeries series=(ListSeries)mainChart.getConfiguration().getSeries().get(0);
                series.addData(player.getOHLCPrice("Goog","2012-10-3-19-45-3"+Integer.toString(a)));
                a++;
            }
        });





        Button nextDayB=new Button("Next day");
        nextDayB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //Notification.show(Float.toString(player.getOHLCPrice("Goog","2012-10-3-19-45-3"+Integer.toString(a))));
                ListSeries series=(ListSeries)mainChart.getConfiguration().getSeries().get(0);
                series.addData(player.getOHLCPrice("Goog","2012-10-3-19-45-3"+Integer.toString(a)));
                a++;
            }
        });


        panelContent.addComponent(nextDayB);
//        panelContent.addComponent(new Button("dd"));
        //

        panel.setContent(panelContent);

        return  panel;
    }

    private Chart buildMainChart(){
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
        configuration.getChart().setType(ChartType.SPLINE);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        plotOptions.setEnableMouseTracking(false);
        configuration.setPlotOptions(plotOptions);

//        configuration.getxAxis().setCategories("Jan", "Feb", "Mar", "Apr",
//                "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        ListSeries ls = new ListSeries();
//        ls.setName("Short");
//        ls.setData(29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,
//                194.1, 95.6, 54.4);
//        configuration.addSeries(ls);
//        ls = new ListSeries();
        ls.setName("GOOG");
//        Number[] data = new Number[] { 129.9, 171.5, 106.4, 129.2, 144.0,
//                176.0, 135.6, 148.5, 216.4, 194.1, 195.6, 154.4 };
//        for (int i = 0; i < data.length / 2; i++) {
//            Number number = data[i];
//            data[i] = data[data.length - i - 1];
//            data[data.length - i - 1] = number;
//        }

//        ls.setData(data);
        configuration.addSeries(ls);

        //disable trademark
        chart.getConfiguration().disableCredits();

        chart.drawChart(configuration);
        return chart;
    }
}

