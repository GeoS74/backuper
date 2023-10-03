package com.signal.backuper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleApp {
    Path from;
    Path to;
    Long timeout;
    int countCopied = 0;
    
    public void run() {
        try { 
            if(this.from == null) {
                this.from = this.getPath("enter directory or file name:");
            }
            
            if(this.to == null) {
                this.to = this.getDir("enter target directory:");
            }
            
            this.timeout = this.getTimeout("input milliseconds for backup");
            if(this.timeout < 5000) {
                this.timeout = Long.valueOf(5000);
            }
//            
//            Copier copier = new Copier(this);
//            copier.setDaemon(true);
//            copier.start();
//            
            while(true) {
                String command = this.listen();
                
                if("quit".equals(command)) {
                    System.out.println("bye bye");
                    System.exit(0);
                }
                
                if("status".equals(command)) {
                    this.getStatus();
                }
            }
                        
//            if(Files.isDirectory(this.from)) {
//                System.out.println("это директория");
//                Files.walkFileTree(this.from, new MyFileVisitor(this.from, this.to));
//            }   
            
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            this.run();
        }
    }
    
    public void getStatus() {
        System.out.println("=========================");
        System.out.println("from: " + this.from );
        System.out.println("to: " + this.to );
        System.out.println("count copies: " + this.countCopied );
    }
    
    public void startCopied() throws InterruptedException, IOException {
        while(true) {
            this.countCopied++;
            this.copyFile();
            Thread.sleep(this.timeout);
        }
    }
    
    public void copyFile() throws IOException {
        if(Files.isRegularFile(this.from)) {
                String fname = this.getFileName(this.from.getFileName().toString());

                Files.copy(this.from, 
                        Paths.get(this.to.toString(), fname),
                        REPLACE_EXISTING
                );
//                System.out.println("file is copied");
        }
    }
    
    public Long getTimeout(String msg) {
        System.out.println(msg);
        String str = this.listen();
        return Long.valueOf(str);
    }
    
    public String getFileName(String fname) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String[] arr = fname.split("\\.");
        return arr[0] + "_backup_" + formater.format(new Date()) + "." + arr[1];
    }
    
    public Path getDir(String msg) throws Exception {
        Path dir = this.getNewPath(msg);

        if(Files.isDirectory(dir)) {
            return dir;
        }
        throw new Exception("this is not directory: "+dir);
    }
    
    public Path getNewPath(String msg) throws Exception {
        System.out.println(msg);
        String dir = this.listen();
        
        Path path = Files.createDirectories(Paths.get(dir));
        if(Files.exists(path)) {
            return path;
        }
        
        throw new Exception("path: " + path + " not found");
    }
    
    public Path getPath(String msg) throws Exception {
        System.out.println(msg);
        String dir = this.listen();
        Path path = Paths.get(dir);
        if(Files.exists(path)) {
            return path;
        }
        throw new Exception("path: " + path + " not found");
    }
    
    public String listen() {
        String str = "";
        try {
            Scanner scan = new Scanner(System.in);
            str = scan.nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return str;
    }
};

//class Copier extends Thread {
//    App app;
//    
//    Copier(App app) {
//        super();
//        this.app = app;
//    }
//    
//    @Override
//    public void run() {
//        try {
//            this.app.startCopied();
//        } catch (InterruptedException | IOException ex) {
//            Logger.getLogger(Copier.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}