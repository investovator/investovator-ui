package org.investovator.ui.agentgaming.user;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.agentgaming.ReportHelper;
import org.investovator.ui.agentgaming.beans.TimeSeriesNode;
import org.investovator.ui.agentgaming.user.components.TimeSeriesChart;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class ReportsView extends DashboardPanel {

    VerticalLayout layout;
    GridLayout reportLayout;
    HashMap<String,TimeSeriesChart> charts;

    ComboBox stockSelect;
    ComboBox reportSelect;
    Button addButton;
    Button addReportButton;

    private String selectedStock;
    private String selectedReport;
    ArrayList<String> addedStocks;

    public ReportsView() {

        setHeight("100%");
        addedStocks = new ArrayList<>();
        charts = new HashMap<>();

        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        createReportLayout();

        //Stock Select
        stockSelect = new ComboBox();
        stockSelect.setNullSelectionAllowed(false);
        stockSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
                selectedStock = valueString;
            }
        });

        addButton = new Button("Add Stock");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                 addStock(selectedStock);
            }
        });

        //Report Select
        reportSelect = new ComboBox();
        reportSelect.setNullSelectionAllowed(false);
        reportSelect.addItem("market price");
        reportSelect.addItem("market spread");

        reportSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
                selectedReport = valueString;
            }
        });

        addReportButton = new Button("Add Report");
        addReportButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addChart(selectedReport);
            }
        });

        HorizontalLayout stockSelectBar = new HorizontalLayout();
        stockSelectBar.setHeight("50px");

        stockSelectBar.addComponent(stockSelect);
        stockSelectBar.addComponent(addButton);
        stockSelectBar.addComponent(reportSelect);
        stockSelectBar.addComponent(addReportButton);

        layout.addComponent(stockSelectBar);
        layout.addComponent(reportLayout);
        this.setContent(layout);
    }

    private void createReportLayout() {
        reportLayout = new GridLayout();
        reportLayout.setSpacing(true);
        reportLayout.setMargin(true);
        reportLayout.setColumns(2);
        reportLayout.setWidth("100%");
        reportLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        reportLayout.setColumnExpandRatio(1, 1);
        reportLayout.setColumnExpandRatio(2, 1);


        reportLayout.setSpacing(true);
    }


    private void addChart(String report){
        if(!charts.containsKey(report)) {

            final TimeSeriesChart newChart = new TimeSeriesChart(report);
            newChart.setHeight("300px");
            newChart.setWidth("100%");

            charts.put( report,newChart  );

            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    for(String stock : addedStocks){
                        newChart.addStock(stock);
                    }
                    for(TimeSeriesChart chart : charts.values()){
                        chart.update();
                        chart.drawChart();
                    }
                    reportLayout.addComponent(newChart);
                    reportLayout.setComponentAlignment(newChart, Alignment.MIDDLE_CENTER);

                    UI.getCurrent().push();
                }
            });
        }
    }


    private void addStock(String stock){
        for(TimeSeriesChart chart : charts.values()){
            addedStocks.add(stock);
            chart.addStock(stock);
            chart.update();
            chart.drawChart();
        }
    }

    @Override
    public void onEnter() {

        addChart("market price");
        addChart("market spread");

        UserData userData = null;

        try {
            userData = new UserDataImpl();
            ArrayList<String > stocks = userData.getWatchList(Session.getCurrentGameInstance(),Authenticator.getInstance().getCurrentUser());

            for(String stock : stocks) {
                addStock(stock);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        //Fill Stocks on page load
        try {
            ArrayList<String> stocks = new CompanyDataImpl().getAvailableStockIds();
            for(String stock : stocks){
                if(!stockSelect.containsId(stock)) stockSelect.addItem(stock);
            }

            stockSelect.select(stocks.get(0));
            reportSelect.select("market price");

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }


}