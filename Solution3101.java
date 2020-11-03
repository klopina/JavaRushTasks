package com.javarush.task.task31.task3101;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Map;
import java.util.TreeMap;

/* 
Проход по дереву файлов
*/

public class Solution {

    public static class SolutionFileVisitor extends SimpleFileVisitor<Path> {
        TreeMap<Path, String> fileList = new TreeMap<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            /*
            В результате обхода отбираются все txt фалы размером не более 50 байт и их содержимое
            записывается в TreeMap (отсортировано по имени файла)
            В конце добавляется перевод строки
             */
            if(file.toString().endsWith(".txt") && Files.size(file) <= 50) {
                String fileContent = new String(Files.readAllBytes(file)) + "\n";
                fileList.put(file.getFileName(), fileContent);
            }
            return FileVisitResult.CONTINUE;
        }
    }
    public static void main(String[] args) throws IOException {
        Path path = Paths.get(args[0]).toAbsolutePath();
        Path resultFileAbsolutePath = Paths.get(args[1]);
//        Path path = Paths.get("dirForTasks").toAbsolutePath(); //TODO REMOVE for testing
//        Path resultFileAbsolutePath = Paths.get("data.txt").toAbsolutePath(); //TODO REMOVE for testing

        // обход директории и формирование списка искомых файлов visitor.fileList
        EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        SolutionFileVisitor visitor = new SolutionFileVisitor();
        Files.walkFileTree(path,opts,Integer.MAX_VALUE, visitor);

        // из полученного пути формируем путь для переименования
        File resultFileBeforeRename = new File(resultFileAbsolutePath.toString());
        resultFileAbsolutePath = Paths.get(resultFileAbsolutePath.getParent().toString() + "/allFilesContent.txt");
        File resultFile = new File(resultFileAbsolutePath.toString());

        // переименовываем файл
        FileUtils.renameFile(resultFileBeforeRename, resultFile);

        // выводим модержимое в файл
        FileOutputStream fos = new FileOutputStream(resultFile);
        for (Map.Entry<Path, String> file : visitor.fileList.entrySet()) {
            fos.write(file.getValue().getBytes());
        }
        fos.close();
    }
}
