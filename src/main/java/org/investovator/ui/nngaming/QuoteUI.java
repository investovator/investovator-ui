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
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyData;
import org.investovator.ui.nngaming.utils.PlayableStockManager;

import java.util.ArrayList;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class QuoteUI extends VerticalLayout{

    //External Data
    CompanyData companyData;

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


    public QuoteUI() {

        setupUI();

    }


    private void setupUI(){

        //Side Select
        HorizontalLayout sideSelectLayout = new HorizontalLayout();

        sideSelect = new ComboBox("Select Order Type");
        sideSelect.addItem(OrderSide.BUY);
        sideSelect.addItem(OrderSide.SELL);
        sideSelect.select(OrderSide.BUY);
        isBuy=true;
        sideSelect.setNullSelectionAllowed(false);

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
        price.setImmediate(true);
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
        stockSelect.setValue(stockIDs.get(0));

    }

    private void setAmount(){
        amount.setValue( Float.toString(orderPrice*orderStockCount));
    }


    Property.ValueChangeListener priceValueChangedListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            orderPrice = Float.parseFloat(valueString);
            setAmount();
        }
    };

    Property.ValueChangeListener stocksChangedListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            orderStockCount = Integer.parseInt(valueString);
            setAmount();
        }
    };


    Button.ClickListener tradeButtonClickListener = new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {

            //ToDO add order to book
        }
    };

    Property.ValueChangeListener selectSymbolValueChange = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            selectedStock = valueString;


            //ToDO

        }
    };

    Property.ValueChangeListener sideSelectValueChangeListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            if(valueString.equals(OrderSide.BUY)) isBuy = true;
            else isBuy=false;

        }
    };



}

enum OrderSide{
    BUY,
    SELL;

    @Override
    public String toString() {
        switch (this)
        {
            case BUY: return "Buy Order";
            case SELL: return "Sell Order" ;
        }
        return null;    }


}
