package com.xl.controller.c;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MyTask implements ServletContextAware {

    private ServletContext sc;

    @Scheduled(cron = "0 0 5 * * ?")
//	@Scheduled(cron = "0/5 * * * * ?")
    public void taskCycle() {
        String dir = "/mnt/";
//        ReadAllFile(dir);
    }

    @Override
    public void setServletContext(ServletContext arg0) {
        this.sc = arg0;
    }

    public void ReadAllFile(String filePath) {
        File f = null;
        f = new File(filePath);
        File[] files = f.listFiles();
        List<File> list = new ArrayList<File>();
        for (File file : files) {
            if (file.isDirectory()) {
                ReadAllFile(file.getAbsolutePath());
            } else {
                list.add(file);
            }
        }
        for (File file : files) {
            if (file.lastModified() < new Date().getTime() - 3 * 24 * 60 * 60
                    * 1000) {
                file.delete();
            }
        }
    }

	/*
     "0 0 12 * * ?"    每天中午十二点触发
"0 15 10 ? * *"    每天早上10：15触发
"0 15 10 * * ?"    每天早上10：15触发
"0 15 10 * * ? *"    每天早上10：15触发
"0 15 10 * * ? 2005"    2005年的每天早上10：15触发
"0 * 14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发
"0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发
"0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
"0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发
"0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发
"0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发
	 */
}