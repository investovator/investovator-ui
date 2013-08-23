package org.investovator.utils;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ListSeries;
import net.sourceforge.jabm.report.CombiSeriesReportVariables;
import net.sourceforge.jabm.spring.BeanFactorySingleton;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class ChartUpdater implements Runnable {
    int count =1;
    Chart chart;

    public ChartUpdater(Chart chart) {
        this.chart = chart;
    }

    @Override
    public void run() {
        CombiSeriesReportVariables report = (CombiSeriesReportVariables) BeanFactorySingleton.getBean("priceTimeSeriesIBM");
        ListSeries ls = (ListSeries)chart.getConfiguration().getSeries().get(0);
        ls.setName("IBM");
        while (true){
            //if(count<report.size(0)){
            for(int i=count-1;i<report.size(0);i++){
                ls.addData(report.getY(0,i));
                System.out.println(count);
                count++;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //}

        }
    }
}
