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


package org.investovator.ui.utils.dashboard;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public enum  IconLoader {
    ANALYSIS,
    GAME_SUMMARY,
    MAIN_VIEW,
    DATA_IMPORT,
    STOCKS,
    OVERVIEW,
    WATCH_LIST,
    REPORTS;




    /**
     * Returns the icon name for the panel name
     * @param name Name of the panel
     * @return
     */
    public static String nameToIcon(IconLoader name){
        switch (name){
            case ANALYSIS:
                return "icon-sales";
            case GAME_SUMMARY:
                return "icon-dashboard";
            case MAIN_VIEW:
                return "icon-monitor";
            case DATA_IMPORT:
                return "icon-upload";
            case STOCKS:
                return "icon-th-list";
            case OVERVIEW:
                return "icon-vcard";
            case WATCH_LIST:
                return "icon-eye";
            case REPORTS:
                return "icon-doc";
            default:
                return "icon-reports";
        }

    }

    public static String getName(IconLoader name){
        switch (name){
            case ANALYSIS:
                return "Analysis";
            case GAME_SUMMARY:
                return "Game Summary";
            case MAIN_VIEW:
                return "Main View";
            case DATA_IMPORT:
                return "Data Import";
            case STOCKS:
                return "Stocks";
            case OVERVIEW:
                return "Overview";
            case WATCH_LIST:
                return "Watch List";
            case REPORTS:
                return "Market Reports";
            default:
                return "Undefined";
        }
    }
}
