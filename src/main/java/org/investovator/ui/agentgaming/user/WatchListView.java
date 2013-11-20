package org.investovator.ui.agentgaming.user;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.agentgaming.user.components.WatchListTable;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.main.components.StockSelectComboBox;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class WatchListView extends DashboardPanel {

    private VerticalLayout content;
    HorizontalLayout stockAddLayout;
    private WatchListTable watchListTable;
    private StockSelectComboBox stockSelect;
    private Button addStockButton;


    public WatchListView(){

       setLayout();

        bindListeners();
    }

    private void setLayout(){

        setHeight("100%");

        content = new VerticalLayout();
        content.setSizeUndefined();

        stockAddLayout = new HorizontalLayout();
        stockAddLayout.setHeight("50px");

        stockSelect = new StockSelectComboBox();
        stockSelect.setWidth("150px");

        addStockButton = new Button("Add to watch list");

        watchListTable = new WatchListTable();

        stockAddLayout.addComponent(stockSelect);
        stockAddLayout.addComponent(addStockButton);

        content.addComponent(stockAddLayout);
        content.addComponent(watchListTable);

        this.setContent(content);
    }

    private void bindListeners(){
        addStockButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    UserData userData = new UserDataImpl();
                    userData.addToWatchList(Authenticator.getInstance().getCurrentUser(), stockSelect.getSelectedStock());
                    watchListTable.updateTable();
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onEnter() {
        setDefaults();
        watchListTable.updateTable();
    }

    private void setDefaults(){
        stockSelect.update();
    }
}
