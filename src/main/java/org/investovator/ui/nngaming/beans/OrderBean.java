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

package org.investovator.ui.nngaming.beans;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author: Hasala Surasinghe
 * @version: ${Revision}
 */
public class OrderBean implements Serializable,Comparator<OrderBean>{

    private float orderValue;
    private int quantity;

    public OrderBean(float orderValue,int quantity){

        this.orderValue = orderValue;
        this.quantity = quantity;

    }

    public float getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(float orderValue) {
        this.orderValue = orderValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compare(OrderBean o1, OrderBean o2) {
        if(o1.getOrderValue() < o2.getOrderValue())
            return -1;
        else if(o1.getOrderValue() > o2.getOrderValue())
            return 1;
        else
            return 0;
    }
}
