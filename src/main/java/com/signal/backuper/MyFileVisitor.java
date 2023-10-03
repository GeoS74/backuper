package com.signal.backuper;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


public class MyFileVisitor extends SimpleFileVisitor<Path> {
    
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

       System.out.println(file);
//       System.out.println(App.from.relativize(file));

       return FileVisitResult.CONTINUE;
   }
}
