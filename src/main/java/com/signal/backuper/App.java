package com.signal.backuper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private static final String IGNORE_FILE_NAME = ".backupignore";
    private static final ArrayList<String> ignoreRules = new ArrayList<>();
    private static final Scanner scan = new Scanner(System.in);
    private static final String TIME_PATTERN = "\\d{1,2}:\\d{1,2}";
    private static final String TIME_START_FORMAT = "HH:mm";
    private static final ArrayList<String> timeStart = new ArrayList<>();
    private static Path from;
    private static Path to;
    private static Long timeOut;
    
    public static void start(){
        App.messageStart();
        App.runDeamon();
        
        while(true) {
            switch(App.scan.nextLine()) {
                case "q":
                case "quit":
                    App.exitProgram();
                    break;
                case "status":
                case "state":
                    App.showState();
                    break;
                case "help":
                    App.showHelp();
                    break;
            }
        }
    }

    private static void runDeamon() {
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        tr.setDaemon(true);
        tr.start();
    }
    
    public static void setTimeStart() throws Exception {
        System.out.println("input time for start backup files (example: \"05:15; 23:10\"):");
        String time = App.scan.nextLine();
        Pattern pattern = Pattern.compile(App.TIME_PATTERN);
        Matcher match = pattern.matcher(time);

        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat(App.TIME_START_FORMAT);
        
        while(match.find()) {
            String[] arr = time.substring(match.start(), match.end())
                    .split(":");
            
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1]));
            
            App.timeStart.add(formatter.format(calendar.getTime()) );
        }
        
        if(App.timeStart.isEmpty()) {
            throw new Exception("incorrect set time for start");
        }
    }
    
    public static void setTimeout() {
        System.out.println("input timeout in ms:");
        int ms = 0;
        
        try {
            ms = Integer.parseInt(App.scan.nextLine());
        } 
        catch (NumberFormatException ex) {} 
        
        if(ms < 60*60*1000) {
            ms = 60*60*1000;
        }
        
        App.timeOut = (long) ms;
    }

    public static void setFrom() throws Exception {
        System.out.println("input directory for backup:");
        Path path = Paths.get(App.scan.nextLine());
        if(!Files.isDirectory(path)) {
            throw new Exception(path +" is not directory");
        }
        App.from = path;
        App.readIgnoreFile();
    }
    
    public static void setTo() throws Exception {
        System.out.println("input target directory:");
        Path path = Paths.get(App.scan.nextLine());
        if(!Files.isDirectory(path)) {
            throw new Exception(path + " is not directory");
        }
        App.to = path;
    }
    
    private static void readIgnoreFile() {
        Path path = Paths.get(App.from.toString(), App.IGNORE_FILE_NAME);
        
        try {
            List<String> ignoreRules = Files.readAllLines(path);

            for(String rule: ignoreRules) {
                App.ignoreRules.add(rule);
            }
        } catch (IOException ex) {
            System.out.println("ATTENTION: file .backupignor not exist");
        }
    }

    private static void exitProgram() {
        System.out.println("bye bye...");
        System.exit(0);
    }
    
    private static void messageStart() {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        System.out.println("==backup started at " + formatter.format(new Date()) + "==");
    }
    
    private static void showState() {
        System.out.println("state:");
        System.out.println("\t\"from\":\t\t" + App.from);
        System.out.println("\t\"to\":\t\t" + App.to);
        System.out.println("\t\"time start\":\t" + App.timeStart);
    }
    
    private static void showHelp() {
        System.out.println("use command:");
        System.out.println("\t\"quit\" or \"q\"\t- stop program");
        System.out.println("\t\"state\"\t\t- show state backuper");
        System.out.println("\t\"help\"\t\t- show help");
    }
    
}