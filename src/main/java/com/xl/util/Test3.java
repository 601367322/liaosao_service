package com.xl.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Shen on 2015/8/16.
 */
public class Test3 {

    public static void main(String[] args) {
       /* Date d = new Date(1451979399498l);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
        System.out.println(new Date().getTime());*/
        File main_dir = new File("C:\\Users\\Shen\\Desktop\\logo");
        File[] file = main_dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                File[] child_files = file[i].listFiles();
                for (int j = 0; j < child_files.length; j++) {
                    if (child_files[j].isFile()) {
                        File child = child_files[j];
                        try {
                            if (child.getName().indexOf("_160") == -1) {
                                FileUtils.copyFile(child, new File(main_dir.getPath() + "\\" + child.getName()));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
