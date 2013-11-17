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
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.api.utils.StockTradingDataImpl;
import org.investovator.core.data.cassandraexplorer.excelimporter.HistoryData;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.agentgaming.ReportHelper;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

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

    private String selectedReport;

    protected static final String OHLC_DATE_FORMAT = "MM/dd/yyyy";
    private String stockID = "SAMP";

    Date startDate = null;
    Date endDate = null;
    Date dateRange[] = null;


    public AnalysisPanel() {

        setHeight("100%");
        charts = new HashMap<>();

        layout = new VerticalLayout();
        layout.setWidth("100%");

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


        HorizontalLayout stockSelectBar = new HorizontalLayout();
        stockSelectBar.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        stockSelectBar.setHeight("50px");

        stockSelectBar.addComponent(reportSelect);
        stockSelectBar.addComponent(addReportButton);

        layout.addComponent(stockSelectBar);
        layout.addComponent(reportLayout);

        this.setContent(layout);
    }


    private void setDefaults(){

        try {
            if(dateRange==null) dateRange = new CompanyStockTransactionsDataImpl().getDataDaysRange(CompanyStockTransactionsData.DataType.OHLC, stockID);
            endDate =  dateRange[1];
            Calendar tmp =  Calendar.getInstance();
            tmp.setTime(endDate);
            tmp.add(Calendar.MONTH, -1);
            startDate =  tmp.getTime();


        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }

    private void fillReportsCombo(){

        for(IndicatorType type : AnalysisHelper.getReportTypes()){
            reportSelect.addItem(type);
        }

        reportSelect.select(AnalysisHelper.getReportTypes()[0]);
        selectedReport=(AnalysisHelper.getReportTypes()[0].toString());

    }

    /**
     * Adds a report of the given type to the panel
     * @param report name of the report. value of IndicatorType Enum.
     */
    private void addReport(String report){

        Calculator calculator = new CalculatorImpl();
        TimeSeriesParams params = null;
        try {
            params = new TimeSeriesParams(stockID, startDate, endDate);
            params.setPeriod(5);

            TimeSeriesResultSet resultSet = (TimeSeriesResultSet) calculator.calculateValues(Enum.valueOf(IndicatorType.class,report), params);

            final MultiPlotTimeSeriesChart newChart = new MultiPlotTimeSeriesChart(report);

            if(!charts.containsKey(report)){

                charts.put(report,newChart );

                for(Map.Entry<TimeSeriesGraph, LinkedHashMap<Date, Double>> graph : resultSet.getGraphs().entrySet() )  {
                    newChart.addSeries(graph.getKey().name(), graph.getValue());
                }

                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        reportLayout.addComponent(newChart);
                    }
                }) ;
            }

        } catch (AnalysisException e) {
            e.printStackTrace();
        } catch (InvalidParamException e) {
            e.printStackTrace();
        }


    }

    private void addInitialCharts(){

        addReport(IndicatorType.EMA.name());
        addReport(IndicatorType.SMA.name());
        addReport(IndicatorType.TRIMA.name());
        addReport(IndicatorType.RSI.name());
    }

    private void createReportLayout(){
        reportLayout = new GridLayout();
        reportLayout.setSpacing(true);
        reportLayout.setMargin(true);
        reportLayout.setColumns(2);
        reportLayout.setWidth("100%");

        reportLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        reportLayout.setColumnExpandRatio(1,1);
        reportLayout.setColumnExpandRatio(2,1);

        reportLayout.setSpacing(true);
    }

    @Override
    public void onEnter() {

        if(!loaded){
            loaded=true;
            firstLoad();
        }

    }


    public void firstLoad(){
        setDefaults();
        addInitialCharts();
    }

    boolean loaded = false;
}
