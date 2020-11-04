package com.javarush.task.task31.task3106;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* 
Разархивируем файл
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        String result = args[0];
        String[] parts = Arrays.copyOfRange(args, 1, args.length);
        TreeMap<Integer, String> fileParts = new TreeMap<>();
        for (String part : parts) {
            String[] elementsOfPart = part.split("\\.");
            int indexOfPart = Integer.parseInt(elementsOfPart[elementsOfPart.length - 1]);
            fileParts.put(indexOfPart, part);
        }
        ByteArrayOutputStream zipData = new ByteArrayOutputStream();
        for (Map.Entry<Integer, String> p : fileParts.entrySet()) {
            FileInputStream fis = new FileInputStream(p.getValue());
            while (fis.available() > 0) {
                byte[] buff = new byte[1024];
                int count = fis.read(buff);
                zipData.write(buff, 0, count);
            }
            fis.close();
        }

        ZipInputStream zip = new ZipInputStream((new ByteArrayInputStream(zipData.toByteArray())));
        FileOutputStream fos = new FileOutputStream(result);
        ZipEntry file = zip.getNextEntry();
        byte[] buff = new byte[2048];
        int size = 0;
        while ((size = zip.read(buff, 0, buff.length)) != -1) {
            fos.write(buff, 0, size);
        }
        fos.flush();
        fos.close();
        zip.closeEntry();
        zip.close();
    }
}
