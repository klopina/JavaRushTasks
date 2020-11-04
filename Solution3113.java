package com.javarush.task.task31.task3113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* 
Что внутри папки?
*/

public class Solution extends SimpleFileVisitor<Path> {
    private AtomicInteger filesCount = new AtomicInteger(0);
    private AtomicInteger dirsCount = new AtomicInteger(-1);
    private AtomicLong totalSize = new AtomicLong(0);

    public int getFilesCount() {
        return filesCount.intValue();
    }

    public int getDirsCount() {
        return dirsCount.intValue();
    }

    public long getTotalSize() {
        return totalSize.longValue();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        totalSize.addAndGet(Files.size(file));
        filesCount.incrementAndGet();
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        dirsCount.incrementAndGet();
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Path path = Paths.get(reader.readLine());
        reader.close();
        if (Files.isDirectory(path)) {
            Solution visitor = new Solution();
            Files.walkFileTree(path, visitor);
            System.out.println("Всего папок - " + visitor.getDirsCount());
            System.out.println("Всего файлов - " + visitor.getFilesCount());
            System.out.println("Общий размер - " + visitor.getTotalSize());
        } else {
            System.out.println(path.toAbsolutePath() + " - не папка");
        }
    }
}
