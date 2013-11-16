package org.investovator.ui.agentgaming;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.utils.dashboard.DashboardPanel;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.Collection;
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
                            layout.setComponentAlignment(newChart, Alignment.MIDDLE_CENTER);

                            UI.getCurrent().push();
                        }
                    });
                }

            }
        });

        charts.put( "market price", new TimeSeriesChart("market price"));

        HorizontalLayout stockSelectBar = new HorizontalLayout();

        stockSelectBar.addComponent(stockSelect);
        stockSelectBar.addComponent(addButton);
        stockSelectBar.addComponent(reportSelect);
        stockSelectBar.addComponent(addReportButton);

        layout.addComponent(stockSelectBar);

        for(TimeSeriesChart chart : charts.values()){
            layout.addComponent(chart);
            layout.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
        }

        layout.addComponent(new Label());

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


class TimeSeriesChart extends Chart {

    private HashMap<String, DataSeries> series;
    private List<String> stocks;
    private String chartVariable;
    private int dataPoints = 50;

    String getChartVariable() {
        return chartVariable;
    }


    public void addStock(String stockID){
        if(!stocks.contains(stockID)){
            stocks.add(stockID);
        }
    }

    public TimeSeriesChart(String chartVariable) {

        this.chartVariable = chartVariable;

        series = new HashMap<String, DataSeries>();
        stocks = new ArrayList<>();

        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.LINE);
        configuration.setTitle(chartVariable);


        configuration.getxAxis().setType(AxisType.DATETIME);

        PlotOptionsLine plotOptionsLine = new PlotOptionsLine();
        plotOptionsLine.setMarker(new Marker(false));
        plotOptionsLine.setShadow(false);
        plotOptionsLine.setAnimation(false);
        configuration.setPlotOptions(plotOptionsLine);


        drawChart(configuration);
        configuration.disableCredits();

        setHeight("400px");
        setWidth("90%");

    }

    public void update() {

        for (String stock : stocks) {

            if (!series.containsKey(stock)) {
                DataSeries tmp = new DataSeries();
                series.put(stock, tmp);
                getConfiguration().addSeries(tmp);
            }

            final DataSeries dataSeries = series.get(stock);
            dataSeries.setName(stock);

            final ArrayList<TimeSeriesNode> data = ReportHelper.getInstance().getTimeSeriesReport(stock, chartVariable, 50);

            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {

                    dataSeries.clear();

                    for (TimeSeriesNode node : data) {
                        dataSeries.add(new DataSeriesItem(node.getDate(), node.getValue()));
                    }

                    UI.getCurrent().push();
                }
            });

        }

    }


}