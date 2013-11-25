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

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.investovator.ui.nngaming.beans.OrderBean;
import org.investovator.ui.nngaming.eventinterfaces.BroadcastEvent;
import org.investovator.ui.nngaming.eventinterfaces.SymbolChangeEvent;
import org.investovator.ui.nngaming.eventobjects.GraphData;
import org.investovator.ui.nngaming.eventobjects.TableData;
import org.investovator.ui.utils.dashboard.DashboardPanel;
/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class DashboardPlayingView extends DashboardPanel implements BroadcastEvent, SymbolChangeEvent {


    //Layout Components
    VerticalLayout content;

    Table orderBookSell;
    Table orderBookBuy;
    BasicChart currentPriceChart;
    QuantityChart quantityChart;
    QuoteUI quoteUI;
    UserPortfolio userPortfolio;


    private EventBroadcaster eventBroadcaster;
    private String selectedStock;

    boolean simulationRunning = false;

    public DashboardPlayingView() {

        eventBroadcaster = EventBroadcaster.getInstance();
        eventBroadcaster.addListener(this);

        createUI();

        quoteUI.addListener(this);
    }


    private void createUI(){

        //Setup Layout
        content = new VerticalLayout();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
       // content.setSpacing(true);

        HorizontalLayout row1 = new HorizontalLayout();
        HorizontalLayout row2 = new HorizontalLayout();
        HorizontalLayout row3 = new HorizontalLayout();

        row1.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        row2.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        row1.setWidth("100%");
        row2.setWidth("100%");
        row3.setWidth("100%");

        row1.setHeight(70, Unit.MM);
        row2.setHeight(50,Unit.MM);
        row3.setHeight(45,Unit.MM);

        row1.setMargin(new MarginInfo(true, true, false, true));
        row2.setMargin(new MarginInfo(true, true, false, true));
        row3.setMargin(new MarginInfo(true, true, true, true));

        content.addComponent(row1);
        content.addComponent(row2);
        content.addComponent(row3);

        content.setExpandRatio(row1, 1.3f);
        content.setExpandRatio(row2, 1.3f);
        content.setExpandRatio(row3, 1.3f);

        GridLayout orderBookLayout = new GridLayout(2,1);

        currentPriceChart = new BasicChart();
        quantityChart = new QuantityChart();
        orderBookSell = getSellSideTable();
        orderBookSell.addStyleName("center-caption");
        orderBookBuy = getBuySideTable();
        orderBookBuy.addStyleName("center-caption");
        quoteUI = new QuoteUI();
        userPortfolio = new UserPortfolio();

        orderBookLayout.addComponent(orderBookSell);
        orderBookLayout.addComponent(orderBookBuy);

        GridLayout bottomLayout = new GridLayout(3,1);
        bottomLayout.addComponent(orderBookLayout);
        bottomLayout.addComponent(quoteUI);
        bottomLayout.addComponent(userPortfolio);

        row1.addComponent(currentPriceChart);
        row2.addComponent(quantityChart);
        row3.addComponent(bottomLayout);

        row2.setComponentAlignment(quantityChart, Alignment.MIDDLE_CENTER);

        orderBookLayout.addStyleName("center-caption");
        quoteUI.addStyleName("center-caption");
        currentPriceChart.addStyleName("center-caption");
        userPortfolio.addStyleName("center-caption");
        quantityChart.addStyleName("center-caption");

        this.setContent(content);

    }

    private Table getSellSideTable() {

        BeanItemContainer<OrderBean> beans = new BeanItemContainer<OrderBean>(OrderBean.class);
        Table orderBookSell = new Table("Sell Order Side", beans);

        orderBookSell.setHeight("100%");
        orderBookSell.setWidth("45%");
        orderBookSell.setSelectable(true);
        orderBookSell.setPageLength(4);
        orderBookSell.setImmediate(true);

        orderBookSell.setColumnHeader("orderValue", "Order Value" );
        orderBookSell.setColumnHeader("quantity", "Quantity");

        return orderBookSell;
    }

    private Table getBuySideTable() {

        BeanItemContainer<OrderBean> beans = new BeanItemContainer<OrderBean>(OrderBean.class);

        Table orderBookBuy = new Table("Buy Order Side",beans);

        orderBookBuy.setHeight("100%");
        orderBookBuy.setWidth("45%");
        orderBookBuy.setSelectable(true);
        orderBookBuy.setPageLength(4);
        orderBookBuy.setImmediate(true);

        orderBookBuy.setColumnHeader("orderValue", "Order Value" );
        orderBookBuy.setColumnHeader("quantity", "Quantity");

        return orderBookBuy;
    }

    @Override
    public void onEnter() {

       /* if (currentPriceChart.isConnectorEnabled()) {
            getSession().lock();
            try {

                currentPriceChart.updateGraph();

            } finally {
                getSession().unlock();
            }
        }*/

        quoteUI.update();
        userPortfolio.update();

        simulationRunning = true;

    }

    @Override
    public void onSymbolChange(String selectedStock) {

        this.selectedStock = selectedStock;

    }

    private void updateTables(final Container beansBuy, final Container beansSell){

        if (orderBookBuy.isConnectorEnabled()) {
            getSession().lock();
            try {

                getUI().access(new Runnable() {
                    @Override
                    public void run() {

                        orderBookBuy.setContainerDataSource(beansBuy);
                        getUI().push();
                    }
                });

            } finally {
                getSession().unlock();
            }
        }

        if (orderBookSell.isConnectorEnabled()) {
            getSession().lock();
            try {

                getUI().access(new Runnable() {
                    @Override
                    public void run() {

                        orderBookSell.setContainerDataSource(beansSell);
                        getUI().push();
                    }
                });

            } finally {
                getSession().unlock();
            }
        }

    }

    @Override
    public void onBroadcast(Object object) {

        if(object instanceof TableData){
            int stockIndex;

            final Container buyBeans = orderBookBuy.getContainerDataSource();
            final Container sellBeans = orderBookSell.getContainerDataSource();

            if(!(buyBeans.size() == 0)){
                buyBeans.removeAllItems();
            }

            if(!(sellBeans.size() == 0)){
                sellBeans.removeAllItems();
            }

            if(selectedStock == null){
                stockIndex = 0;
            }
            else{
                stockIndex = ((TableData) object).getStockList().indexOf(selectedStock);
            }


            if(((TableData) object).getStockBeanListBuy().isEmpty() || ((TableData) object).getStockBeanListSell().isEmpty()
                    || ((TableData) object).getStockBeanListBuy().size() <= stockIndex || ((TableData) object).getStockBeanListSell()
                    .size() <= stockIndex){

                updateTables(buyBeans, sellBeans);

            }

            else{
                for(int i = 0; i < ((TableData) object).getStockBeanListBuy().get(stockIndex).size(); i++){

                    buyBeans.addItem(((TableData) object).getStockBeanListBuy().get(stockIndex).get(i));

                }

                for(int i = 0; i < ((TableData) object).getStockBeanListSell().get(stockIndex).size(); i++){

                    sellBeans.addItem(((TableData) object).getStockBeanListSell().get(stockIndex).get(i));

                }

                updateTables(buyBeans, sellBeans);
            }

        }

        if(object instanceof GraphData) {

            int currentIndex = ((GraphData) object).getCurrentIndex();

            if (currentPriceChart.isConnectorEnabled()) {
                getSession().lock();
                try {

                    currentPriceChart.addPointToChart(currentIndex);

                } finally {
                    getSession().unlock();
                }
            }

            if (quantityChart.isConnectorEnabled()) {
                getSession().lock();
                try {

                    quantityChart.addPointToChart(currentIndex);

                } finally {
                    getSession().unlock();
                }
            }
        }

    }
}
