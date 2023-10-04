package com.signal.backuper;

/**
 *
 * @author geos
 */
public class Backuper {

    public static void main(String[] args) {
        try {
            App.setFrom();
            App.setTo();
            App.setTimeStart();
            App.start();
        } catch (Exception ex) {
              System.out.println(ex.getMessage());
              System.exit(0);
//            Logger.getLogger(Backuper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
