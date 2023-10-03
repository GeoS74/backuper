package com.signal.backuper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private static Scanner scan = new Scanner(System.in);
    private static ArrayList<Path> from = new ArrayList<>();
    
    public static void addPaths(){
        System.out.print("input files and directories for backup");
        System.out.println(" (empty string for the end):");
        while(true) {
            String path = App.scan.nextLine();
            if("".equals(path)) {
                break;
            }
            App.from.add(Paths.get(path));
        }
        App.showPathsFrom();
    }
    
    private static void showPathsFrom() {
        System.out.println("added files and directories:");
        for(Path p: App.from) {
            System.out.println("\t"+p);
        }
    }

}