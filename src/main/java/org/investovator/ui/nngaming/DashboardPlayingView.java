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

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.UserDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.ui.agentgaming.QuoteUI;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.util.Collection;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class DashboardPlayingView extends DashboardPanel {


    //Layout Components
    GridLayout content;
    Table watchListTable;
    Chart currentPriceChart;
    QuoteUI quoteUI;

    CompanyDataImpl companyData;

    boolean simulationRunning = false;

    public DashboardPlayingView() {

        createUI();

        try {
            companyData = new CompanyDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private void createUI(){



        //Setup Layout
        content = new GridLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.setRows(2);
        content.setColumns(2);


        //QuoteUI
      //  quoteUI = new QuoteUI(companyData);


        Button test = new Button("Start");
        Button stop = new Button("Stop");


        test.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //testing

                simulationRunning = true;

            }
        });


        stop.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

               // simulationFacade.terminateSimulation();
            }
        });


        watchListTable = getTable();
        currentPriceChart = getChart();
        quoteUI = new QuoteUI(companyData);

        VerticalLayout buttons = new VerticalLayout();
        buttons.addComponent(test);
        buttons.addComponent(stop);



        //Adding to main layout
        content.addComponent(watchListTable);
        content.addComponent(currentPriceChart);
        content.addComponent(quoteUI);
        content.setComponentAlignment(watchListTable,Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(currentPriceChart,Alignment.MIDDLE_CENTER);
        //content.addComponent(buttons);

        content.setSizeFull();

        this.setSizeFull();

        this.setContent(content);

    }

    protected Table getTable() {

        /*BeanContainer<String, StockItemBean> watchList = new BeanContainer<String, StockItemBean>(StockItemBean.class);
        watchList.setBeanIdProperty("stockID");

        StockItemBean stockItemBean = new StockItemBean();
        stockItemBean.setStockID("GOOG");
        stockItemBean.setLastAsk(125.4f);
        stockItemBean.setLastBid(100);
        stockItemBean.setMarketPrice(102.5f);*/

        Table table = new Table();

        table.setSizeFull();
        table.setWidth("90%");
        table.setSelectable(true);
        table.setImmediate(true);

        //table.setVisibleColumns(new String[]{"stockID","bestBid","bestAsk","marketPrice"});

        return table;
    }


    final ListSeries series = new ListSeries(0);





    protected Chart getChart() {

        final Chart chart = new Chart();
        chart.setHeight("350px");
        chart.setWidth("90%");
        chart.setCaption("Share Price Summary");

        final Configuration configuration = new Configuration();
        configuration.setTitle("Last Traded Price");

        configuration.getChart().setType(ChartType.SPLINE);
        configuration.disableCredits();

        configuration.setSeries(series);

        chart.drawChart(configuration);


        return chart;
    }


    @Override
    public void onEnter() {



        quoteUI.update();

        simulationRunning = true;

        Collection<String> availableStocks = null;
        try {
            availableStocks = new UserDataImpl().getWatchList(Authenticator.getInstance().getCurrentUser());
            for(String stock : availableStocks){
                //simulationFacade.addListener(stock,watchList);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }


}
