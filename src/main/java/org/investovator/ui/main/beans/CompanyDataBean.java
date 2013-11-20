package org.investovator.ui.main.beans;

import java.util.Date;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class CompanyDataBean {

    private String stockID;
    private String companyName;
    Date dataStart;
    Date dataEnd;

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getDataStart() {
        return dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(Date dataEnd) {
        this.dataEnd = dataEnd;
    }
}
