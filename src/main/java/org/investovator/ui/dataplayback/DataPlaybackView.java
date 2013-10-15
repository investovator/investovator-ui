package org.investovator.ui.dataplayback;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.*;
import org.investovator.dataPlayBackEngine.DataPlayer;
import org.investovator.dataPlayBackEngine.events.StockEvent;
import org.investovator.ui.utils.dashboard.BasicDashboard;

import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @author: Ishan
 */


@SuppressWarnings("serial")
public class DataPlaybackView extends BasicDashboard implements Observer{

    //decides the number of points shown in the ticker chart
    private static int TICKER_CHART_LENGTH=10;

    DataPlayer player;
    //used for counting data iteration number
    int a=0;

    //to keep track of the number of points in the ticker chart
    int tickerChartPointCounter=0;

    DataPlaybackView mySelf;

    Chart tickerChart;



    public DataPlaybackView() {
        super("<span><center>investovator</center></span> Data Playback");
        mySelf=this;
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

                //add as an observer
                player.setObserver(mySelf);
            }
        });
        playGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ListSeries series=(ListSeries)mainChart.getConfiguration().getSeries().get(0);
                series.addData(player.getOHLCPrice("Goog","2012-10-3-19-45-"+Integer.toString(a)));
                a++;

                //start event playing
                player.runPlayback(1);
            }
        });


        stopGameButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                player.stopPlayback();
            }
        });




        Button nextDayB=new Button("Next day");
        nextDayB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ListSeries series=(ListSeries)mainChart.getConfiguration().getSeries().get(0);
                series.addData(player.getOHLCPrice("Goog","2012-10-3-19-45-"+Integer.toString(a)));
                a++;
            }
        });


        panelContent.addComponent(nextDayB);

        //add ticker chart
        panelContent.addComponent(buildTickerChart());

        panel.setContent(panelContent);

        return  panel;
    }

    private Chart buildMainChart(){
        Chart chart = new Chart();
        chart.setHeight("350px");
        chart.setWidth("90%");

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

        ListSeries ls = new ListSeries();
        ls.setName("GOOG");

        configuration.addSeries(ls);

        //disable trademark
        chart.getConfiguration().disableCredits();
        chart.getConfiguration().getTitle().setText("Stock Closing Prices");

        chart.drawChart(configuration);
        return chart;
    }

    private Chart buildTickerChart(){
        tickerChart = new Chart();
        tickerChart.setHeight("350px");
        tickerChart.setWidth("90%");

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

        DataSeries ls = new DataSeries();
        ls.setName("GOOG");
        ls.setPlotOptions(new PlotOptionsSpline());
        configuration.addSeries(ls);


        //disable trademark
        tickerChart.getConfiguration().disableCredits();

        tickerChart.getConfiguration().getTitle().setText("Real-time Stock Prices");


        tickerChart.drawChart(configuration);
        return tickerChart;
    }


    @Override
    public void update(Observable o, Object arg) {
        final StockEvent event=(StockEvent) arg;

        if (tickerChart.isConnectorEnabled()) {
            getSession().lock();
            try {
                //ListSeries series=(ListSeries)tickerChart.getConfiguration().getSeries().get(0);

                DataSeries series = (DataSeries) tickerChart.getConfiguration().getSeries().get(0);
//
                if (series.getData().size() > TICKER_CHART_LENGTH) {

//                                    series.addData(event.getPrice(),true,true);

                    series.add(new DataSeriesItem(event.getTime(), event.getPrice()), true, true);
                    System.out.println(event.getTime() + "==" + event.getPrice());

                } else {
//                                    series.addData(event.getPrice());
                    series.add(new DataSeriesItem(event.getTime(), event.getPrice()));
                    System.out.println(event.getTime() + "==" + event.getPrice());

                    //tickerChartPointCounter++;

                }
                tickerChart.setImmediate(true);
            } finally {
                getSession().unlock();
            }
        }
    }
}

