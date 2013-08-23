package org.investovator;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
//import org.dussan.vaadin.dcharts.DCharts;
//import org.dussan.vaadin.dcharts.base.elements.XYaxis;
//import org.dussan.vaadin.dcharts.data.DataSeries;
//import org.dussan.vaadin.dcharts.data.Ticks;
//import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
//import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
//import org.dussan.vaadin.dcharts.options.Axes;
//import org.dussan.vaadin.dcharts.options.Highlighter;
//import org.dussan.vaadin.dcharts.options.Options;
//import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import net.sourceforge.jabm.report.CombiSeriesReportVariables;
import net.sourceforge.jabm.report.SimEventReport;
import net.sourceforge.jabm.spring.BeanFactorySingleton;
import org.investovator.utils.ChartUpdater;
import org.investovator.utils.MainClassRunner;
import org.springframework.beans.factory.BeanFactory;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "org.investovator.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        Chart chart=getBasicChart();

        Button button = new Button("Click Me to run JASA");
        button.addClickListener(new JasaRunner(chart, layout));
        layout.addComponent(button);



//        Button button1 = new Button("drawGraph");
//        button1.addClickListener(new Button.ClickListener() {
//            public void buttonClick(ClickEvent event) {
//                chart=getBasicChart(chart);
//                 layout.addComponent();
//
//            }
//        });
//        layout.addComponent(button1);

        Button button2 = new Button("updateGraph");
        button2.addClickListener(new TableUpdater(chart));
        layout.addComponent(button2);

        layout.addComponent(chart);


    }

    @Override
    public String getDescription() {
        return "Bart with negative stack";
    }

    protected Chart getBasicChart(){
        Chart chart = new Chart();
        chart.setHeight("450px");
        chart.setWidth("100%");

        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getChart().setMarginRight(130);
        configuration.getChart().setMarginBottom(25);

        configuration.getTitle().setText("Stock prices of IBM");
        configuration.getSubTitle().setText("Source: JASA simulation-1");

//        configuration.getxAxis().setCategories("Jan", "Feb", "Mar", "Apr",
//                "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        Axis yAxis = configuration.getyAxis();
        yAxis.setMin(-5d);
        yAxis.setTitle(new Title("Prices"));
        yAxis.getTitle().setVerticalAlign(VerticalAlign.HIGH);

        configuration
                .getTooltip()
                .setFormatter(
                        "''+ this.series.name +''+this.x +': '+ this.y +'°C'");

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        //plotOptions.setDataLabels(new Labels(true));
        configuration.setPlotOptions(plotOptions);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setHorizontalAlign(HorizontalAlign.RIGHT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(-10d);
        legend.setY(100d);
        legend.setBorderWidth(0);

        ListSeries ls = new ListSeries();
        ls.setName("IBM");
        //ls.getPlotOptions().setPointStart(1959);

        //CombiSeriesReportVariables report = (CombiSeriesReportVariables)BeanFactorySingleton.getBean("priceTimeSeriesIBM");
        for(int i=0;i<10;i++){
//            System.out.println(report.getX(0,i).toString());
//            layout.addComponent(new Label(report.getY(0,i).toString()));
            ls.addData(i);
        }

//        ls.setData(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3,
//                13.9, 9.6);
        configuration.addSeries(ls);
//        ls = new ListSeries();
//        ls.setName("New York");
//        ls.setData(-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1,
//                8.6, 2.5);
//        configuration.addSeries(ls);
//        ls = new ListSeries();
//        ls.setName("Berlin");
//        ls.setData(-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9,
//                1.0);
//        configuration.addSeries(ls);
//        ls = new ListSeries();
//        ls.setName("London");
//        ls.setData(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6,
//                4.8);
//        configuration.addSeries(ls);

        chart.drawChart(configuration);
        System.out.println("Chart creatred");
        return chart;
    }

    public int updateTable(Chart chart, int count){
        System.out.println("called");

        ListSeries ls = (ListSeries)chart.getConfiguration().getSeries().get(0);
        ls.setName("IBM");

        //ls.getPlotOptions().setPointStart(1959);

        CombiSeriesReportVariables report = (CombiSeriesReportVariables)BeanFactorySingleton.getBean("priceTimeSeriesIBM");
        for(int i=count;i<report.size(0);i++){
//            System.out.println(report.getX(0,i).toString());
//            layout.addComponent(new Label(report.getY(0,i).toString()));
            ls.addData(report.getY(0,i));
            count++;
            //report.setSeriesList();
            //System.out.println(report.getY(0,i));
        }

        return count;

//        Configuration configuration=chart.getConfiguration();
//        configuration.addSeries(ls);
//        chart.drawChart(configuration);
//        chart.


    }

//    @Override
    protected Component getChart() {
        Chart chart = new Chart(ChartType.BAR);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("Population pyramid for Germany, midyear 2010");
        conf.setSubTitle("Source: www.census.gov");

        final String[] categories = new String[] { "0-4", "5-9", "10-14",
                "15-19", "20-24", "25-29", "30-34", "35-39", "40-44", "45-49",
                "50-54", "55-59", "60-64", "65-69", "70-74", "75-79", "80-84",
                "85-89", "90-94", "95-99", "100 +" };

        XAxis x1 = new XAxis();
        conf.addxAxis(x1);
        x1.setCategories(categories);
        x1.setReversed(false);

        XAxis x2 = new XAxis();
        conf.addxAxis(x2);
        x2.setCategories(categories);
        x2.setOpposite(true);
        x2.setReversed(false);
        x2.setLinkedTo(x1);

        YAxis y = new YAxis();
        y.setMin(-4000000);
        y.setMax(4000000);
        y.setTitle(new Title(""));
        conf.addyAxis(y);

        PlotOptionsSeries plot = new PlotOptionsSeries();
        plot.setStacking(Stacking.NORMAL);
        conf.setPlotOptions(plot);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("''+ this.series.name +', age '+ this.point.category +''+ 'Population: '+ Highcharts.numberFormat(Math.abs(this.point.y), 0)");
        conf.setTooltip(tooltip);

        conf.addSeries(new ListSeries("Male", -1746181, -1884428, -2089758,
                -2222362, -2537431, -2507081, -2443179, -2664537, -3556505,
                -3680231, -3143062, -2721122, -2229181, -2227768, -2176300,
                -1329968, -836804, -354784, -90569, -28367, -3878));
        conf.addSeries(new ListSeries("Female", 1656154, 1787564, 1981671,
                2108575, 2403438, 2366003, 2301402, 2519874, 3360596, 3493473,
                3050775, 2759560, 2304444, 2426504, 2568938, 1785638, 1447162,
                1005011, 330870, 130632, 21208));

        chart.drawChart(conf);

        return chart;
    }

    protected void graph(Layout layout){
        System.out.println("Called");
//        DataSeries dataSeries = new DataSeries()
//                .add(2, 6, 7, 10);
//
//
//        SeriesDefaults seriesDefaults = new SeriesDefaults()
//                .setRenderer(SeriesRenderers.BAR);
//
//        Axes axes = new Axes()
//                .addAxis(
//                        new XYaxis()
//                                .setRenderer(AxisRenderers.CATEGORY)
//                                .setTicks(
//                                        new Ticks()
//                                                .add("a", "b", "c", "d")));
//
//        Highlighter highlighter = new Highlighter()
//                .setShow(false);
//
//        Options options = new Options()
//                .setSeriesDefaults(seriesDefaults)
//                .setAxes(axes)
//                .setHighlighter(highlighter);
//
//        DCharts chart = new DCharts()
//                .setDataSeries(dataSeries)
//                .setOptions(options)
//                .show();
//        layout.addComponent(chart);


        System.out.println("Ended");
    }

    class JasaRunner implements Button.ClickListener {

        Chart chart;
        Layout layout;

        JasaRunner(Chart chart, Layout layout) {
            this.chart = chart;
            this.layout=layout;
        }

        @Override
        public void buttonClick(ClickEvent clickEvent) {
            new Thread(new MainClassRunner()).start();
            layout.addComponent(new Label("JASA running in background!"));
        }
    }

    class TableUpdater implements Button.ClickListener{

        Chart chart;
        int count;

        TableUpdater(Chart chart) {
            this.chart = chart;
            count=0;
        }

        @Override
        public void buttonClick(ClickEvent clickEvent) {

            new Thread(new ChartUpdater(chart)).start();
//            count = updateTable(chart, count);
//            System.out.println(count);

        }
    }

}
