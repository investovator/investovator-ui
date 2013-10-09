package org.investovator.ui.dataplayback;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.ChartClickEvent;
import com.vaadin.addon.charts.ChartClickListener;
import com.vaadin.addon.charts.ChartOptions;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.addon.charts.themes.GrayTheme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.investovator.core.data.types.HistoryOrderData;
import org.investovator.core.excelimporter.HistoryData;
import org.investovator.dataPlayBackEngine.DataPlayer;
import org.investovator.ui.GlobalView;
import org.investovator.ui.authentication.Authenticator;

import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: hasala
 * Date: 10/4/13
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class DataPlaybackView extends GlobalView implements Observer {
    Authenticator authenticator;
    DataPlayer player ;
    Label entry;
    int i = 0;

    private CheckBox useCustomStyles;

    public DataPlaybackView() {
        authenticator = Authenticator.getInstance();
        entry = new Label();
        player= new DataPlayer("GOOG");
        player.setObserver(this);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!authenticator.isLoggedIn()) {
            getUI().getNavigator().navigateTo("");
        } else {

            //related to ticker data playback

            Notification.show("Welcome to Data Playback Engine");
            addComponent(entry);
            addComponent(new Label("------------------------------------------------"));

            Button startBut=new Button("play");
            startBut.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    player.runPlayback(1);
                }
            });

            Button stopBut=new Button("stop");
            stopBut.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    player.stopPlayback();
                }
            });
            addComponent(startBut);
            addComponent(stopBut);

//            end of related to ticker data playback




            //boxplot
            final Chart chart = new Chart();

            ChartOptions.get().setTheme(new GrayTheme());

            chart.getConfiguration().setTitle("Box Plot Example");

            Legend legend = new Legend();
            legend.setEnabled(false);
            chart.getConfiguration().setLegend(legend);

            XAxis xaxis = chart.getConfiguration().getxAxis();
            xaxis.setTitle("Experiment No.");
            //xaxis.setCategories("1", "2", "3", "4", "5");
            xaxis.setTickInterval(1);
            //chart.getConfiguration().getM

            YAxis yAxis = chart.getConfiguration().getyAxis();

            yAxis.setTitle("Observations");
            PlotLine plotLine = new PlotLine();
            plotLine.setColor(new SolidColor("red"));
            plotLine.setValue(932);
            plotLine.setWidth(1);
            PlotBandLabel label = new PlotBandLabel("Theoretical mean: 932");
            label.setAlign(HorizontalAlign.CENTER);
            Style style = new Style();
            style.setColor(new SolidColor("gray"));
            label.setStyle(style);
            plotLine.setLabel(label);
            yAxis.setPlotLines(plotLine);

//            ChartModel model=chart.getConfiguration().getChart();
//            model.setAnimation(false);
//            chart.getConfiguration().setChart(model);

            PlotOptionsBoxPlot plotOptions = new PlotOptionsBoxPlot();
            plotOptions.setAnimation(false);
            chart.getConfiguration().setPlotOptions(plotOptions);


            final DataSeries observations = new DataSeries();
            observations.setName("Observations");

            // Add PlotBoxItems contain all fields relevant for plot box chart
            observations.add(new BoxPlotItem(760, 801, 848, 895, 965));

            // Example with no arg constructor
            BoxPlotItem plotBoxItem = new BoxPlotItem();
            plotBoxItem.setLow(733);
            plotBoxItem.setLowerQuartile(853);
            plotBoxItem.setMedian(939);
            plotBoxItem.setUpperQuartile(980);
            plotBoxItem.setHigh(1080);
            observations.add(plotBoxItem);

            observations.add(new BoxPlotItem(714, 762, 817, 870, 918));
            observations.add(new BoxPlotItem(724, 802, 806, 871, 950));
            observations.add(new BoxPlotItem(724, 802, 806, 871, 950));
            observations.add(new BoxPlotItem(834, 836, 864, 882, 910));
            observations.setPlotOptions(getPlotBoxOptions());
            chart.getConfiguration().addSeries(observations);

