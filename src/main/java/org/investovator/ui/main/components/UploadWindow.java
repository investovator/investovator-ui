/*
 * investovator, Stock Market Gaming framework
 * Copyright (C) 2013  investovator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.investovator.ui.main.components;

import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.investovator.core.data.api.CompanyDataImpl;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.CompanyStockTransactionsDataImpl;
import org.investovator.core.data.exeptions.DataAccessException;
import org.vaadin.easyuploads.MultiFileUpload;

import java.io.File;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class UploadWindow extends Window {

    private UploadWindow(String caption) {
        super(caption);

        final VerticalLayout content = new VerticalLayout();

        MultiFileUpload uploder = new MultiFileUpload() {
            @Override
            protected void handleFile(File file, String s, String s2, long l) {

                CompanyStockTransactionsData historyData = new CompanyStockTransactionsDataImpl();
                try {
                    historyData.importCSV(CompanyStockTransactionsData.DataType.OHLC,"SAMP","MM/dd/yyyy",file);
                    new CompanyDataImpl().addCompanyData("SAMP", "Sampath Bank", 100000);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }

                synchronized (UI.getCurrent()){
                    content.addComponent(new Label(s));
                }

            }

            @Override
            protected boolean supportsFileDrops(){
                return false;
            }


        };

        uploder.setUploadButtonCaption("Choose Files");
        uploder.setImmediate(true);

        content.addComponent(uploder);

        this.setContent(content);
        this.setResizable(false);
        this.center();
    }
}
