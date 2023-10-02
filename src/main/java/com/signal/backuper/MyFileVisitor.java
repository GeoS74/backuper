package com.signal.backuper;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.BasicFileAttributes;

public class MyFileVisitor extends SimpleFileVisitor<Path> {
    Path from;
    Path to;
    
    MyFileVisitor(Path from, Path to){
        this.from = from;
        this.to = to;
    }
    
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

       System.out.println(file.toAbsolutePath());
       System.out.println(file.getParent());
       System.out.println(this.from.getNameCount());
       System.out.println(file.getParent().getNameCount());
       
       
       
//        Files.copy(file, Paths.get(this.to.toString(), file.getFileName().toString()),
//                        REPLACE_EXISTING);
//        System.out.println("file is copied");

       return FileVisitResult.CONTINUE;
   }
}
