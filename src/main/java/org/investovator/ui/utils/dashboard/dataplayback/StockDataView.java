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


package org.investovator.ui.utils.dashboard.dataplayback;

/**
 * @author: ishan
 * @version: ${Revision}
 */

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.timeline.Timeline;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.data.BogusHistoryDataGenerator;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.utils.dashboard.DashboardPanel;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Should show a view like http://demo.vaadin.com/charts/#ForumTrends
 */
public class StockDataView extends DashboardPanel {

    VerticalLayout content;

    //charts to be shown
    protected Timeline mainChart;

    public StockDataView( ) {
        this.content = new VerticalLayout();
        content.setSizeFull();
    }


    @Override
    public void onEnter() {
        content.removeAllComponents();

        content.addComponent(setUpTopBar());
        content.addComponent(setUpChart());

        this.setContent(content);
    }

    private HorizontalLayout setUpTopBar(){
        HorizontalLayout components=new HorizontalLayout();
//        components.setSizeFull();

        //create the stocks drop down list
        final ComboBox stocksList=new ComboBox();
        components.addComponent(stocksList);

        stocksList.setCaption("Stock");
        stocksList.setNullSelectionAllowed(false);
        for(String stock: DataPlaybackEngineStates.playingSymbols){
            stocksList.addItem(stock);
        }
        stocksList.select(stocksList.getItemIds().toArray()[0]);

        //Data items list
        final NativeSelect dataItems=new NativeSelect();
        components.addComponent(dataItems);
        dataItems.setCaption("Data: ");
//        dataItems.addItem(OrderType.BUY);
//        dataItems.addItem(OrderType.SELL);

        for(TradingDataAttribute attr:setSelectableAttributes()){
            dataItems.addItem(attr);
        }
//        dataItems.setWidth("90%");
        dataItems.setNullSelectionAllowed(false);
        dataItems.select(dataItems.getItemIds().toArray()[0]);
        dataItems.setImmediate(true);

        return components;
    }

    private Timeline setUpChart(){
        Timeline timeline=new Timeline("Stock Data");

        timeline.setSizeFull();
        timeline.setHeight(8,Unit.CM);
        timeline.setId("timeline");
        timeline.setUniformBarThicknessEnabled(true);

        timeline.setChartMode(Timeline.ChartMode.LINE);

        //disable bar chart
        timeline.setChartModeVisible(Timeline.ChartMode.BAR,false);


        IndexedContainer data;
        data = createIndexedContainer();

        // Add data sources
//        timeline.addGraphDataSource(data);
        timeline.addGraphDataSource(data,Timeline.PropertyId.TIMESTAMP,Timeline.PropertyId.VALUE);
        timeline.setGraphCaption(data, "Stock");
        timeline.setGraphOutlineColor(data, new Color(0x00, 0xb4, 0xf0));
        timeline.setGraphFillColor(data, null);
        timeline.setVerticalAxisLegendUnit(data, "Price");

        // Set the date range
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        timeline.setVisibleDateRange(cal.getTime(), new Date());


         timeline.setSizeFull();

        return timeline;
    }

    public TradingDataAttribute[] setSelectableAttributes(){
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        attributes.add(TradingDataAttribute.PRICE);
        attributes.add(TradingDataAttribute.CLOSING_PRICE);


        return attributes.toArray(new TradingDataAttribute[attributes.size()]);
    }

    /**
     * Creates an indexed container with two properties: value and timestamp.
     *
     * @return a container with "value, timestamp" items.
     */
    private IndexedContainer createIndexedContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(Timeline.PropertyId.VALUE, Float.class,
                new Float(0));
        container.addContainerProperty(Timeline.PropertyId.TIMESTAMP,
                java.util.Date.class, null);


//        // Add some random data to the container
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, -1);
//        Date today = new Date();
//        Random generator = new Random();
//
//        while(cal.getTime().before(today)){
//            // Create  a point in time
//            Item item = container.addItem(cal.getTime());
//
//            // Set the timestamp property
//            item.getItemProperty(Timeline.PropertyId.TIMESTAMP)
//                    .setValue(cal.getTime());
//
//            // Set the value property
//            item.getItemProperty(Timeline.PropertyId.VALUE)
//                    .setValue(generator.nextFloat());
//
//            cal.add(Calendar.DAY_OF_MONTH, 1);
//        }

        //my data test

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        try {
            StockTradingData stockTradingData= new BogusHistoryDataGenerator().
                    getTradingData(CompanyStockTransactionsData.DataType.OHLC,
                            "APPL", DateUtils.decrementTimeByDays(30, new Date()), new Date(), 100, attributes);
            //add the data
            //sort first
            Collection<Date> unsorted = stockTradingData.getTradingData().keySet();
            List<Date> list=new ArrayList<Date>(unsorted);
            Collections.sort(list);

            for(Date date:list){
                // Create  a point in time
                Item item = container.addItem(date);

                // Set the timestamp property
                item.getItemProperty(Timeline.PropertyId.TIMESTAMP)
                        .setValue(date);


                // Set the value property
                item.getItemProperty(Timeline.PropertyId.VALUE)
                        .setValue(Float.parseFloat(stockTradingData.getTradingData().get(date).
                                get(TradingDataAttribute.PRICE)));




            }
        } catch (DataAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return container;
    }

}
