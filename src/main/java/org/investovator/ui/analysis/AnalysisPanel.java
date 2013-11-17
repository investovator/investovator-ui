package org.investovator.ui.analysis;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class AnalysisPanel extends DashboardPanel {


    VerticalLayout layout;
    HashMap<String,MultiPlotTimeSeriesChart> charts;

    ComboBox reportSelect;
    Button addReportButton;

    private String selectedStock;
    private String selectedReport;
    ArrayList<String> addedStocks;

    public AnalysisPanel() {

        setHeight("100%");
        addedStocks = new ArrayList<>();
        charts = new HashMap<>();

        layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);


        //Report Select
        reportSelect = new ComboBox();
        reportSelect.setNullSelectionAllowed(false);


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


            }
        });

        //charts.put( "market price", new MultiPlotTimeSeriesChart("market price"));

        HorizontalLayout stockSelectBar = new HorizontalLayout();
        stockSelectBar.setHeight("50px");

        stockSelectBar.addComponent(reportSelect);
        stockSelectBar.addComponent(addReportButton);

        layout.addComponent(stockSelectBar);

        for(MultiPlotTimeSeriesChart chart : charts.values()){
            layout.addComponent(chart);
            layout.setComponentAlignment(chart, Alignment.TOP_CENTER);
        }

        this.setContent(layout);
    }
    @Override
    public void onEnter() {

    }
}
