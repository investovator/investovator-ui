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


package org.investovator.ui.dataplayback.util;

import org.investovator.dataplaybackengine.configuration.GameTypes;

import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlaybackEngineStates {

    //game configuration
    public static GameTypes gameConfig;

    //stores the stock symbols up on which the game is played
    public static String[] playingSymbols;

    //stores the game start date
    public static Date gameStartDate;

    public static boolean isMultiplayer;

    //todo - a quick hack
//    public static String gameInstance;
}


