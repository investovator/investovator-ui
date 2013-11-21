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
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.investovator.ui.nngaming.beans.OrderBean;
import org.investovator.ui.nngaming.eventobjects.Order;
import org.investovator.ui.nngaming.utils.GameDataHelper;
import org.investovator.ui.nngaming.utils.PlayableStockManager;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class QuoteUI extends VerticalLayout implements EventListener {

    //Layout Components
    Button tradeButton;
    ComboBox stockSelect;
    ComboBox sideSelect;
    TextField price;
    TextField stocks;
    Label amount;

    //Data Variables
    String selectedStock;
    boolean isBuy;
    float orderPrice;
    int orderStockCount;

    private GameDataHelper gameDataHelper;
    private EventBroadcaster eventBroadcaster;
    private List<SymbolChangeEvent> symbolListeners;

    public QuoteUI() {

        setupUI();
        gameDataHelper = GameDataHelper.getInstance();
        eventBroadcaster = EventBroadcaster.getInstance();

        symbolListeners = new ArrayList<>();

    }


    private void setupUI(){

        //Side Select
        HorizontalLayout sideSelectLayout = new HorizontalLayout();

        sideSelect = new ComboBox("Select Order Type");
        sideSelect.addItem("Buy Order");
        sideSelect.addItem("Sell Order");
        sideSelect.select("Buy Order");
        isBuy=true;
        sideSelect.setNullSelectionAllowed(false);
        sideSelect.addValueChangeListener(sideSelectValueChangeListener);
        sideSelect.setImmediate(true);

        sideSelectLayout.setSpacing(true);
        sideSelectLayout.setSizeFull();
        sideSelectLayout.setWidth("100%");
        sideSelectLayout.addComponent(sideSelect);

        //Trade Button
        tradeButton = new Button("Place Order");
        tradeButton.addClickListener(tradeButtonClickListener);


        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

        buttonLayout.addComponent(tradeButton);

        //Price Layout
        HorizontalLayout priceLayout = new HorizontalLayout();

        price = new TextField("Price");
        price.addValueChangeListener(priceValueChangedListener);
        price.setImmediate(true);

        amount= new Label();
        amount.setCaption("Amount");
        amount.addStyleName("outlined");

        stocks = new TextField("Stocks");
        stocks.addValueChangeListener(stocksChangedListener);
        stocks.setImmediate(true);

        priceLayout.setSpacing(true);
        priceLayout.addComponent(price);
        priceLayout.addComponent(stocks);
        priceLayout.addComponent(amount);


        //Stock Select
        stockSelect = new ComboBox();
        stockSelect.setCaption("Select stock to trade");
        stockSelect.setNullSelectionAllowed(false);
        stockSelect.setWidth("100%");
        stockSelect.setImmediate(true);
        stockSelect.addValueChangeListener(selectSymbolValueChange);

        this.addComponent(stockSelect);
        this.addComponent(sideSelectLayout);
        this.addComponent(priceLayout);
        this.addComponent(buttonLayout);

        this.setImmediate(true);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        this.setWidth("90%");
        this.setMargin(true);

    }

    public void update(){

        ArrayList<String> stockIDs = PlayableStockManager.getInstance().getStockList();

        for (String stock : stockIDs) {
            stockSelect.addItem(stock);
        }
        stockSelect.setNullSelectionAllowed(false);

    }

    public void addListener(SymbolChangeEvent listener){
        this.symbolListeners.add(listener);
    }

    private void notifyListeners(String selectedStock){
        for (int i = 0; i < symbolListeners.size(); i++) {
            symbolListeners.get(i).onSymbolChange(selectedStock);
        }
    }

    private void setAmount(){
        amount.setValue( Float.toString(orderPrice*orderStockCount));
    }


    Property.ValueChangeListener priceValueChangedListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            if(!(valueString.isEmpty())) {
                orderPrice = Float.parseFloat(valueString);
                setAmount();
            }
        }
    };

    Property.ValueChangeListener stocksChangedListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            if(!(valueString.isEmpty())){
                orderStockCount = Integer.parseInt(valueString);
                setAmount();
            }
        }
    };


    Button.ClickListener tradeButtonClickListener = new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {

            if((stocks.getValue().isEmpty()) || (price.getValue().isEmpty() || (stocks.getValue().equals("0"))
            || (price.getValue().equals("0"))) || (stockSelect.getValue() == null)){
                Notification notification = new Notification("Please enter a valid Stock Price & Amount",
                        Notification.Type.ERROR_MESSAGE);
                notification.setPosition(Position.BOTTOM_RIGHT);
                notification.show(Page.getCurrent());
            }

            else{
                int status = checkExecutableStatus();

                if(status == 1){
                    //execute order
                    //let dashboard playing view know
                    //update portfolio
                    System.out.println("Order Can be Executed");
                }
                else if (status == 0) {
                    price.setValue("");
                    stocks.setValue("");
                    amount.setValue("");
                    eventBroadcaster.setEvent(new Order(selectedStock, isBuy, orderPrice, orderStockCount));
                }
                else {

                }
            }

        }
    };

    Property.ValueChangeListener selectSymbolValueChange = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            String valueString = (String) valueChangeEvent.getProperty().getValue();
            selectedStock = valueString;

            notifyListeners(selectedStock);

        }
    };

    Property.ValueChangeListener sideSelectValueChangeListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());

            if(!(valueString.isEmpty())){

                if(valueString.equals("Buy Order")) {
                    isBuy = true;
                }
                else if(valueString.equals("Sell Order")) {
                    isBuy = false;
                }
            }

        }
    };

    private int checkExecutableStatus(){

        int status = -1;
        ArrayList<String> stockList = eventBroadcaster.getStocks();

        if(isBuy){
            ArrayList<ArrayList<OrderBean>> sellStockBeanList = gameDataHelper.getStockBeanListSell();

            if(sellStockBeanList.size() <= stockList.indexOf(selectedStock)){
                 status = -1;
            }

            else{
                ArrayList<OrderBean> sellBeanList = sellStockBeanList.get(stockList.indexOf(selectedStock));

                if(sellBeanList.get(0).getOrderValue() <= orderPrice){
                    status = 1;
                }
                else {
                    status = 0;
                }
            }

        }
        else{
            ArrayList<ArrayList<OrderBean>> buyStockBeanList = gameDataHelper.getStockBeanListBuy();

            if(buyStockBeanList.size() <= stockList.indexOf(selectedStock)){
                status = -1;
            }
            else{
                ArrayList<OrderBean> buyBeanList = buyStockBeanList.get(stockList.indexOf(selectedStock));

                if(buyBeanList.get(0).getOrderValue() >= orderPrice){
                    status = 1;
                }
                else {
                    status = 0;
                }
            }
        }

        return status;
    }

}
