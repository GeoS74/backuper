package com.signal.backuper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author geos
 */
public class Backuper {

    public static void main(String[] args) {
        try {
//            App.setFrom();
//            App.setTo();
////            App.setTimeout();
            App.setTimeStart();
            App.start();
              
              String str = "08:10";
              String str2 = "08:10;7:15;22:23; 123:533 45&77";
              String str3 = "5:3";
              

              int h = Integer.parseInt("08");
              
              GregorianCalendar cal = new GregorianCalendar();
              
              SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
              
              cal.set(Calendar.HOUR_OF_DAY, h);
              cal.set(Calendar.MINUTE, 3);
              

//              System.out.println(formatter.format(cal.getTime()));
//              
//              GregorianCalendar cal = new GregorianCalendar(2002, 0, 15, 11, 35);
//              GregorianCalendar cal2 = new GregorianCalendar(2002, 0, 16);
//
//              System.out.println(cal.getTime());
//              
//              SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//              SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
//               
//              System.out.println(formatter.format(cal.getTime()));
//              
//              System.out.println(formatter.format(new Date()));
//              
//              System.out.println(formatter.format(new Date()).equals(formatter.format(new Date())));

//                Pattern pattern = Pattern.compile("\\d{1,2}:\\d{1,2}");
//                Matcher match = pattern.matcher(str2);
//                while(match.find()) {
//                    System.out.println(str2.substring(match.start(), match.end()));
//                }
       
     
               
            
        } catch (Exception ex) {
              System.out.println(ex.getMessage());
              System.exit(0);
//            Logger.getLogger(Backuper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
