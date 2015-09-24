package com.xl.controller.b;

import com.xl.bean.UserTable;
import com.xl.dao.UserDao;
import com.xl.util.ResultCode;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Shen on 2015/9/19.
 */
@Controller
@RequestMapping(value = "/user")
public class UserServlet {

    @Autowired
    HttpServletRequest request;
    @Resource
    UserDao userDao;

    @RequestMapping(value = "/uploadlogo")
    public
    @ResponseBody
    Object uploadLogo(@RequestParam("file") MultipartFile file, @RequestParam String deviceId) {
        JSONObject jo = new JSONObject();
        if (!file.isEmpty()) {
            UserTable ut = userDao.getUserByDeviceId(deviceId);
            ServletContext sc = request.getSession().getServletContext();
            String dir = "/mnt/logo/" + ut.getDeviceId(); // 设定文件保存的目录
            String filename = "logo" + ".jpg"; // 得到上传时的文件名
            try {
                File logo = null;
                FileUtils.writeByteArrayToFile(logo = new File(dir, filename),
                        file.getBytes());
                Thumbnails.of(logo)
                        .size(160, 160)
                        .outputFormat("jpg")
                        .toFiles(new Rename() {
                            @Override
                            public String apply(String name, ThumbnailParameter param) {
                                return name + "_" + (int) param.getSize().getWidth() + "x" + (int) param.getSize().getHeight() + ".jpg";
                            }
                        });
                jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
                return jo;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                jo.put(ResultCode.STATUS, ResultCode.FAIL);
            }
        } else {
            jo.put(ResultCode.STATUS, ResultCode.FAIL);
        }
        return jo;
    }

    @RequestMapping(value = "/{path}/{deviceId}/{fileName}")
    public void download(HttpServletRequest request,
                         HttpServletResponse response,
                         @PathVariable("path") String path,
                         @PathVariable("fileName") String fileName,
                         @PathVariable("deviceId") String deviceId)
            throws FileNotFoundException, IOException {
        ServletContext sc = request.getSession().getServletContext();
        String dir = "/mnt/" + path + "/" + deviceId;
        File downloadFile = new File(dir, fileName);
        response.setContentLength(new Long(downloadFile.length()).intValue());
        response.setHeader("Content-Disposition", "attachment; filename="
                + fileName);
        FileCopyUtils.copy(new FileInputStream(downloadFile),
                response.getOutputStream());
    }
}
