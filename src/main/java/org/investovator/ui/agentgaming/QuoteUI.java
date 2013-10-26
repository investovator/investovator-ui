package org.investovator.ui.agentgaming;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import net.sourceforge.jasa.market.Order;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.jasa.api.JASAFacade;
import org.investovator.ui.authentication.Authenticator;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class QuoteUI extends VerticalLayout {

    //External Data
    CompanyData companyData;

    //Layout Components
    Button tradeButton;
    ComboBox stockSelect;
    ComboBox sideSelect;
    ComboBox orderTypeSelect;
    ComboBox validitySelect;
    TextField price;
    TextField stocks;
    Label amount;

    //Data Variables
    String selectedStock;
    boolean isBuy;
    float orderPrice;
    int orderStockCount;


    public QuoteUI(CompanyData companyData) {
        this.companyData = companyData;
        setupUI();
        JASAFacade.getMarketFacade().AddUserAgent("testUser1",10000000);
    }


    private void setupUI(){

        //Side Select
        HorizontalLayout sideSelectLayout = new HorizontalLayout();

        sideSelect = new ComboBox("Select side");
        sideSelect.addItem(OrderSide.BUY);
        sideSelect.addItem(OrderSide.SELL);
        sideSelect.select(OrderSide.BUY);
        isBuy=true;
        sideSelect.setNullSelectionAllowed(false);

        orderTypeSelect = new ComboBox("Order Type");
        orderTypeSelect.addItem(OrderType.MARKET_ORDER);
        orderTypeSelect.addItem(OrderType.LIMIT_ORDER);
        orderTypeSelect.select(OrderType.LIMIT_ORDER);
        orderTypeSelect.setNullSelectionAllowed(false);


        validitySelect = new ComboBox("Order Validity");
        validitySelect.addItem(OrderValidity.DAY_ORDER);
        validitySelect.addItem(OrderValidity.VALID_UNTIL_CANCEL);
        validitySelect.addItem(OrderValidity.CUSTOM_ORDER);
        validitySelect.select(OrderValidity.DAY_ORDER);
        validitySelect.setNullSelectionAllowed(false);

        sideSelectLayout.setSpacing(true);
        sideSelectLayout.setSizeFull();
        sideSelectLayout.addComponent(sideSelect);
        sideSelectLayout.addComponent(orderTypeSelect);
        sideSelectLayout.addComponent(validitySelect);

        //Trade Button
        tradeButton = new Button("Place Order");
        tradeButton.addClickListener(tradeButtonClickListener);


        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setMargin(true);
        buttonLayout.setWidth("100%");
        buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

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

        try {
            for (String stock : companyData.getAvailableStockIds()) {
                stockSelect.addItem(stock);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

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
            JASAFacade.getMarketFacade().putLimitOrder(Authenticator.getInstance().getCurrentUser(), selectedStock, orderStockCount, orderPrice, isBuy);
        }
    };

    Property.ValueChangeListener selectSymbolValueChange = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            selectedStock = valueString;

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


enum OrderValidity{
    DAY_ORDER,
    VALID_UNTIL_CANCEL,
    CUSTOM_ORDER;

    @Override
    public String toString() {
        switch (this)
        {
            case DAY_ORDER: return "End of Day";
            case VALID_UNTIL_CANCEL: return "Good Till Cancelled" ;
            case CUSTOM_ORDER: return "Custom";
        }
        return null;
    }
}


enum OrderType{
    MARKET_ORDER,
    LIMIT_ORDER;

    @Override
    public String toString() {
        switch (this)
        {
            case MARKET_ORDER: return "Market Order";
            case LIMIT_ORDER: return "Limit Order" ;
        }
        return null;
    }
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


