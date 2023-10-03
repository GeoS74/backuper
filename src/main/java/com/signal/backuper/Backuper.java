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
            App.setTimeout();
            App.start();
            
//        String str = "/home/geos/test/from";
//        Path p = Paths.get(str);
//        System.out.println(Files.isDirectory(p));
//        System.out.println(Files.isRegularFile(p));
        } catch (Exception ex) {
              System.out.println(ex.getMessage());
              System.exit(0);
//            Logger.getLogger(Backuper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
