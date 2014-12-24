package com.xl.controller.a;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xl.bean.UserTable;
import com.xl.dao.UserDao;
import com.xl.socket.StaticUtil;
import com.xl.util.MD5;

@Controller
@RequestMapping(value = "/a")
public class LoginServlet {
	@Resource
	public UserDao userDao;

	@RequestMapping(value = "/login", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object tempLogin(@ModelAttribute UserTable userTable,
			HttpSession httpSession) {
		System.out.println(userTable.getDeviceId());
		httpSession.setAttribute(StaticUtil.DEVICEID, userTable.getDeviceId());
		UserTable temp = userDao.getUserByUserName(userTable);

		UserTable user = null;
		if (temp == null) {
			userTable.setToken(MD5.GetMD5Code(userTable.getUserName()
					+ userTable.getDeviceId()
					+ String.valueOf(new Date().getTime())));
			userTable
					.setId(Integer.valueOf(userDao.save(userTable).toString()));
			user = userTable;
		} else {
			temp.setToken(MD5.GetMD5Code(userTable.getUserName()
					+ userTable.getDeviceId()
					+ String.valueOf(new Date().getTime())));
			if (userTable.getUserType() > 0) {
				temp.setOtherToken(userTable.getOtherToken());
			}
			userDao.update(temp);
			user = temp;
		}
		
		httpSession.setAttribute("user", user);
		return user;
	}
}
