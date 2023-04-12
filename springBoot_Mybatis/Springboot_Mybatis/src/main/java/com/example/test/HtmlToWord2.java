package com.example.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class HtmlToWord2 {


    public static void main(String[] args) throws Throwable {

        writeWordFile();


    }


    public static boolean writeWordFile() {

        boolean w = false;
        String path = "F:/";

        try {
            if (!"".equals(path)) {

                // 检查目录是否存在
                File fileDir = new File(path);
                if (fileDir.exists()) {

                    // 生成临时文件名称
                    String fileName = "a.doc";
                    // .html文件 的内容
                    String content = "";

                    byte b[] = content.getBytes();
                    ByteArrayInputStream bais = new ByteArrayInputStream(b);
                    POIFSFileSystem poifs = new POIFSFileSystem();
                    DirectoryEntry directory = poifs.getRoot();
                    DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
                    FileOutputStream ostream = new FileOutputStream(path + fileName);
                    poifs.writeFilesystem(ostream);
                    bais.close();
                    ostream.close();

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return w;
    }


}

