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
    Button addButton;
    private String selectedStock;

    public ReportsView() {

        charts = new HashMap<>();

        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        //Stock Select
        stockSelect = new ComboBox();
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
                    chart.addStock(selectedStock);
                    chart.update();
                    chart.drawChart();
                }
            }
        });

        charts.put( "market price", new TimeSeriesChart("market price"));
        charts.put( "market spread", new TimeSeriesChart("market spread"));

        HorizontalLayout stockSelectBar = new HorizontalLayout();

        stockSelectBar.addComponent(stockSelect);
        stockSelectBar.addComponent(addButton);

        layout.addComponent(stockSelectBar);

        for(TimeSeriesChart chart : charts.values()){
            layout.addComponent(chart);
        }

        layout.addComponent(new Label());

        this.setContent(layout);
    }

    @Override
    public void onEnter() {

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

        setHeight("300px");

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