//            DataSeries newseries=(DataSeries)chart.getConfiguration().getSeries().get(0);
//            newseries.add(new BoxPlotItem(834, 836, 864, 882, 910));
//            chart.getConfiguration().addSeries(newseries);



            //observations.add(new BoxPlotItem(884, 886, 914, 932, 960));

            chart.drawChart();

            //threaded

            Thread randomDataGenerator = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            sleep(1000);
                            if (chart.isConnectorEnabled()) {
//                                long x = System.currentTimeMillis();
                                //double y = random.nextDouble();
                                getSession().lock();
                                try {
                                    observations.add(new BoxPlotItem(834, 836, 864, 882, 910));

                                    HistoryData d=player.getOHLCPrice("Goog", "2012-10-3-19-45-33");

                                    observations.add(new BoxPlotItem(d.getLowPrice(), d.getLowPrice()+25, d.getLowPrice()+50, d.getLowPrice()+75, d.getHighPrice()));
                                    chart.drawChart();
                                    System.out.println("called");
                                } finally {
                                    getSession().unlock();
                                }
                            } else {
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };



            randomDataGenerator.start();

            ///threaded end


            //end of boxplot

            //OHLC data playback related
            Button ohlcStartBut=new Button("play OHLC");
            ohlcStartBut.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    final HistoryData d=player.getOHLCPrice("Goog", "2012-10-3-19-45-33");

//                    getUI().access(new Runnable() {
//                        @Override
//                        public void run() {
                            //entry.setValue(d.getDate() + "-" + d.getStockId() + "-" + i);
//                            addComponent(new Label(d.getLowPrice() + "-" + d.getHighPrice() + "-" + i));
//                            i++;
                            //observations.add(new BoxPlotItem(d.getLowPrice(), d.getLowPrice()+25, d.getLowPrice()+50, d.getLowPrice()+75, d.getHighPrice()));
                    observations.add(new BoxPlotItem(884, 886, 914, 932, 960));
                    chart.drawChart();
//                            observations.addData(d.getOpeningPrice()/100);



//                        }
//                    });
                }
            });

            addComponent(ohlcStartBut);


            chart.addChartClickListener(new ChartClickListener() {
                @Override
                public void onClick(ChartClickEvent event) {
//                    double getyAxisValue = event.getyAxisValue();
//                    series.addData(getyAxisValue);
                    observations.add(new BoxPlotItem(884, 886, 914, 932, 960));
                    chart.drawChart();
                    System.out.println("done");

                }
            });

            addComponent(chart);






//            ////////  additional chart
//            final Chart nchart = new Chart();
//            nchart.setHeight("450px");
//            nchart.setWidth("100%");
//
//            final Configuration configuration = new Configuration();
//
//            configuration.getChart().setType(ChartType.SPLINE);
//
//            final ListSeries series = new ListSeries(29.9, 71.5, 106.4, 129.2,
//                    144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4);
//            configuration.setSeries(series);
//
//            nchart.drawChart(configuration);
//
//            nchart.addChartClickListener(new ChartClickListener() {
//                @Override
//                public void onClick(ChartClickEvent event) {
//                    double getyAxisValue = event.getyAxisValue();
//                    series.addData(getyAxisValue);
//                }
//            });
//            addComponent(nchart);

            ////// end of additional chart


//            //OHLC data playback related
//            Button ohlcStartBut=new Button("play OHLC");
//            ohlcStartBut.addClickListener(new Button.ClickListener() {
//                @Override
//                public void buttonClick(Button.ClickEvent clickEvent) {
//                    final HistoryData d=player.getOHLCPrice("Goog", "2012-10-3-19-45-33");
//
//                    getUI().access(new Runnable() {
//                        @Override
//                        public void run() {
//                            //entry.setValue(d.getDate() + "-" + d.getStockId() + "-" + i);
//                            addComponent(new Label(d.getOpeningPrice() + "-" + d.getClosingPrice() + "-" + i));
//                            i++;
////                            observations.add(new BoxPlotItem(904, 906, 934, 952, 980), true, true);
//                            series.addData(d.getOpeningPrice()/100);
//
//
//
//                        }
//                    });
//                }
//            });
//
//            addComponent(ohlcStartBut);








        }
    }




    public void setupUI(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("Welcome to Data Playback Engine");
    }

    private PlotOptionsBoxPlot getPlotBoxOptions() {
        PlotOptionsBoxPlot options = new PlotOptionsBoxPlot();

        if (false) {
            // optional styling
            options.setMedianColor(new SolidColor("cyan"));
            options.setMedianWidth(1);

            options.setStemColor(new SolidColor("green"));
            options.setStemDashStyle(DashStyle.SHORTDOT);
            options.setStemWidth(4);

            options.setWhiskerColor(new SolidColor("magenta"));
            options.setWhiskerLength(70);
            options.setWhiskerWidth(5);
        }

        return options;
    }


    @Override
    public void update(Observable o, Object arg) {
        final HistoryOrderData d = (HistoryOrderData) arg;

        getUI().access(new Runnable() {
            @Override
            public void run() {
                entry.setValue(d.getDate() + "-" + d.getStockId() + "-" + i);
                addComponent(new Label(d.getDate() + "-" + d.getStockId() + "-" + i));
                i++;


            }
        });


    }


}

