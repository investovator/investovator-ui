package org.investovator.ui.dataplayback;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.investovator.core.data.types.HistoryOrderData;
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
        player= new DataPlayer("GOOG");
        player.setObserver(this);
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

