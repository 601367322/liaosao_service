package com.xl.controller.a;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xl.bean.UserBean;
import com.xl.bean.UserTable;
import com.xl.bean.Vip;
import com.xl.dao.UserDao;
import com.xl.dao.VipDao;
import com.xl.socket.StaticUtil;
import com.xl.util.DefaultDefaultValueProcessor;
import com.xl.util.MD5;
import com.xl.util.MyUtil;
import com.xl.util.ResultCode;

@Controller
@RequestMapping(value = "/a")
public class LoginServlet {
	@Resource
	public UserDao userDao;
	@Resource
	public VipDao vipDao;

	@RequestMapping(value = "/getuserinfo")
	public @ResponseBody
	Object getUserInfo(@RequestParam String deviceId) {
		JSONObject jo = new JSONObject();
		try {
			UserTable ut = userDao.getUserByDeviceId(deviceId);
			if (ut == null) {
				ut = new UserTable(deviceId);
				UserBean ub = new UserBean();
				ut.setDetail(JSONObject.fromObject(ub,
						DefaultDefaultValueProcessor.getJsonConfig())
						.toString());
				ut.setDeviceId(deviceId);
				
				userDao.save(ut);
			}
			UserBean ub = (UserBean) JSONObject.toBean(
					JSONObject.fromObject(ut.getDetail()), UserBean.class);
			Vip vip = vipDao.getVipByDeviceId(deviceId.length() > 16 ? MyUtil
					.getmd5DeviceId(deviceId) : deviceId);
			if (vip == null) {
				ub.setVip(false);
			} else {
				ub.setVip(true);
			}
			ut.setDetail(JSONObject.fromObject(ub,
					DefaultDefaultValueProcessor.getJsonConfig()).toString());

			jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
			jo.put(StaticUtil.CONTENT, ut);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jo.put(ResultCode.STATUS, ResultCode.FAIL);
		}
		return jo;
	}
}
