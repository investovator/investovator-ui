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
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.analysis.MultiPlotTimeSeriesChart;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.*;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class DashboardAnalysisView extends DashboardPanel {

    VerticalLayout layout;
    GridLayout reportLayout;
    HorizontalLayout configBar;
    HashMap<String, MultiPlotTimeSeriesChart> charts;

    TextField inputParameter;
    ComboBox parameterSelect;

    Button chartUpdate;

    DateField startDatePicker;
    DateField endDatePicker;

    /**
     * Layout Components *
     */

    private String stockID = "SAMP";

    Date startDate = null;
    Date endDate = null;
    Date dateRange[] = null;


    public DashboardAnalysisView() {

        setHeight("100%");
        charts = new HashMap<>();

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

        configBar.addComponent(parameterSelect);
        configBar.addComponent(startDatePicker);
        configBar.addComponent(endDatePicker);
        configBar.addComponent(chartUpdate);

        configBar.setExpandRatio(parameterSelect,4);
        configBar.setExpandRatio(startDatePicker,2);
        configBar.setExpandRatio(endDatePicker,2);
        configBar.setExpandRatio(chartUpdate,2);


        layout.addComponent(configBar);
        layout.addComponent(reportLayout);

        this.setContent(layout);
    }


    private void setDefaults() {

        try {
            ArrayList<String> stocks = new CompanyDataImpl().getAvailableStockIds();  //parameters

            for(String stock : stocks){
                parameterSelect.addItem(stock);
            }
            parameterSelect.select(stocks.get(0));
            stockID = stocks.get(0);

            parameterSelect.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    stockID =  parameterSelect.getValue().toString();
                    try {
                        configBar.setCaption(stockID + " - "+ new CompanyDataImpl().getCompanyName(stockID));
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }

                    for(String chart : charts.keySet()){
                        //todo
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
            java.util.Calendar tmp = java.util.Calendar.getInstance();
            tmp.setTime(endDate);
            tmp.add(java.util.Calendar.MONTH, -1);
            startDate = tmp.getTime();


        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

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

    private void createParamInputField(){

        inputParameter = new TextField("Enter the Input Value");

        inputParameter.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                //todo
            }
        });


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

    private void createChartUpdateButton() {
        chartUpdate = new Button("Update Charts");

        chartUpdate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                for (String chart : charts.keySet()) {
                    //todo
                }
            }
        });
    }


    @Override
    public void onEnter() {

        if (!loaded) {
            loaded = true;
            //todo
        }

    }

    boolean loaded = false;
}
