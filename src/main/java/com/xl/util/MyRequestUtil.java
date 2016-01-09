package com.xl.util;

import com.xl.bean.UserTable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Shen on 2015/10/1.
 */
public class MyRequestUtil {

    //用户信息
    public static final String SESSION_TAG_USER = "session_user";

    /**
     * 获取网站域名
     *
     * @param request
     * @return
     */
    public static String getHost(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        return tempContextUrl;
    }

    /**
     * 获取session
     */
    public static UserTable getUserTable(HttpServletRequest request) throws Exception {
        return getUserTable(request.getSession());
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    public static UserTable getUserTable(HttpSession session) throws Exception {
        UserTable user = (UserTable) session.getAttribute(SESSION_TAG_USER);
        if (user == null) {
            throw new Exception("请重新登陆");
        }
        return user;
    }

    /**
     * 将用户信息放入session
     *
     * @param session
     * @param ut
     */
    public static void setUserTable(HttpSession session, UserTable ut) {
        session.setAttribute(SESSION_TAG_USER, ut);
    }
}
