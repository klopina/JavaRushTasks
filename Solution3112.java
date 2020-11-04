package com.javarush.task.task31.task3112;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/* 
Загрузчик файлов
*/

public class Solution {

    public static void main(String[] args) throws IOException {
        Path passwords = downloadFile("https://yastatic.net/morda-logo/i/citylogos/yandex19-logo-ru.png", Paths.get("downloads"));

//        for (String line : Files.readAllLines(passwords)) {
//            System.out.println(line);
//        }
    }

    public static Path downloadFile(String urlString, Path downloadDirectory) throws IOException {
        // implement this method
        URL url = new URL(urlString);
        Path filename = Paths.get(url.getFile()).getFileName();
        if (!Files.exists(downloadDirectory))
            Files.createDirectory(downloadDirectory);
        Path tempFile = Files.createTempFile("file", "");
        InputStream url_stream = null;
        try {
            url_stream = url.openStream();
        } catch (IOException e) {
            System.out.println("Error: " + e);
            return null;
        }
        Path fullFilePath = downloadDirectory.resolve(filename);
        try {
            Files.copy(url_stream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            url_stream.close();
            return null;
        }
        Files.move(tempFile, fullFilePath, StandardCopyOption.REPLACE_EXISTING);
        return fullFilePath;
    }
}
