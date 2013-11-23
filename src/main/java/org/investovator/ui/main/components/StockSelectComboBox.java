package org.investovator.ui.main.components;

import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;

import java.util.ArrayList;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class StockSelectComboBox extends ComboBox {

    private String selectedStock;

    public StockSelectComboBox(){

        setNullSelectionAllowed(false);
        setWidth("100%");
        setImmediate(true);

        addValueChangeListener(new ValueChangeListener(){
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                selectedStock = getValue().toString();
            }
        });
    }

    public String getSelectedStock(){
        return selectedStock;
    }

    public void update(){

        try {
            CompanyData companyData = new CompanyDataImpl();
            ArrayList<String> stocks = companyData.getAvailableStockIds();

            for(String stock : stocks ){
                addItem(stock);
            }
            select(stocks.get(0));

        } catch (DataAccessException e) {
            e.printStackTrace();
        }


    }

}
