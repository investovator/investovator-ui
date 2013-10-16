package org.investovator.ui.agentgaming;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.exeptions.DataAccessException;

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


    public QuoteUI(CompanyData companyData) {
        this.companyData = companyData;
        setupUI();
    }


    private void setupUI(){

        //Side Select
        HorizontalLayout sideSelectLayout = new HorizontalLayout();

        sideSelect = new ComboBox("Select side");
        sideSelect.addItem("Buy");
        sideSelect.addItem("Sell");
        sideSelect.select("Buy");
        sideSelect.setNullSelectionAllowed(false);

        orderTypeSelect = new ComboBox("Order Type");
        orderTypeSelect.addItem("Market Order");
        orderTypeSelect.addItem("Limit Order");
        orderTypeSelect.select("Limit Order");
        orderTypeSelect.setNullSelectionAllowed(false);


        validitySelect = new ComboBox("Order Validity");
        validitySelect.addItem("End of Day");
        validitySelect.addItem("Good Till Cancelled");
        validitySelect.addItem("Custom");
        validitySelect.select("End of Day");
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

        amount= new Label();
        amount.setCaption("Amount");
        amount.addStyleName("outlined");

        stocks = new TextField("Stocks");
        priceLayout.setSpacing(true);
        priceLayout.addComponent(price);
        priceLayout.addComponent(stocks);
        priceLayout.addComponent(amount);


        //Stock Select
        stockSelect = new ComboBox();
        stockSelect.setCaption("Select stock to trade");
        stockSelect.setNullSelectionAllowed(false);
        stockSelect.setWidth("100%");

        try {
            for (String stock : companyData.getAvailableStockIds()) {
                stockSelect.addItem(stock);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        this.addComponent(stockSelect);
        this.addComponent(sideSelectLayout);
        this.addComponent(priceLayout);
        this.addComponent(buttonLayout);

        this.setImmediate(true);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        this.setWidth("90%");
        this.setMargin(true);

    }



    Button.ClickListener tradeButtonClickListener = new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    Property.ValueChangeListener selectSymbolValueChange = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            final String valueString = String.valueOf(valueChangeEvent.getProperty().getValue());
            selectedStock = valueString;

        }
    };


}
