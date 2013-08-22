package org.investovator.utils;

import org.investovator.Main;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class MainClassRunner implements Runnable {
    @Override
    public void run() {
        //test JASA code
        Main main=new Main();
        String[] v=new String[1];
        v[0]="d";
        main.main(v);
    }
}
