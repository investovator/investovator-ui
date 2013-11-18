package org.investovator.ui.main.components;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;
import org.investovator.ui.agentgaming.beans.StockItemBean;
import org.investovator.ui.main.beans.CompanyDataBean;

import java.util.Date;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class CompanyTable extends Table {

    private BeanContainer<String, CompanyDataBean> companyList;

    public CompanyTable() {

        companyList = new BeanContainer<String, CompanyDataBean>(CompanyDataBean.class);
        companyList.setBeanIdProperty("stockID");

        setContainerDataSource(companyList);

        setWidth("90%");
        setSelectable(true);
        setImmediate(true);

        setColumns();

    }


    public void addCompany(String stockID, String companyName, Date dataStart, Date dataEnd){

        CompanyDataBean tmp = new CompanyDataBean();
        tmp.setStockID(stockID);
        if(companyName != null) tmp.setCompanyName(companyName);
        if(dataStart != null) tmp.setDataStart(dataStart);
        if(dataEnd != null) tmp.setDataEnd(dataEnd);

        companyList.addBean(tmp);
    }


    private void setColumns(){
        setVisibleColumns(new String[]{"stockID","companyName","dataStart","dataEnd"});

        setColumnHeader("stockID", "Symbol");
        setColumnHeader("companyName", "Company Name");
        setColumnHeader("dataStart", "Start Date");
        setColumnHeader("dataEnd", "End Date");
    }
}
