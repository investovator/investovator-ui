package org.investovator.ui.agentgaming.beans;

import java.util.Date;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class TimeSeriesNode {

    private Date date;
    private double value;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
