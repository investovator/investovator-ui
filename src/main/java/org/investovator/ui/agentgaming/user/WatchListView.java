package org.investovator.ui.agentgaming.user;

import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.agentgaming.user.components.WatchListTable;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class WatchListView extends DashboardPanel {

    VerticalLayout content;
    private WatchListTable watchListTable;

    public WatchListView(){

       setLayout();

    }

    private void setLayout(){

        setHeight("100%");

        content = new VerticalLayout();
        content.setSizeFull();

        watchListTable = new WatchListTable();

        content.addComponent(watchListTable);

        this.setContent(content);
    }

    @Override
    public void onEnter() {
        setDefaults();
        watchListTable.updateTable();
    }

    private void setDefaults(){


    }
}
