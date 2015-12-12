package com.xl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shen on 2015/8/16.
 */
public class Test {

    public static void main(String[] args) {
        Date d = new Date(1451979399498l);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
        System.out.println(new Date().getTime());
        /*File main_dir = new File("C:\\Users\\Shen\\Desktop\\11");
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
        }*/
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
