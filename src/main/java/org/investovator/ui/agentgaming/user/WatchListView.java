package org.investovator.ui.agentgaming.user;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Alignment;
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
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class WatchListView extends DashboardPanel {

    private VerticalLayout content;
    private HorizontalLayout firstRowlayout;
    private HorizontalLayout stockAddLayout;
    private VerticalLayout watchListLayout;
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
        content.setWidth("100%");
        content.setSpacing(true);


        stockAddLayout = new HorizontalLayout();
        stockAddLayout.setHeight("50px");

        watchListLayout = new VerticalLayout();

        firstRowlayout = new HorizontalLayout();
        firstRowlayout.setWidth("95%");
        firstRowlayout.setCaption("Manage Watch List");
        firstRowlayout.addStyleName("center-caption");

        stockSelect = new StockSelectComboBox();
        stockSelect.setWidth("150px");

        addStockButton = new Button("Add to watch list");

        watchListTable = new WatchListTable();

        stockAddLayout.addComponent(stockSelect);
        stockAddLayout.addComponent(addStockButton);
        stockAddLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        watchListLayout.addComponent(stockAddLayout);
        watchListLayout.addComponent(watchListTable);
        watchListLayout.setComponentAlignment(stockAddLayout, Alignment.MIDDLE_CENTER);
        watchListLayout.setComponentAlignment(watchListTable, Alignment.TOP_CENTER);

        firstRowlayout.addComponent(watchListLayout);

        content.addComponent(firstRowlayout);

        this.setContent(content);
    }

    private void bindListeners(){

        addStockButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    UserData userData = new UserDataImpl();
                    userData.addToWatchList(Session.getCurrentGameInstance(),Authenticator.getInstance().getCurrentUser(), stockSelect.getSelectedStock());
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
                addHistoryChart();
            }
        });
    }

    @Override
    public void onEnter() {
        setDefaults();

        watchListTable.updateTable();

        selectedStock = (String) watchListTable.getItemIds().iterator().next();
        watchListTable.select(selectedStock);
        addHistoryChart();

        firstRowlayout.setExpandRatio(watchListLayout, 2);
        firstRowlayout.setExpandRatio(selectedStockHistoryChart, 1);

        watchListTable.setWidth("90%");

    }

    private void addHistoryChart(){

        TimeSeriesChart newChart = new TimeSeriesChart("market price");
        newChart.setWidth("400px");
        newChart.setHeight("300px");
        newChart.setStocks(new String[]{selectedStock});
        newChart.update();

        if(selectedStockHistoryChart == null){
            firstRowlayout.addComponent(newChart);
        } else{
            firstRowlayout.replaceComponent(selectedStockHistoryChart, newChart);
        }
        selectedStockHistoryChart = newChart;

    }

    private void setDefaults(){
        stockSelect.update();
    }
}
