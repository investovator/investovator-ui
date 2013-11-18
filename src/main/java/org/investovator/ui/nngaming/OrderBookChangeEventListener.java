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

package org.investovator.ui.nngaming;

import org.investovator.ann.nngaming.events.AddBidEvent;
import org.investovator.ann.nngaming.events.DayChangedEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class OrderBookChangeEventListener implements Observer{

    private static OrderBookChangeEventListener instance;


    private OrderBookChangeEventListener(){


    }

    public static OrderBookChangeEventListener getInstance() {
        if(instance == null){
            synchronized(OrderBookChangeEventListener.class){
                if(instance == null)
                    instance = new OrderBookChangeEventListener();
            }
        }
        return instance;
    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof DayChangedEvent){
           System.out.println("DayChangedObserved");


        }
        if(arg instanceof AddBidEvent){
            System.out.println("AddBidObserved");

        }

    }


}
