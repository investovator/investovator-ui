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


package org.investovator.ui.dataplayback.user.dashboard.realtime;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.*;
import org.investovator.controller.GameController;
import org.investovator.controller.GameControllerImpl;
import org.investovator.controller.command.dataplayback.GetDataPlayerCommand;
import org.investovator.controller.command.exception.CommandExecutionException;
import org.investovator.controller.command.exception.CommandSettingsException;
import org.investovator.core.commons.events.GameEvent;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.Terms;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.events.PlaybackFinishedEvent;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserAlreadyJoinedException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.dataplaybackengine.player.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.ui.authentication.Authenticator;
import org.investovator.ui.dataplayback.beans.PortfolioBean;
import org.investovator.ui.dataplayback.beans.StockNamePriceBean;
import org.investovator.ui.dataplayback.util.DataPlaybackEngineStates;
import org.investovator.ui.dataplayback.util.DataPlaybackGameOverWindow;
import org.investovator.ui.utils.Session;
import org.investovator.ui.utils.UIConstants;
import org.investovator.ui.utils.dashboard.dataplayback.BasicMainView;

import java.util.Date;


/**
 * @author: ishan
 * @version: ${Revision}
 */
public class RealTimeMainView extends BasicMainView implements PlaybackEventListener{

    //decides the number of points shown in the ticker chart
    private static int TICKER_CHART_LENGTH = 10;

    protected String userName;
    private Authenticator.UserType userType;

    private DataPlayer player;

    //used to keep track of the date of the last update
    public Date lastUpdateTime;



