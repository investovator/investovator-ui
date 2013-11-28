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
    ComboBox parameterSelect;

    Button chartUpdate;

    DateField startDatePicker;
    DateField endDatePicker;

    Date startDate = null;
    Date endDate = null;

    private double analysisValue = 50;


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

        configBar.addComponent(parameterSelect);
        configBar.addComponent(inputParameter);
        configBar.addComponent(startDatePicker);
        configBar.addComponent(endDatePicker);
        configBar.addComponent(chartUpdate);

        configBar.setExpandRatio(parameterSelect,4);
        configBar.setExpandRatio(startDatePicker,2);
        configBar.setExpandRatio(inputParameter,2);
        configBar.setExpandRatio(endDatePicker,2);
        configBar.setExpandRatio(chartUpdate,2);

        configBar.setCaption("Stock Price Analysis");

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
        parameterSelect = new ComboBox();
        parameterSelect.setCaption("Select Parameter");
        parameterSelect.setImmediate(true);
        parameterSelect.setNullSelectionAllowed(false);
    }

    private void setDefaults() {

        stockIDs = gameDataHelper.getStockList();
        analysisIDs = gameDataHelper.getAnalysisParameters();
        graphData = new ArrayList<>();

        ArrayList<TradingDataAttribute> attributes = new ArrayList<>();
        attributes.add(TradingDataAttribute.LOW_PRICE);

        Date[] dates = nnGamingFacade.getDateRange(stockIDs.get(0));

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

        for(int i = 0; i < analysisIDs.size(); i++){
            parameterSelect.addItem(analysisIDs.get(i)+" - Stock Price");
        }
        parameterSelect.setValue(analysisIDs.get(0)+" - Stock Price");
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
        inputParameter.setValue(String.valueOf(analysisValue));
        inputParameter.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                analysisValue = Double.parseDouble(inputParameter.getValue());
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

                if(inputParameter.getValue().equals("0") || inputParameter.getValue().isEmpty()){
                    Notification.show("Please insert a valid input value", Notification.Type.TRAY_NOTIFICATION);
                }
                else{
                    String analysisParam = (String) parameterSelect.getValue();
                    analysisParam = analysisParam.substring(0,analysisParam.indexOf(" "));

                    reportLayout.removeAllComponents();
                    charts.clear();

                    for(int i = 0; i < stockIDs.size(); i++){

                        if(stockIDs.get(i).equals(analysisParam)) continue;

                        graphData = nnGamingFacade.getAnalysisData(startDate,endDate,stockIDs.get(i), GameTypes.ANALYSIS_GAME,
                                analysisValue,analysisParam);

                        AnalysisChart analysisChart = new AnalysisChart(stockIDs.get(i));
                        charts.add(analysisChart);
                        analysisChart.addSeries(stockIDs.get(i) + " - Actual Stock Prices", graphData, ACTUAL_INDEX);
                        analysisChart.addSeries(stockIDs.get(i)+" - Predicted Stock Prices",graphData,PREDICTED_INDEX);
                        reportLayout.addComponent(analysisChart);

                    }
                }

            }
        });
    }

    private void addInitialCharts(){

        String analysisParam = (String) parameterSelect.getValue();
        analysisParam = analysisParam.substring(0,analysisParam.indexOf(" "));

        for(int i = 0; i < stockIDs.size(); i++){

            if(stockIDs.get(i).equals(analysisParam)) continue;

            graphData = nnGamingFacade.getAnalysisData(startDate,endDate,stockIDs.get(i), GameTypes.ANALYSIS_GAME,
                        analysisValue,analysisParam);

            AnalysisChart analysisChart = new AnalysisChart(stockIDs.get(i));
            charts.add(analysisChart);
            analysisChart.addSeries(stockIDs.get(i) + " - Actual Stock Prices", graphData, ACTUAL_INDEX);
            analysisChart.addSeries(stockIDs.get(i)+" - Predicted Stock Prices",graphData,PREDICTED_INDEX);
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
