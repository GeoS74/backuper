package com.signal.backuper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author geos
 */
public class App {
    private static final String IGNORE_FILE_NAME = ".backupignore";
    private static final ArrayList<Pattern> ignorePatterns = new ArrayList<>();
    private static final Scanner scan = new Scanner(System.in);
    private static final String TIME_PATTERN = "\\d{1,2}:\\d{1,2}";
    private static final SimpleDateFormat timeStartTemplate = new SimpleDateFormat("HH:mm:ss");
    private static final ArrayList<String> timeStart = new ArrayList<>();
    private static final SimpleFileVisitor<Path> fileVisitor = App.getFileVisitor();
    private static final ArrayList<Path> filesForArchive = new ArrayList<>();
    private static Path from;
    private static Path to;
    private static Date lastBackup;
    
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
    
    public static void setTimeStart() throws Exception {
        System.out.println("input time for start backup files (example: \"05:15; 23:10\"):");
        String time = App.scan.nextLine();
        Pattern pattern = Pattern.compile(App.TIME_PATTERN);
        Matcher match = pattern.matcher(time);

        GregorianCalendar calendar = new GregorianCalendar();
        
        while(match.find()) {
            String[] arr = time.substring(match.start(), match.end())
                    .split(":");
            
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1]));
            calendar.set(Calendar.SECOND, 0);
            
            App.timeStart.add(App.timeStartTemplate.format(calendar.getTime()) );
        }
        
        if(App.timeStart.isEmpty()) {
            throw new Exception("incorrect set time for start");
        }
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
            System.out.println("directory does not exist, create directory? [Y/n]");
            
            switch(App.scan.nextLine()) {
                case "Y":
                case "y":
                case "Д":
                case "д":
                    App.createDirectory(path);
                    break;
                default: throw new Exception("failed to create directory");
            }
        }
        App.to = path;
    }
    
    private static void createDirectory(Path dir) throws Exception {
        try {
            Files.createDirectories(dir);
        } catch(IOException ex) {
            throw new Exception("failed to create directory!");
        }
    }
    
    private static void addFilesToArchive() {
        String archive = Paths.get(App.to.toString(), App.makeArchiveName()).toString();
        
        try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archive))){
            for(Path file: App.filesForArchive) {
                String relativePath = App.from.relativize(file).toString();
                
                try(FileInputStream fis= new FileInputStream(file.toString());) {
                
                    ZipEntry entry = new ZipEntry(relativePath);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage());
                  } 
            }
            zout.closeEntry(); 
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
        } 
    }
    
    private static boolean matchingIgnoreRules(Path file) {
        String relativePath = App.from.relativize(file).toString();
        
        for(Pattern rule: App.ignorePatterns) {
            if(rule.matcher(relativePath).find()){
                return true;
            }
        }
        return false;
    }
    
    private static Pattern makeIgnorePattern(String rule) {
        rule = rule.replaceFirst("^/", "^");
        rule = rule.replaceAll("\\*", "[^/]*");
        rule = rule.replaceAll("\\.", "\\\\.");
        rule = rule.replaceAll("\\+", "\\\\+");
        return Pattern.compile(rule);
    }
    
    private static void makeArchive() {
        try {
            App.filesForArchive.clear();
            Files.walkFileTree(App.from, App.fileVisitor);
            App.addFilesToArchive();
            App.lastBackup = new Date();
        } catch (IOException ex) {}
    }
    
    private static void runDeamon() {
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String now = App.timeStartTemplate.format(new Date().getTime());
                
                    if(App.timeStart.indexOf(now) != -1) {
                        App.makeArchive();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {}
                }
            }
        });
        tr.setDaemon(true);
        tr.start();
    }
    
    private static SimpleFileVisitor getFileVisitor() {
        return new SimpleFileVisitor<Path>() {
    
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
               
               if(!App.matchingIgnoreRules(file)) {
                   App.filesForArchive.add(file);
               }
               return FileVisitResult.CONTINUE;
           }
        };
    }
        
    private static void readIgnoreFile() {
        Path path = Paths.get(App.from.toString(), App.IGNORE_FILE_NAME);
        
        try {
            List<String> igRules = Files.readAllLines(path);
            
            // default rules
            App.ignorePatterns.add(App.makeIgnorePattern(App.IGNORE_FILE_NAME));
            
            // other rules
            igRules.forEach((String rule) -> {
                if(!rule.isEmpty()) { // ignore empty string
                    App.ignorePatterns.add(App.makeIgnorePattern(rule));
                }
            });
        } catch (IOException ex) {
            System.out.println("ATTENTION: file .backupignor not exist");
        }
    }

    private static String makeArchiveName() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return "backup_"+ f.format(new Date().getTime())+".zip";
    }
        
    private static void exitProgram() {
        System.out.println("bye bye...");
        System.exit(0);
    }
    
    private static void messageStart() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("==backup started at " + f.format(new Date()) + "==");
    }
    
    private static void showState() {
        System.out.println("state:");
        System.out.println("\t\"from\":\t\t" + App.from);
        System.out.println("\t\"to\":\t\t" + App.to);
        System.out.println("\t\"time to backup\":" + App.timeStart);
        
        SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String last = "There is no backup right now";
        if(App.lastBackup != null) {
            last = t.format(App.lastBackup);
        }
        System.out.println("\t\"last backup\":\t" + last);
    }
    
    private static void showHelp() {
        System.out.println("use command:");
        System.out.println("\t\"quit\" or \"q\"\t- stop program");
        System.out.println("\t\"state\"\t\t- show state backuper");
        System.out.println("\t\"help\"\t\t- show help");
    }
    
}