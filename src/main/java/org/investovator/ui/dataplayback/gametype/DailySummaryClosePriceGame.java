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


package org.investovator.ui.dataplayback.gametype;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryClosePriceGame implements GameType {
    @Override
    public TradingDataAttribute[] getInterestedAttributes() {
        TradingDataAttribute[] attrs=new TradingDataAttribute[2];
        attrs[0]=TradingDataAttribute.DAY;
        attrs[1]=TradingDataAttribute.CLOSING_PRICE;

        return attrs;
    }

    @Override
    public PlayerTypes getPlayerType() {
        return PlayerTypes.DAILY_SUMMARY_PLAYER;
    }

    @Override
    public TradingDataAttribute getAttributeToMatch() {
        return TradingDataAttribute.CLOSING_PRICE;
    }

    @Override
    public String getDescription() {
        return "Daily Summary game based on closing prices of stocks";
    }
}
