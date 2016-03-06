package com.henry.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class XmlCreater {
    
    /**
     * 追加文件内容A
     * @param fileName
     * @param content
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * 追加文件内容B
     * @param fileName
     * @param content
     */
    public static void appendMethodB(String fileName, String content){
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建文件
     * @param dirName 文件夹路径
     * @param fileName 文件名称
     */
    public static void CreatFile(String dirName,String fileName){
        File dir = isNotFindFileCreate(dirName);
        try {
            File fileXml = new File(dir,fileName);
            fileXml.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static File isNotFindFileCreate(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
