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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.investovator.ui.nngaming.eventinterfaces.PortfolioUpdateEvent;
import org.investovator.ui.nngaming.eventinterfaces.SymbolChangeEvent;
import org.investovator.ui.nngaming.eventobjects.Order;
import org.investovator.ui.nngaming.utils.GameDataHelper;
import org.investovator.ui.utils.Session;

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

    private EventBroadcaster eventBroadcaster;
    private List<SymbolChangeEvent> symbolListeners;
    private List<PortfolioUpdateEvent> portfolioListeners;

    public QuoteUI() {

        setupUI();
        eventBroadcaster = EventBroadcaster.getInstance();

        symbolListeners = new ArrayList<>();
        portfolioListeners = new ArrayList<>();

    }


    private void setupUI(){

        //Trade Button
        tradeButton = new Button("Place Order");
        tradeButton.addClickListener(tradeButtonClickListener);
        tradeButton.setWidth("100%");

        //Price Layout
        price = new TextField("Price");
        price.addValueChangeListener(priceValueChangedListener);
        price.setImmediate(true);
        price.setWidth("100%");

        amount= new Label();
        amount.setCaption("Amount");
        amount.addStyleName("outlined");
        amount.setWidth("100%");

        stocks = new TextField("Stocks");
        stocks.addValueChangeListener(stocksChangedListener);
        stocks.setImmediate(true);
        stocks.setWidth("100%");

        //Stock Select
        stockSelect = new ComboBox();
        stockSelect.setCaption("Select stock to trade");
        stockSelect.setNullSelectionAllowed(false);
        stockSelect.setImmediate(true);
        stockSelect.setWidth("100%");
        stockSelect.addValueChangeListener(selectSymbolValueChange);

        VerticalLayout column1 = new VerticalLayout();
        column1.setSpacing(true);
        column1.setWidth("100%");
        column1.addComponent(stockSelect);
        column1.setComponentAlignment(stockSelect,Alignment.MIDDLE_LEFT);
        column1.addComponent(price);
        column1.setComponentAlignment(price,Alignment.MIDDLE_LEFT);
        column1.addComponent(amount);
        column1.setComponentAlignment(amount,Alignment.MIDDLE_LEFT);

        //Side Select
        sideSelect = new ComboBox("Select Order Type");
        sideSelect.addItem("Buy Order");
        sideSelect.addItem("Sell Order");
        sideSelect.select("Buy Order");
        isBuy=true;
        sideSelect.setNullSelectionAllowed(false);
        sideSelect.addValueChangeListener(sideSelectValueChangeListener);
        sideSelect.setImmediate(true);
        sideSelect.setWidth("100%");

        VerticalLayout column2 = new VerticalLayout();

        column2.setSpacing(true);
        column2.setWidth("100%");
        column2.addComponent(sideSelect);
        column2.setComponentAlignment(sideSelect,Alignment.MIDDLE_RIGHT);
        column2.addComponent(stocks);
        column2.setComponentAlignment(stocks,Alignment.MIDDLE_RIGHT);
        column2.addComponent(tradeButton);
        column2.setComponentAlignment(tradeButton,Alignment.BOTTOM_RIGHT);

        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(column1);
        layout.addComponent(column2);
        layout.setSpacing(true);

        this.addComponent(layout);
        this.setComponentAlignment(layout,Alignment.MIDDLE_RIGHT);
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false,false,false,true));
        this.setImmediate(true);
        this.setWidth("100%");

    }

    public void update(){

        ArrayList<String> stockIDs = GameDataHelper.getInstance().getStockList();

        for (String stock : stockIDs) {
            stockSelect.addItem(stock);
        }
        stockSelect.setNullSelectionAllowed(false);

        stockSelect.setValue(stockIDs.get(0));

    }

    public void addSymbolListener(SymbolChangeEvent listener){
        this.symbolListeners.add(listener);
    }

    private void notifyListeners(String selectedStock){
        for (int i = 0; i < symbolListeners.size(); i++) {
            symbolListeners.get(i).onSymbolChange(selectedStock);
        }
    }

    public void addPortfolioListener(PortfolioUpdateEvent listener){
        this.portfolioListeners.add(listener);
    }

    private void notifyListeners(boolean update){
        for (int i = 0; i < portfolioListeners.size(); i++) {
            portfolioListeners.get(i).onPortfolioUpdate(update);
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
               try {
                   orderPrice = Float.parseFloat(valueString);
                   setAmount();
               } catch (NumberFormatException e){
                   Notification.show("Please enter a valid Stock Price", Notification.Type.TRAY_NOTIFICATION);
               }

            }
        }
    };

    Property.ValueChangeListener stocksChangedListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            if(!(valueString.isEmpty())){
                try{
                    orderStockCount = Integer.parseInt(valueString);
                    setAmount();
                } catch (NumberFormatException e){
                    Notification.show("Please enter a valid Stock Quantity");
                }

            }
        }
    };


    Button.ClickListener tradeButtonClickListener = new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {

            if((stocks.getValue().isEmpty()) || (price.getValue().isEmpty() || (stocks.getValue().equals("0"))
            || (price.getValue().equals("0"))) || (stockSelect.getValue() == null)){
                Notification.show("Please enter a valid Stock Price & Quantity", Notification.Type.TRAY_NOTIFICATION);

                price.setValue("");
                stocks.setValue("");
                amount.setValue("");
            }

            try{
                 orderPrice = Float.parseFloat(price.getValue());
                 orderStockCount = Integer.parseInt(stocks.getValue());

                 if(orderPrice < 0 || orderStockCount < 0){
                     Notification.show("Please enter a valid Stock Price & Quantity", Notification.Type.TRAY_NOTIFICATION);
                     return;
                 }

                 price.setValue("");
                 stocks.setValue("");
                 amount.setValue("");

                 String userName = Session.getCurrentUser();
                 notifyListeners(true);
                 eventBroadcaster.setEvent(new Order(userName,selectedStock, isBuy, orderPrice, orderStockCount));
            } catch (NumberFormatException e){
                Notification.show("Please enter a valid Stock Price & Quantity", Notification.Type.TRAY_NOTIFICATION);
            }

        }
    };

    Property.ValueChangeListener selectSymbolValueChange = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            String valueString = (String) valueChangeEvent.getProperty().getValue();
            selectedStock = valueString;

            notifyListeners(selectedStock);

            eventBroadcaster.setEvent(new Object());

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


}