    @Override
    public Chart buildMainChart() {
        Chart chart = new Chart();
        chart.setHeight(70,Unit.MM);


        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        tooltip.setHeaderFormat("{point.key}");
        tooltip.setPointFormat("");
        tooltip.setFooterFormat("{series.name}: 	{point.y} EUR");

        Configuration configuration = new Configuration();
        configuration.setTooltip(tooltip);
        configuration.getChart().setType(ChartType.LINE);

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        plotOptions.setEnableMouseTracking(false);
        //performance related
        plotOptions.setShadow(false);

        configuration.setPlotOptions(plotOptions);

        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%e. %b", "%b"));

        configuration.getyAxis().setTitle("Price");

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                DataSeries ls = new DataSeries();
                ls.setName(stock);

                //add dummy points to fill it up
                for(int counter=1;counter<=TICKER_CHART_LENGTH;counter++){
                    ls.add(new DataSeriesItem
                            (DateUtils.decrementTimeBySeconds((TICKER_CHART_LENGTH-counter),
                                    DataPlaybackEngineStates.gameStartDate),0));
                }

                configuration.addSeries(ls);

            }
        }

        chart.setImmediate(true);
        chart.drawChart(configuration);
        //disable trademark
        chart.getConfiguration().disableCredits();

        chart.getConfiguration().getTitle().setText("Price");
        return chart;
    }

    public void updatePortfolioTable(final String stockID){
        final BeanContainer<String,PortfolioBean> beans = (BeanContainer<String,PortfolioBean>)
                portfolioTable.getContainerDataSource();

                //if the stock is already bought
                if(beans.containsId(stockID)){
                    beans.removeItem(stockID);
                }
                try {
                    Portfolio portfolio=player.getMyPortfolio(userName);
                    //if the user has stocks
                    if(!portfolio.getShares().isEmpty() && (portfolio.getShares().get(stockID)!=null)){

                        double price = portfolio.getShares().get(stockID).get(Terms.PRICE);
                        int quantity =portfolio.getShares().get(stockID).get(Terms.QNTY).intValue();
                        beans.addBean(new PortfolioBean(stockID,price, quantity));
                    }
                } catch (UserJoinException e) {
                    Notification.show("First joint the game", Notification.Type.ERROR_MESSAGE);
                }

    }

    @Override
    public HorizontalLayout getBuySellForumButtons(final ComboBox stocksList,
                                                   final TextField quantity,final NativeSelect orderSide) {
        HorizontalLayout buttonsBar=new HorizontalLayout();
        final Button buySellButton=new Button("Buy");
        buySellButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //check for invalid orders
                boolean invalidOrder=false;
                String numericRegex="^[0-9]+$";
                //conditions to check
                if(stocksList.getValue()==null ||
                        quantity.getValue()==null ||
                        !quantity.getValue().toString().matches(numericRegex)) {
                        invalidOrder=true;

                }
                //if this is a sell order
                else if(((OrderType) orderSide.getValue())==OrderType.SELL){
                    //check if te user has this stock
                    BeanContainer<String,PortfolioBean> beans = (BeanContainer<String,PortfolioBean>)
                            portfolioTable.getContainerDataSource();

                    if(!beans.containsId(stocksList.getValue().toString())){
                        invalidOrder=true;
                    }
                }

                if(invalidOrder){
                    Notification.show("Invalid Order");
                    return;
                }

                try {
                    Boolean status= player.executeOrder(stocksList.getValue().toString(),
                            Integer.parseInt(quantity.getValue().toString()), ((OrderType) orderSide.getValue()),
                            userName);
                    //if the transaction was a success
                    if(status){
                        updatePortfolioTable(stocksList.getValue().toString());
                        //update account info
                        updateAccountBalance();
                        Notification.show("Order executed successfully", Notification.Type.TRAY_NOTIFICATION);
                    }
                    else{

                        Notification.show(status.toString());
                    }
                } catch (InvalidOrderException e) {
                    Notification.show(e.getMessage());
                } catch (UserJoinException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }});

        orderSide.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if(valueChangeEvent.getProperty().getValue()==OrderType.BUY){
                    buySellButton.setCaption("Buy");
                }
                else if(valueChangeEvent.getProperty().getValue()==OrderType.SELL){
                    buySellButton.setCaption("Sell");
                }
            }
        });


        buttonsBar.addComponent(buySellButton);

        return buttonsBar;
    }

    @Override
    public void onEnterMainView() {

        //check if a game instance exists
        if((Session.getCurrentGameInstance()==null)){
            getUI().getNavigator().navigateTo(UIConstants.USER_VIEW);
            return;
        }

        try {
            this.userName=Authenticator.getInstance().getCurrentUser();
            this.userType=Authenticator.getInstance().getMyPrivileges();
            GameController controller= GameControllerImpl.getInstance();
            GetDataPlayerCommand command=new GetDataPlayerCommand();
            controller.runCommand(Session.getCurrentGameInstance(),command );
            this.player=(RealTimeDataPlayer)command.getPlayer();
            //join the game if the user has not already done so
            if(!this.player.hasUserJoined(this.userName)){
                this.player.joinGame(this,this.userName);
            }
            //else add this as a listener
            else{
                this.player.setObserver(this);
            }

            //update the account balance
            this.updateAccountBalance();

        } catch (UserAlreadyJoinedException e) {
            e.printStackTrace();
        } catch (CommandExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CommandSettingsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private void updateTickerChart(final StockUpdateEvent event)  {

        //iterate every series in the chart at the moment
        for (Series series : mainChart.getConfiguration().getSeries()) {
            final DataSeries dSeries = (DataSeries) series;
            //if this series matches the stock events stock
            if (dSeries.getName().equalsIgnoreCase(event.getStockId())) {

                final float value;
                //if new data is available
                if(event.getData()!=null ){
                    value=event.getData().get(DataPlaybackEngineStates.gameConfig.getAttributeToMatch());
                }
                else {
                    //get the value of the last stock
                    BeanContainer<String,StockNamePriceBean> beans = (BeanContainer<String,StockNamePriceBean>)
                            stockPriceTable.getContainerDataSource();
                    value=beans.getItem(event.getStockId()).getBean().getPrice();
                }

                        if (dSeries.getData().size() > TICKER_CHART_LENGTH) {

                            dSeries.add(new DataSeriesItem(event.getTime(),value), true, true);

                        } else {
                            dSeries.add(new DataSeriesItem(event.getTime(),value));

                        }

            }

        }

    }

    private void updateStockPriceTable(final StockUpdateEvent event){

        final BeanContainer<String,StockNamePriceBean> beans = (BeanContainer<String,StockNamePriceBean>)
                stockPriceTable.getContainerDataSource();

                beans.removeItem(event.getStockId());
                beans.addBean(new StockNamePriceBean(event.getStockId(),
                        event.getData().get(DataPlaybackEngineStates.gameConfig.getAttributeToMatch())));
    }

    public void updatePieChart(final StockUpdateEvent event)
            throws PlayerStateException, UserJoinException {

        Portfolio portfolio=this.player.getMyPortfolio(this.userName);

        //since we know that there's only one data series
        final DataSeries dSeries = (DataSeries) stockPieChart.getConfiguration().getSeries().get(0);



                //if this is an update for a stock that the user has already bought
                if(portfolio.getShares().containsKey(event.getStockId())){
                    float price =event.getData().get(DataPlaybackEngineStates.gameConfig.getAttributeToMatch());
                    double quantity= portfolio.getShares().get(event.getStockId()).get(Terms.QNTY);

                    //update the chart
                    DataSeriesItem item=dSeries.get(event.getStockId());
                    item.setY(price*quantity);
                    dSeries.update(item);

                }


                dSeries.update(dSeries.get(event.getStockId()));
                stockPieChart.setImmediate(true);
                stockPieChart.drawChart();
                getUI().push();
    }


    @Override
    public void eventOccurred(GameEvent arg) {

        //if this is a stock price update
        if (arg instanceof StockUpdateEvent) {
            final StockUpdateEvent event = (StockUpdateEvent) arg;

            //update the ticker chart
            updateTickerChart(event);

            //update quantity chart
            updateQuantityChart(event);

            //if event contains data
            if(event.getData()!=null){


                //update the table
                updateStockPriceTable(event);
            }

            //todo -shows false values, does not use the latest data for calculations
            if(lastUpdateTime!=null && lastUpdateTime.before(event.getTime())){
                updateProfitChart(event);
            }

            //push the changes
            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    getUI().push();
                }
            });

            //save the updates event
            if(lastUpdateTime==null || lastUpdateTime.before(event.getTime())){
                lastUpdateTime=event.getTime();
            }

        }
        //if the game has stopped
        else if (arg instanceof PlaybackFinishedEvent) {

            //if this UI is not a destroyed one
            if(getUI()!=null){
                getUI().addWindow(new DataPlaybackGameOverWindow(this.userName, this.userType));
            }


            //push the changes
            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    getUI().push();
                }
            });


        }
    }


    private void updateQuantityChart(final StockUpdateEvent event)  {

        //iterate every series in the chart at the moment
        for (Series series : quantityChart.getConfiguration().getSeries()) {
            final DataSeries dSeries = (DataSeries) series;
            //if this series matches the stock events stock
            if (dSeries.getName().equalsIgnoreCase(event.getStockId())) {
                final float value;
                //if new data is available
                if(event.getData()!=null ){
                    value=event.getData().get(TradingDataAttribute.SHARES);
                }
                else {
                    value=0;

                }

                            if (dSeries.getData().size() > TICKER_CHART_LENGTH) {

                                dSeries.add(new DataSeriesItem(event.getTime(),value
                                        ), true, true);

                            } else {
                                dSeries.add(new DataSeriesItem(event.getTime(),
                                        value));

                            }

            }

        }

    }


    public Chart buildQuantityChart(){
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight(43,Unit.MM);

        Configuration conf = chart.getConfiguration();
        conf.getTitle().setText("Quantity");

        XAxis x = new XAxis();
        x.setType(AxisType.DATETIME);
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Quantity");
        conf.addyAxis(y);


        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.y+' sold'");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0);
        plot.setBorderWidth(0);
        //set the widht of the columns
        plot.setPointWidth(15);

        if (DataPlaybackEngineStates.playingSymbols != null) {
            for (String stock : DataPlaybackEngineStates.playingSymbols) {
                DataSeries ls = new DataSeries();
                ls.setName(stock);
                ls.setPlotOptions(plot);

                //add dummy points to fill it up
                for(int counter=1;counter<=TICKER_CHART_LENGTH;counter++){
                    ls.add(new DataSeriesItem
                            (DateUtils.decrementTimeBySeconds((TICKER_CHART_LENGTH-counter),
                                    DataPlaybackEngineStates.gameStartDate),0));
                }

                conf.addSeries(ls);


            }
        }

        conf.disableCredits();
        chart.drawChart(conf);
        chart.setImmediate(true);
        return chart;
    }

    public void updateProfitChart(final StockUpdateEvent event){
        Portfolio portfolio=null;


            try {
                //if this is a game based on the real time data player
                if(DataPlaybackEngineStates.gameConfig.getPlayerType()== PlayerTypes.REAL_TIME_DATA_PLAYER){
                    portfolio= player.getMyPortfolio(this.userName);
                }
                else if(DataPlaybackEngineStates.gameConfig.getPlayerType()== PlayerTypes.DAILY_SUMMARY_PLAYER){
                    portfolio= player.getMyPortfolio(this.userName);
                }

                //get the current prices of all the stocks
                BeanContainer<String,StockNamePriceBean> beans = (BeanContainer<String,StockNamePriceBean>)
                        stockPriceTable.getContainerDataSource();

                double profit=0;
                for(String stock:portfolio.getShares().keySet()){
                    //cost for a stock
                    double cost = portfolio.getShares().get(stock).get(Terms.PRICE);
                    //current price of a stock
                    float currentPrice=beans.getItem(stock).getBean().getPrice();
                    //total number of stocks bought
                    double numOfStocks= portfolio.getShares().get(stock).get(Terms.QNTY);

                    profit=profit+((currentPrice-cost)*numOfStocks);
                }

                //since there is only one series
                DataSeries ds=(DataSeries)profitChart.getConfiguration().getSeries().get(0);
                float floatProfit=(float)profit;
                ds.add(new DataSeriesItem(event.getTime(),floatProfit),true,true);


            }
            catch (UserJoinException e) {
                e.printStackTrace();
            }

    }

    @Override
    public Chart setupProfitChart() {
            Chart chart = new Chart();
            chart.setHeight(100,Unit.PERCENTAGE);
            chart.setWidth(95,Unit.PERCENTAGE);

            Tooltip tooltip = new Tooltip();
            tooltip.setShared(true);
            tooltip.setUseHTML(true);
            tooltip.setHeaderFormat("{point.key}");
            tooltip.setPointFormat("");
            tooltip.setFooterFormat("{series.name}: 	{point.y} EUR");

            Configuration configuration = new Configuration();
            configuration.setTooltip(tooltip);
            configuration.getChart().setType(ChartType.LINE);
            configuration.getLegend().setEnabled(false);
            configuration.getyAxis().setTitle("");

            PlotOptionsLine plotOptions = new PlotOptionsLine();
            plotOptions.setEnableMouseTracking(false);
            //performance related
            plotOptions.setShadow(false);

            configuration.setPlotOptions(plotOptions);

            configuration.getxAxis().setType(AxisType.DATETIME);
            configuration.getxAxis().setDateTimeLabelFormats(
                    new DateTimeLabelFormats("%e. %b", "%b"));


            DataSeries ls = new DataSeries();

            //add dummy points to fill it up
            for(int counter=1;counter<=PROFIT_CHART_LENGTH;counter++){
                ls.add(new DataSeriesItem
                        (DateUtils.decrementTimeBySeconds((PROFIT_CHART_LENGTH - counter),
                                DataPlaybackEngineStates.gameStartDate),0));
            }

            configuration.addSeries(ls);

            chart.setImmediate(true);
            chart.drawChart(configuration);
            //disable trademark
            chart.getConfiguration().disableCredits();

            chart.getConfiguration().getTitle().setText(null);
            return chart;

    }

    public Component setUpAccountInfoForm(){
        FormLayout form=new FormLayout();

        try {
            if(this.userName==null){

                int bal=player.getInitialCredit();
                Label accountBalance=new Label(Integer.toString(bal));
                this.accBalance=accountBalance;
                accountBalance.setCaption("Account Balance");
                form.addComponent(accountBalance);

                int max=player.getMaxOrderSize();
                Label maxOrderSize=new Label(Integer.toString(max));
                maxOrderSize.setCaption("Max. Order Size");
                form.addComponent(maxOrderSize);
            }
            else{
                Double bal=player.getMyPortfolio(this.userName).getCashBalance();
                Label accountBalance=new Label(bal.toString());
                this.accBalance=accountBalance;
                accountBalance.setCaption("Account Balance");
                form.addComponent(accountBalance);

                int max=player.getMaxOrderSize();
                Label maxOrderSize=new Label(Integer.toString(max));
                maxOrderSize.setCaption("Max. Order Size");
                form.addComponent(maxOrderSize);
            }
        } catch (UserJoinException e) {
            e.printStackTrace();
        }


        return form;
    }

    public void updateAccountBalance(){
        try {
            Double bal=this.player.getMyPortfolio(this.userName).getCashBalance();
            this.accBalance.setValue(String.format("%.2f",bal));
        } catch (UserJoinException e) {
            e.printStackTrace();
        }

    }
}
