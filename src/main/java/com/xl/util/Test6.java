package com.xl.util;

import java.io.File;

/**
 * Created by Shen on 2015/8/16.
 */
public class Test6 {

    public static void main(String[] args) {
       /* Date d = new Date(1451979399498l);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
        System.out.println(new Date().getTime());*/
        File main_dir = new File("/Users/bingbing/Downloads/头像&logo/人像");
        File[] file = main_dir.listFiles();
        for(int j=1;j<=50;j++){
            for (int i = 0; i < file.length; i++) {
                File child = file[i];
                if(child.getName().indexOf("、")>-1) {
                    String index = child.getName().substring(0, child.getName().indexOf("、"));
                    if (Integer.valueOf(index) == j) {
                        child.renameTo(new File(main_dir, "b" + (j) + ".jpg"));
                    }
                }
            }
        }
    }


}
