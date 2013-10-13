package org.investovator.ui.agentgaming;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Iterator;
import java.util.Set;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class StockSelectView implements WizardStep {

    String[] selectedStocks;
    TwinColSelect stockSelectList;
    VerticalLayout content;

    public StockSelectView() {
        stockSelectList = new TwinColSelect("Select Stocks for Game");
        stockSelectList.addItem("SAMP");
        stockSelectList.addItem("GOOG");
        stockSelectList.addItem("IBM");

        stockSelectList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                Set<String> results = (Set) valueChangeEvent.getProperty().getValue();

                Iterator resultsItr = results.iterator();
                selectedStocks = new String[results.size()];

                int i = 0;

                for (Iterator<String> iterator = results.iterator(); iterator.hasNext(); ) {
                    String next = iterator.next();
                    selectedStocks[i] = next;
                    i++;
                }


            }
        });

        content = new VerticalLayout();
        content.addComponent(stockSelectList);
    }

    @Override
    public String getCaption() {
        return "Select Stocks";
    }

    @Override
    public Component getContent() {
        return  content;
    }

    @Override
    public boolean onAdvance() {
        return true;
    }

    @Override
    public boolean onBack() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String[] getSelectedStocks(){
        return selectedStocks;
    }
}
