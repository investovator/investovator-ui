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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.investovator.ui.nngaming.beans.OrderBean;
import org.investovator.ui.nngaming.eventobjects.TableData;
import org.investovator.ui.utils.dashboard.DashboardPanel;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class DashboardPlayingView extends DashboardPanel implements BroadcastEvent, SymbolChangeEvent{


    //Layout Components
    VerticalLayout content;

    Table orderBookSell;
    Table orderBookBuy;
    BasicChart currentPriceChart;
    QuoteUI quoteUI;


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
        content.setSpacing(true);

        HorizontalLayout row1 = new HorizontalLayout();
        HorizontalLayout row2 = new HorizontalLayout();

        row1.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        row2.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        row1.setWidth("100%");
        row2.setWidth("100%");

        row1.setHeight("60%");
        row2.setHeight("35%");

        content.addComponent(row1);
        content.addComponent(row2);

        content.setExpandRatio(row1, 55);
        content.setExpandRatio(row2, 45);

        HorizontalLayout orderBookLayout = new HorizontalLayout();

        currentPriceChart = new BasicChart();
        orderBookSell = getSellSideTable();
        orderBookBuy = getBuySideTable();
        quoteUI = new QuoteUI();


        orderBookLayout.addComponent(orderBookSell);
        orderBookLayout.addComponent(orderBookBuy);

        row1.addComponent(currentPriceChart);
        row2.addComponent(orderBookLayout);
        row2.addComponent(quoteUI);

        orderBookLayout.addStyleName("center-caption");
        quoteUI.addStyleName("center-caption");
        currentPriceChart.addStyleName("center-caption");

        content.setSizeFull();

        this.setSizeFull();

        this.setContent(content);

    }

    private Table getSellSideTable() {

        BeanItemContainer<OrderBean> beans = new BeanItemContainer<OrderBean>(OrderBean.class);
        Table orderBookSell = new Table("Sell Order Side", beans);

        orderBookSell.setHeight("100%");
        orderBookSell.setWidth("45%");
        orderBookSell.setSelectable(true);
        orderBookSell.setPageLength(10);
        orderBookSell.setImmediate(true);

        return orderBookSell;
    }

    private Table getBuySideTable() {

        BeanItemContainer<OrderBean> beans = new BeanItemContainer<OrderBean>(OrderBean.class);

        Table orderBookBuy = new Table("Buy Order Side",beans);

        orderBookBuy.setHeight("100%");
        orderBookBuy.setWidth("45%");
        orderBookBuy.setSelectable(true);
        orderBookBuy.setPageLength(10);
        orderBookBuy.setImmediate(true);

        return orderBookBuy;
    }

    @Override
    public void onEnter() {

        quoteUI.update();

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


            for(int i = 0; i < ((TableData) object).getStockBeanListBuy().get(stockIndex).size(); i++){

                buyBeans.addItem(((TableData) object).getStockBeanListBuy().get(stockIndex).get(i));

            }

            for(int i = 0; i < ((TableData) object).getStockBeanListSell().get(stockIndex).size(); i++){

                sellBeans.addItem(((TableData) object).getStockBeanListSell().get(stockIndex).get(i));

            }

            updateTables(buyBeans, sellBeans);

        }

        /*if (currentPriceChart.isConnectorEnabled()) {
            getSession().lock();
            try {

                currentPriceChart.addPointToChart();

            } finally {
                getSession().unlock();
            }
        }*/

    }
}
