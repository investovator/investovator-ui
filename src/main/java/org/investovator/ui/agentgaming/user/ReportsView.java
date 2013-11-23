package org.investovator.ui.agentgaming.user;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.agentgaming.ReportHelper;
import org.investovator.ui.agentgaming.beans.TimeSeriesNode;
import org.investovator.ui.agentgaming.user.components.TimeSeriesChart;
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
                for(TimeSeriesChart chart : charts.values()){
                    addedStocks.add(selectedStock);
                    chart.addStock(selectedStock);
                    chart.update();
                    chart.drawChart();
                }
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
                if(!charts.containsKey(selectedReport)) {

                    final TimeSeriesChart newChart = new TimeSeriesChart(selectedReport);
                    charts.put( selectedReport,newChart  );

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
                            layout.addComponent(newChart);
                            layout.setComponentAlignment(newChart, Alignment.TOP_CENTER);

                            UI.getCurrent().push();
                        }
                    });
                }

            }
        });

        charts.put( "market price", new TimeSeriesChart("market price"));

        HorizontalLayout stockSelectBar = new HorizontalLayout();
        stockSelectBar.setHeight("50px");

        stockSelectBar.addComponent(stockSelect);
        stockSelectBar.addComponent(addButton);
        stockSelectBar.addComponent(reportSelect);
        stockSelectBar.addComponent(addReportButton);

        layout.addComponent(stockSelectBar);

        for(TimeSeriesChart chart : charts.values()){
            layout.addComponent(chart);
            layout.setComponentAlignment(chart, Alignment.TOP_CENTER);
        }

        this.setContent(layout);
    }

    @Override
    public void onEnter() {

        //Fill Stocks on page load
        try {
            ArrayList<String> stocks = new CompanyDataImpl().getAvailableStockIds();
            for(String stock : stocks){
                if(!stockSelect.containsId(stock)) stockSelect.addItem(stock);
            }

            if(selectedStock==null) selectedStock=stocks.get(0);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        for(TimeSeriesChart chart : charts.values()){
            chart.update();
        }
    }


}