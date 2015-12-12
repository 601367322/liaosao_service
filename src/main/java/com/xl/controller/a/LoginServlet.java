package com.xl.controller.a;

import com.xl.bean.UserBean;
import com.xl.bean.UserTable;
import com.xl.bean.Vip;
import com.xl.dao.UserDao;
import com.xl.dao.VipDao;
import com.xl.socket.StaticUtil;
import com.xl.util.MyRequestUtil;
import com.xl.util.ResultCode;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/a")
public class LoginServlet {
	@Resource
	public UserDao userDao;
	@Resource
	public VipDao vipDao;
    @Autowired
    HttpServletRequest request;


    /**
     * 查询用户信息
     * @param deviceId
     * @return
     */
	@RequestMapping(value = "/getuserinfo")
	public @ResponseBody
	Object getUserInfo(@RequestParam String deviceId) {
		JSONObject jo = new JSONObject();
		try {
            //先从数据库查询，如果没有，则创建一个用户
			UserTable ut = userDao.getUserByDeviceId(deviceId);
			if (ut == null) {
				ut = new UserTable(deviceId);
				UserBean ub = new UserBean();
				ut.setUserBean(ub);//设置用户的基本信息
				userDao.save(ut);
			}

            //获取VIP信息
            UserBean ub = ut.getUserBean();
            ub.setLogo(MyRequestUtil.getHost(request)+ub.getLogo());
			Vip vip = vipDao.getVipByDeviceId(deviceId);
            ub.setVip(vip == null ? false : true);
			ut.setUserBean(ub);

            //将用户信息放回
			jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
			jo.put(StaticUtil.CONTENT, ut);
		} catch (Exception e) {
			e.printStackTrace();
			jo.put(ResultCode.STATUS, ResultCode.FAIL);
		}
		return jo;
	}
}
