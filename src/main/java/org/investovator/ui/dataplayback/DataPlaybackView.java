package org.investovator.ui.dataplayback;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.Trendline;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.locations.TooltipLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.options.*;
import org.dussan.vaadin.dcharts.renderers.tick.AxisTickRenderer;
import org.investovator.controller.data.types.HistoryOrderData;
import org.investovator.dataPlayBackEngine.DataPlayer;
import org.investovator.ui.Authentication.Authenticator;

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
public class DataPlaybackView extends VerticalLayout implements View, Observer {
    Authenticator authenticator;
    DataPlayer player ;
    Label entry;
    int i = 0;

    public DataPlaybackView() {
        authenticator = Authenticator.getInstance();
        entry = new Label();
        player= new DataPlayer("GOOG", this);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!authenticator.isLoggedIn()) {
            getUI().getNavigator().navigateTo("");
        } else {
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



        }

        ///
        DataSeries dataSeries = new DataSeries()
                .newSeries()
                .add("23-May-08", 1)
                .add("24-May-08", 4)
                .add("25-May-08", 2)
                .add("26-May-08", 6);

        SeriesDefaults seriesDefaults = new SeriesDefaults()
                .setTrendline(
                        new Trendline()
                                .setShow(true));

        Axes axes = new Axes()
                .addAxis(
                        new XYaxis()
                                .setRenderer(AxisRenderers.DATE)
                                .setTickOptions(
                                        new AxisTickRenderer()
                                                .setFormatString("%#m/%#d/%y"))
                                .setNumberTicks(4))
                .addAxis(
                        new XYaxis(XYaxes.Y)
                                .setTickOptions(
                                        new AxisTickRenderer()
                                                .setFormatString("$%.2f")));

        Highlighter highlighter = new Highlighter()
                .setShow(true)
                .setSizeAdjust(10)
                .setTooltipLocation(TooltipLocations.NORTH)
                .setTooltipAxes(TooltipAxes.Y)
                .setTooltipFormatString("<b><i><span style='color:red;'>hello</span></i></b> %.2f")
                .setUseAxesFormatters(false);

        Cursor cursor = new Cursor()
                .setShow(true);

        Options options = new Options()
                .addOption(seriesDefaults)
                .addOption(axes)
                .addOption(highlighter)
                .addOption(cursor);

        DCharts chart = new DCharts()
                .setDataSeries(dataSeries)
                .setOptions(options)
                .show();



        //


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

