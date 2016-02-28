package com.xl.filter;

import com.xl.bean.UserBean;
import com.xl.bean.UserTable;
import com.xl.bean.Vip;
import com.xl.dao.UserDao;
import com.xl.dao.VipDao;
import com.xl.util.MyRequestUtil;
import com.xl.util.MyUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter extends OncePerRequestFilter {

    public UserDao userDao;
    public VipDao vipDao;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        userDao = (UserDao) context.getBean("userDao");
        vipDao = (VipDao) context.getBean("vipDao");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        request.setCharacterEncoding("utf-8");

        MyRequestUtil.getHost(request);//初始化host，与其他无关

        String deviceId = request.getParameter("deviceId");
        if (!MyUtil.isEmpty(deviceId)) {
            UserTable uts = null;
            try {
                uts = MyRequestUtil.getUserTable(request);
            } catch (Exception e) {
            }
            //如果session中不存在用户信息，则从数据库里查询放到session里
            if (uts == null) {
                UserTable ut = userDao.getUserByDeviceId(deviceId);
                if (ut != null) {
                    Vip vip = vipDao.getVipByDeviceId(deviceId);//查询vip信息
                    if (vip != null) {
                        UserBean ub = ut.getUserBean();
                        ub.setVip(true);
                        ut.setUserBean(ub);
                    }
                    MyRequestUtil.setUserTable(request.getSession(),ut);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
