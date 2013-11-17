package org.investovator.ui.analysis;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.analysis.exceptions.AnalysisException;
import org.investovator.analysis.exceptions.InvalidParamException;
import org.investovator.analysis.technical.Calculator;
import org.investovator.analysis.technical.CalculatorImpl;
import org.investovator.analysis.technical.indicators.timeseries.utils.TimeSeriesGraph;
import org.investovator.analysis.technical.indicators.timeseries.utils.TimeSeriesParams;
import org.investovator.analysis.technical.indicators.timeseries.utils.TimeSeriesResultSet;
import org.investovator.analysis.technical.utils.IndicatorType;
import org.investovator.ui.agentgaming.ReportHelper;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AnalysisPanel extends DashboardPanel {


    VerticalLayout layout;
    GridLayout reportLayout;
    HashMap<String,MultiPlotTimeSeriesChart> charts;

    ComboBox reportSelect;
    Button addReportButton;

    private String selectedStock;
    private String selectedReport;
    ArrayList<String> addedStocks;

    protected static final String OHLC_DATE_FORMAT = "MM/dd/yyyy";
    private String stockID = "SAMP";


    public AnalysisPanel() {

        setHeight("100%");
        addedStocks = new ArrayList<>();
        charts = new HashMap<>();

        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);

        createReportLayout();

        //Report Select
        reportSelect = new ComboBox();
        reportSelect.setNullSelectionAllowed(false);
        fillReportsCombo();


        reportSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
                selectedReport = valueString;
            }
        });

        addReportButton = new Button("Add Analysis Report");
        addReportButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addReport(selectedReport);
            }
        });

        //charts.put( "market price", new MultiPlotTimeSeriesChart("market price"));

        HorizontalLayout stockSelectBar = new HorizontalLayout();
        stockSelectBar.setHeight("50px");

        stockSelectBar.addComponent(reportSelect);
        stockSelectBar.addComponent(addReportButton);

        layout.addComponent(stockSelectBar);
        layout.addComponent(reportLayout);

        for(MultiPlotTimeSeriesChart chart : charts.values()){
            reportLayout.addComponent(chart);
            reportLayout.setComponentAlignment(chart, Alignment.TOP_CENTER);
        }

        this.setContent(layout);
    }

    private void fillReportsCombo(){

        for(IndicatorType type : AnalysisHelper.getReportTypes()){
            reportSelect.addItem(type);
        }

        reportSelect.select(AnalysisHelper.getReportTypes()[0]);
        selectedReport=(AnalysisHelper.getReportTypes()[0].toString());

    }

    private void addReport(String report){

        Calculator calculator = new CalculatorImpl();

        String staringDate = "1/4/2010";
        String endDate = "3/30/2010";
        SimpleDateFormat format = new SimpleDateFormat(OHLC_DATE_FORMAT);

        TimeSeriesParams params = null;
        try {
            params = new TimeSeriesParams(stockID, format.parse(staringDate), format.parse(endDate));
            params.setPeriod(5);

            TimeSeriesResultSet resultSet = (TimeSeriesResultSet) calculator.calculateValues(Enum.valueOf(IndicatorType.class,report), params);

            final MultiPlotTimeSeriesChart newChart = new MultiPlotTimeSeriesChart(report);

            for(Map.Entry<TimeSeriesGraph, LinkedHashMap<Date, Double>> graph : resultSet.getGraphs().entrySet() )  {
                newChart.addSeries(graph.getKey().name(), graph.getValue());
            }

            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    reportLayout.addComponent(newChart);
                }
            }) ;

            UI.getCurrent().push();




        } catch (ParseException e) {
            e.printStackTrace();
        } catch (AnalysisException e) {
            e.printStackTrace();
        } catch (InvalidParamException e) {
            e.printStackTrace();
        }


    }

    private void createReportLayout(){
        reportLayout = new GridLayout();
        reportLayout.setSpacing(true);
        reportLayout.setMargin(true);
        reportLayout.setColumns(2);
    }

    @Override
    public void onEnter() {

    }
}
