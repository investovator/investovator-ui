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
public interface GameConfiguration {

    /**
     * returns the attributes that the data player should include in
     * data playback events
     * @return
     */
    public TradingDataAttribute[] getInterestedAttributes();

    /**
     * returns the data player that should be played when running the
     * game
     *
     * @return
     */
    public PlayerTypes getPlayerType();

    /**
     * returns the attribute on which the game should be run.
     * Ex - Closing price for a Daily Summary based game
     * @return
     */
    public TradingDataAttribute getAttributeToMatch();

    /**
     * returns a description about the game
     * @return
     */
    public String getDescription();
}
