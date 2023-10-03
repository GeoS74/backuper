package com.signal.backuper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String IGNORE_FILE_NAME = ".backupignore";
    private static final ArrayList<String> ignoreRules = new ArrayList<>();
    private static final Scanner scan = new Scanner(System.in);
    private static Path from;
    private static Path to;
    private static Long timeOut;
    
    public static void start(){
        
    }
    
    public static void setTimeout() {
        System.out.println("input timeout in ms:");
        int ms = Integer.parseInt(App.scan.nextLine());
        if(ms < 60*1000) {
            ms = 60*1000;
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

}