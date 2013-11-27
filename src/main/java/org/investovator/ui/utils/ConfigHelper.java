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

package org.investovator.ui.utils;

import com.vaadin.server.VaadinService;

import java.io.File;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class ConfigHelper {

    static String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();


    public static String getDatabaseConfigProperties(){
        return basepath + "/WEB-INF/configuration/database.properties";
    }

    public static String getBasepath(){
        return basepath;
    }

    public static String getImagePath(){
        return basepath +"/WEB-INF/images/";
    }

    public static String getUploadPath(){

        String path = basepath + "/uploads/";

        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        return path;
    }

}
