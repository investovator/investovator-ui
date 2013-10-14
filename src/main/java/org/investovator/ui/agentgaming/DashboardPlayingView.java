package org.investovator.ui.agentgaming;

import com.google.gwt.aria.client.TimerRole;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.ChartClickEvent;
import com.vaadin.addon.charts.ChartClickListener;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.ui.*;
import net.sourceforge.jabm.report.Report;
import net.sourceforge.jasa.agent.valuation.GeometricBrownianMotionPriceProcess;
import net.sourceforge.jasa.report.CurrentPriceReportVariables;
import org.investovator.jasa.api.JASAFacade;
import org.investovator.jasa.api.MarketFacade;
import org.investovator.jasa.multiasset.simulation.HeadlessMultiAssetSimulationManager;
import org.springframework.core.env.Environment;


import java.util.*;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DashboardPlayingView extends Panel {

    VerticalLayout content;

   MarketFacade simulationFacade = JASAFacade.getMarketFacade();

    HashMap<String,ArrayList<Report>>  reports;

    String mainXmlPath;

    boolean simulationRunning = false;


    public DashboardPlayingView() {
        content = new VerticalLayout();

        Button test = new Button("Start");
        Button stop  = new Button("Stop");

        test.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //testing

                System.out.println(System.getProperty("jabm.config"));

                simulationFacade = JASAFacade.getMarketFacade();
                simulationFacade.startSimulation();
                simulationRunning=true;

                viewReport();
                reportsReady = true;
            }
        });

        stop.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                simulationFacade.terminateSimulation();
            }
        });



        content.addComponent(test);
        content.addComponent(stop);
        content.addComponent(getChart());

        content.setSizeFull();

        this.setSizeFull();

        this.setContent(content);


    }


    boolean reportsReady = false;

    GeometricBrownianMotionPriceProcess googGBM =null;

    public void viewReport(){

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reports = simulationFacade.getReports();
        if(reports == null) return;

        ArrayList<Report> goog = reports.get("GOOG");



        for (int i = 0; i < goog.size(); i++) {
            Report tmp = goog.get(i);
            if(tmp.getName().equals("GBM")){
                googGBM = (GeometricBrownianMotionPriceProcess) tmp;
            }
        }

    }




    int lastIndex = 0;

 /*   public int[] getValues(){

        List<Integer> values = new ArrayList<Integer>();

        if(googGBM==null){
            viewReport();
            int[] tmp = {};
            return tmp;
        }

        else{

            int count = lastIndex;

            while(true){

                googGBM.get
                Number num = googGBM.getY(count);
                if(num==null) break;

                values.add(num.intValue());
                lastIndex++;

            }
        }

        int[] result = new int[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i);
        }

        return result;
    }*/


    public int getValue(){

        //if(googGBM==null){
         //   viewReport();
         //   return 0;
        //}

        //else{
         return googGBM.getY(0).intValue();
       // }
    }


    final ListSeries series = new ListSeries(0);

    protected Component getChart() {

        final Chart chart = new Chart();
        chart.setHeight("450px");
        chart.setWidth("75%");

        final Configuration configuration = new Configuration();

        configuration.getChart().setType(ChartType.SPLINE);


        configuration.setSeries(series);

        chart.drawChart(configuration);


        Thread randomDataGenerator = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);

                        if(!reportsReady) continue;

                        if (chart.isConnectorEnabled()) {
                            getSession().lock();
                            try {
                                series.addData(getValue());
                            } finally {
                                getSession().unlock();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        randomDataGenerator.start();


        return chart;
    }





}



