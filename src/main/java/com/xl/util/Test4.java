package com.xl.util;

import java.io.File;

/**
 * Created by Shen on 2015/8/16.
 */
public class Test4 {

    public static void main(String[] args) {
       /* Date d = new Date(1451979399498l);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
        System.out.println(new Date().getTime());*/
        File main_dir = new File("/Users/bingbing/Documents/aa");
        File[] file = main_dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                File[] child_files = file[i].listFiles();
                for (int j = 0; j < child_files.length; j++) {
                    if (child_files[j].isFile()) {
                        File child = child_files[j];
                        if (child.getName().length() < "1439655813814".length()) {
                            child.renameTo(new File(main_dir.getPath() + "\\" + child.getName() + ".amr"));
                        } else if (child.getName().indexOf(".mp4") > 0) {
                            child.renameTo(new File(main_dir.getPath() + "\\" + child.getName()));
                        } else {
                            child.renameTo(new File(main_dir.getPath() + "\\" + child.getName() + ".jpg"));
                        }
                    }
                }
            }
        }
    }


}
