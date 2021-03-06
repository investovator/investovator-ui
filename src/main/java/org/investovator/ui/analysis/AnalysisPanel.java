package org.investovator.ui.analysis;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.investovator.analysis.exceptions.AnalysisException;
import org.investovator.analysis.exceptions.InvalidParamException;
import org.investovator.analysis.technical.Calculator;
import org.investovator.analysis.technical.CalculatorImpl;
import org.investovator.analysis.technical.indicators.timeseries.utils.TimeSeriesGraph;
import org.investovator.analysis.technical.indicators.timeseries.utils.TimeSeriesParams;
import org.investovator.analysis.technical.indicators.timeseries.utils.TimeSeriesResultSet;
import org.investovator.analysis.technical.utils.IndicatorType;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.*;
import java.util.Calendar;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AnalysisPanel extends DashboardPanel {


    /**
     * Layout Components *
     */
    VerticalLayout layout;
    GridLayout reportLayout;
    HorizontalLayout configBar;
    HashMap<String, MultiPlotTimeSeriesChart> charts;

    ComboBox reportSelect;
    ComboBox stockSelect;

    Button addReportButton;
    Button dateUpdate;

    DateField startDatePicker;
    DateField endDatePicker;

    /**
     * Layout Components *
     */


    private String selectedReport;
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


        configBar = new HorizontalLayout();
        configBar.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        configBar.setHeight("50px");
        configBar.setWidth("95%");

        configBar.setStyleName("center-caption");

        createDatePickers();
        createDateUpdateButton();
        createStockSelect();

        configBar.addComponent(stockSelect);
        configBar.addComponent(reportSelect);
        configBar.addComponent(addReportButton);
        configBar.addComponent(startDatePicker);
        configBar.addComponent(endDatePicker);
        configBar.addComponent(dateUpdate);

        configBar.setExpandRatio(stockSelect,4);
        configBar.setExpandRatio(reportSelect,2);
        configBar.setExpandRatio(addReportButton,2);
        configBar.setExpandRatio(startDatePicker,2);
        configBar.setExpandRatio(endDatePicker,2);
        configBar.setExpandRatio(dateUpdate,2);


        layout.addComponent(configBar);
        layout.addComponent(reportLayout);

        this.setContent(layout);
    }


    private void setDefaults() {

        try {
            ArrayList<String> stocks = new CompanyDataImpl().getAvailableStockIds();

            for(String stock : stocks){
                stockSelect.addItem(stock);
            }
            stockSelect.select(stocks.get(0));
            stockID = stocks.get(0);

            stockSelect.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    stockID =  stockSelect.getValue().toString();
                    try {
                        configBar.setCaption(stockID + " - "+ new CompanyDataImpl().getCompanyName(stockID));
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }

                    for(String chart : charts.keySet()){
                        addReport(chart);
                    }

                    UI.getCurrent().access(new Runnable() {
                        @Override
                        public void run() {
                            UI.getCurrent().push();
                        }
                    });
                }
            });

            configBar.setCaption(stockID + " - "+ new CompanyDataImpl().getCompanyName(stockID));


        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        try {
            if (dateRange == null)
                dateRange = new CompanyStockTransactionsDataImpl().getDataDaysRange(CompanyStockTransactionsData.DataType.OHLC, stockID);
            endDate = dateRange[1];
            Calendar tmp = Calendar.getInstance();
            tmp.setTime(endDate);
            tmp.add(Calendar.MONTH, -2);
            startDate = tmp.getTime();


        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

    }

    private void fillReportsCombo() {

        for (IndicatorType type : AnalysisHelper.getReportTypes()) {
            reportSelect.addItem(type);
        }

        reportSelect.select(AnalysisHelper.getReportTypes()[0]);
        selectedReport = (AnalysisHelper.getReportTypes()[0].toString());

    }

    /**
     * Adds a report of the given type to the panel
     *
     * @param report name of the report. value of IndicatorType Enum.
     */
    private void addReport(final String report) {

        Calculator calculator = new CalculatorImpl();
        TimeSeriesParams params = null;
        try {
            params = new TimeSeriesParams(stockID, startDate, endDate);
            params.setPeriod(5);

            TimeSeriesResultSet resultSet = (TimeSeriesResultSet) calculator.calculateValues(Enum.valueOf(IndicatorType.class, report), params);

            final MultiPlotTimeSeriesChart newChart = new MultiPlotTimeSeriesChart(report);

            for (Map.Entry<TimeSeriesGraph, LinkedHashMap<Date, Double>> graph : resultSet.getGraphs().entrySet()) {
                newChart.addSeries(graph.getKey().name(), graph.getValue());
            }

            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    if (charts.containsKey(report)) {
                        reportLayout.replaceComponent(charts.get(report), newChart);
                    }else{
                        reportLayout.addComponent(newChart);
                    }
                    charts.put(report, newChart);
                }
            });

        } catch (AnalysisException e) {
            e.printStackTrace();
        } catch (InvalidParamException e) {
            e.printStackTrace();
        }


    }


    private void addInitialCharts() {

        addReport(IndicatorType.EMA.name());
        addReport(IndicatorType.RSI.name());
        addReport(IndicatorType.MACD.name());
        addReport(IndicatorType.BBAND.name());
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


    private void createDatePickers() {

        startDatePicker = new DateField();
        startDatePicker.setImmediate(true);
        startDatePicker.setTimeZone(TimeZone.getTimeZone("UTC"));
        startDatePicker.setLocale(Locale.US);
        startDatePicker.setResolution(Resolution.DAY);


        startDatePicker.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                final String valueString = String.valueOf(event.getProperty().getValue());
                startDate = startDatePicker.getValue();
            }
        });


        endDatePicker = new DateField();
        endDatePicker.setImmediate(true);
        endDatePicker.setTimeZone(TimeZone.getTimeZone("UTC"));
        endDatePicker.setLocale(Locale.US);
        endDatePicker.setResolution(Resolution.DAY);

        endDatePicker.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                endDate = endDatePicker.getValue();
            }
        });


        startDatePicker.setWidth("100px");
        endDatePicker.setWidth("100px");

    }

    private void createDateUpdateButton() {
        dateUpdate = new Button("Update Dates");

        dateUpdate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                for (String chart : charts.keySet()) {
                    addReport(chart);
                }
            }
        });
    }

    private void createStockSelect(){

        stockSelect = new ComboBox();
        stockSelect.setImmediate(true);
        stockSelect.setNullSelectionAllowed(false);

    }

    @Override
    public void onEnter() {

        if (!loaded) {
            loaded = true;
            firstLoad();
        }

    }


    public void firstLoad() {
        setDefaults();
        addInitialCharts();
    }

    boolean loaded = false;
}
