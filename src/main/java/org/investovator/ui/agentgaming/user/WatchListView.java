package org.investovator.ui.agentgaming.user;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.agentgaming.user.components.TimeSeriesChart;
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
    private HorizontalLayout stockAddLayout;
    private HorizontalLayout watchListTableLayout;
    private WatchListTable watchListTable;
    private StockSelectComboBox stockSelect;
    private org.investovator.ui.agentgaming.user.components.TimeSeriesChart selectedStockHistoryChart;
    private Button addStockButton;

    private String selectedStock;


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

        watchListTableLayout = new HorizontalLayout();

        stockSelect = new StockSelectComboBox();
        stockSelect.setWidth("150px");

        addStockButton = new Button("Add to watch list");

        watchListTable = new WatchListTable();
        selectedStockHistoryChart = new org.investovator.ui.agentgaming.user.components.TimeSeriesChart("market price");

        stockAddLayout.addComponent(stockSelect);
        stockAddLayout.addComponent(addStockButton);

        watchListTableLayout.addComponent(watchListTable);
        watchListTableLayout.addComponent(selectedStockHistoryChart);

        content.addComponent(stockAddLayout);
        content.addComponent(watchListTableLayout);

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

        watchListTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                selectedStock = itemClickEvent.getItemId().toString();
                updateHistoryChart();
            }
        });
    }

    @Override
    public void onEnter() {
        setDefaults();

        watchListTable.updateTable();

        selectedStock = (String) watchListTable.getItemIds().iterator().next();
        watchListTable.select(selectedStock);
        updateHistoryChart();

    }

    private void updateHistoryChart(){
        TimeSeriesChart newChart = new TimeSeriesChart("market price");
        newChart.setStocks(new String[]{selectedStock});
        newChart.update();
        watchListTableLayout.replaceComponent(selectedStockHistoryChart, newChart);
        selectedStockHistoryChart = newChart;

    }

    private void setDefaults(){
        stockSelect.update();
    }
}
