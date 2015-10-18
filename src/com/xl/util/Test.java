package com.xl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Shen on 2015/8/16.
 */
public class Test {

    public static void main(String[] args) {
        File main_dir = new File("C:\\Users\\Shen\\Desktop\\11");
        File[] file = main_dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                File[] child_files = file[i].listFiles();
                for (int j = 0; j < child_files.length; j++) {
                    if (child_files[j].isFile()) {
                        File child = child_files[j];
                        if (child.getName().length() < "1439655813814".length()) {
                            if (getFileSizes(child) > 140 * 1024) {
                                child.renameTo(new File(main_dir.getPath() + "\\" + child.getName() + ".mp4"));
                            } else {
                                child.renameTo(new File(main_dir.getPath() + "\\" + child.getName() + ".amr"));
                            }
                        } else {
                            child.renameTo(new File(main_dir.getPath() + "\\" + child.getName() + ".jpg"));
                        }
                    }
                }
            }
        }
    }

    public static long getFileSizes(File f) {//取得文件大小
        long s = 0;
        if (f.exists()) {
            try {
                FileInputStream fis = null;
                fis = new FileInputStream(f);
                s = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }
}
