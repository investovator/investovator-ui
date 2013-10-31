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


package org.investovator.ui.dataplayback.user.dashboard.dailysummary;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.ui.utils.dashboard.dataplayback.BasicStockDataView;

import java.util.ArrayList;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryStockDataView extends BasicStockDataView {
    @Override
    public TradingDataAttribute[] setSelectableAttributes(){
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        attributes.add(TradingDataAttribute.DATE_HIGH);
        attributes.add(TradingDataAttribute.DATE_LOW);
        attributes.add(TradingDataAttribute.CLOSING_PRICE);



        return attributes.toArray(new TradingDataAttribute[attributes.size()]);
    }
}
