/*
 * investovator, Stock Market Gaming Framework
 *     Copyright (C) 2013  investovator
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.nngaming;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.investovator.ann.nngaming.NNGamingFacade;
import org.investovator.ann.nngaming.util.GameTypes;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.ui.nngaming.utils.GameDataHelper;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class StockAnalysisView extends DashboardPanel {

    private ArrayList<AnalysisChart> charts;
    private ArrayList<ArrayList<Object>> graphData;
    private GameDataHelper gameDataHelper;
    private ArrayList<String> stockIDs;
    private ArrayList<String> analysisIDs;
    private NNGamingFacade nnGamingFacade;

    private int ACTUAL_INDEX = 1;
    private int PREDICTED_INDEX = 2;

    VerticalLayout layout;
    GridLayout reportLayout;
    HorizontalLayout configBar;

    TextField inputParameter;
    ComboBox stockSelect;

    Button chartUpdate;

    DateField startDatePicker;
    DateField endDatePicker;

    Date startDate = null;
    Date endDate = null;
    Date minDate = null;
    Date maxDate = null;

    private double analysisValue = 250;


    public StockAnalysisView() {

        setHeight("100%");

        layout = new VerticalLayout();
        layout.setWidth("100%");

        createReportLayout();

        configBar = new HorizontalLayout();
        configBar.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        configBar.setHeight("50px");
        configBar.setWidth("95%");

        configBar.setStyleName("center-caption");

        createDatePickers();
        createChartUpdateButton();
        createParamInputField();
        createParameterSelect();

        configBar.addComponent(stockSelect);
        configBar.addComponent(inputParameter);
        configBar.addComponent(startDatePicker);
        configBar.addComponent(endDatePicker);
        configBar.addComponent(chartUpdate);

        configBar.setExpandRatio(stockSelect,4);
        configBar.setExpandRatio(startDatePicker,2);
        configBar.setExpandRatio(inputParameter,2);
        configBar.setExpandRatio(endDatePicker,2);
        configBar.setExpandRatio(chartUpdate,2);

        configBar.setCaption("Stock Market Share Price Analysis");

        layout.addComponent(configBar);
        layout.addComponent(reportLayout);

        this.setContent(layout);

        charts = new ArrayList<>();
        stockIDs = new ArrayList<>();
        analysisIDs = new ArrayList<>();
        nnGamingFacade = NNGamingFacade.getInstance();
        gameDataHelper = GameDataHelper.getInstance();

    }

    private void createParameterSelect(){
        stockSelect = new ComboBox();
        stockSelect.setCaption("Select StockID");
        stockSelect.setImmediate(true);
        stockSelect.setNullSelectionAllowed(false);
    }

    private void setDefaults() {

        stockIDs = gameDataHelper.getStockList();
        analysisIDs = gameDataHelper.getAnalysisParameters();
        graphData = new ArrayList<>();

        ArrayList<TradingDataAttribute> attributes = new ArrayList<>();
        attributes.add(TradingDataAttribute.LOW_PRICE);

        Date[] dates = nnGamingFacade.getDateRange(stockIDs.get(0));

        minDate = dates[0];
        maxDate = dates[1];

        endDate = dates[1];
        java.util.Calendar tmp = java.util.Calendar.getInstance();
        tmp.setTime(endDate);
        tmp.add(java.util.Calendar.MONTH, -1);
        startDate = tmp.getTime();

        startDatePicker.setRangeStart(startDate);
        startDatePicker.setRangeEnd(endDate);
        endDatePicker.setRangeStart(startDate);
        endDatePicker.setRangeEnd(endDate);
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

        for(int i = 0; i < stockIDs.size(); i++){
            stockSelect.addItem(stockIDs.get(i));
        }
        stockSelect.setValue(stockIDs.get(0));
    }

    private void createReportLayout() {
        reportLayout = new GridLayout();
        reportLayout.setSpacing(true);
        reportLayout.setMargin(true);
        reportLayout.setColumns(1);
        reportLayout.setWidth("100%");
        reportLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        reportLayout.setColumnExpandRatio(1, 1);

        reportLayout.setSpacing(true);
    }

    private void createParamInputField(){

        inputParameter = new TextField("Input Value");
        inputParameter.setImmediate(true);
        inputParameter.setValue(String.valueOf(analysisValue));
        inputParameter.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                 try {
                        analysisValue = Double.parseDouble(inputParameter.getValue());
                 }catch (NumberFormatException e){
                     Notification.show("Please insert a valid input value", Notification.Type.TRAY_NOTIFICATION);
                 }
            }
        });

    }

    private void createDatePickers() {

        startDatePicker = new DateField();
        startDatePicker.setImmediate(true);
        startDatePicker.setTimeZone(TimeZone.getTimeZone("UTC"));
        startDatePicker.setLocale(Locale.US);
        startDatePicker.setResolution(Resolution.DAY);
        startDatePicker.setCaption("Start Date");


        startDatePicker.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                final String valueString = String.valueOf(event.getProperty().getValue());
                startDate = startDatePicker.getValue();

                if(startDate.before(minDate) || startDate.after(maxDate)){
                    Notification.show("Please select a date within "+minDate+" - "+maxDate+" range",
                            Notification.Type.TRAY_NOTIFICATION);
                    return;
                }

                java.util.Calendar tmp = java.util.Calendar.getInstance();
                tmp.setTime(startDate);
                tmp.add(java.util.Calendar.MONTH, 1);
                endDate = tmp.getTime();

                endDatePicker.setRangeStart(startDate);
                endDatePicker.setRangeEnd(endDate);
                startDatePicker.setRangeStart(startDate);
                startDatePicker.setRangeEnd(endDate);

                endDatePicker.setValue(endDate);
            }
        });


        endDatePicker = new DateField();
        endDatePicker.setImmediate(true);
        endDatePicker.setTimeZone(TimeZone.getTimeZone("UTC"));
        endDatePicker.setLocale(Locale.US);
        endDatePicker.setResolution(Resolution.DAY);
        endDatePicker.setCaption("End Date");

        endDatePicker.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                endDate = endDatePicker.getValue();

                if(endDate.before(minDate) || endDate.after(maxDate)){
                    Notification.show("Please select a date within "+minDate+" - "+maxDate+" range",
                            Notification.Type.TRAY_NOTIFICATION);
                    return;
                }

                java.util.Calendar tmp = java.util.Calendar.getInstance();
                tmp.setTime(endDate);
                tmp.add(java.util.Calendar.MONTH, -1);
                startDate = tmp.getTime();

                startDatePicker.setRangeStart(startDate);
                startDatePicker.setRangeEnd(endDate);
                endDatePicker.setRangeStart(startDate);
                endDatePicker.setRangeEnd(endDate);

                startDatePicker.setValue(startDate);
            }
        });


        startDatePicker.setWidth("100px");
        endDatePicker.setWidth("100px");

    }

    private void createChartUpdateButton() {
        chartUpdate = new Button("Update Charts");

        chartUpdate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                if(inputParameter.getValue().equals("0") || inputParameter.getValue().isEmpty() ||
                        analysisValue < 0){
                    Notification.show("Please insert a valid input value", Notification.Type.TRAY_NOTIFICATION);
                }

                try {
                    analysisValue = Double.parseDouble(inputParameter.getValue());

                    String stockID = (String) stockSelect.getValue();

                    reportLayout.removeAllComponents();
                    charts.clear();

                    for(int i = 0; i < analysisIDs.size(); i++){

                        if(analysisIDs.get(i).equals(stockID)) continue;

                        graphData = nnGamingFacade.getAnalysisData(startDate,endDate,stockID, GameTypes.ANALYSIS_GAME,
                                analysisValue,analysisIDs.get(i));

                        AnalysisChart analysisChart = new AnalysisChart(stockID);
                        charts.add(analysisChart);
                        analysisChart.addSeries(stockID + " - Actual Stock Price", graphData, ACTUAL_INDEX);
                        analysisChart.addSeries(stockID+" - Stock Price if "+analysisIDs.get(i)+" average price = "+analysisValue,
                                graphData,PREDICTED_INDEX);
                        reportLayout.addComponent(analysisChart);

                    }


                }catch (NumberFormatException e){
                    Notification.show("Please insert a valid input value", Notification.Type.TRAY_NOTIFICATION);
                }


            }

        });
    }

    private void addInitialCharts(){

        String stockID = (String) stockSelect.getValue();

        for(int i = 0; i < analysisIDs.size(); i++){

            if(analysisIDs.get(i).equals(stockID)) continue;

            graphData = nnGamingFacade.getAnalysisData(startDate,endDate,stockID, GameTypes.ANALYSIS_GAME,
                        analysisValue,analysisIDs.get(i));

            AnalysisChart analysisChart = new AnalysisChart(stockID);
            charts.add(analysisChart);
            analysisChart.addSeries(stockID + " - Actual Stock Price", graphData, ACTUAL_INDEX);
            analysisChart.addSeries(stockID+" - Stock Price if "+analysisIDs.get(i)+" average price = "+analysisValue,
                    graphData,PREDICTED_INDEX);
            reportLayout.addComponent(analysisChart);

        }

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
