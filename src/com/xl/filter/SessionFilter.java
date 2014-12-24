package com.xl.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xl.bean.UserTable;
import com.xl.dao.UserDao;
import com.xl.util.ResultCode;

public class SessionFilter extends OncePerRequestFilter {

	public UserDao userDao;
	
	@Override
	protected void initFilterBean() throws ServletException {
		// TODO Auto-generated method stub
		super.initFilterBean();
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		userDao = (UserDao) context.getBean("userDao");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		/*if (uri.indexOf("/b/") != -1) {
			boolean doFilter = true;
			if (doFilter) {
				Object obj = request.getSession().getAttribute("user");
				if (null == obj) {
					Object token = request.getParameter("token");
					if (token != null) {
						UserTable ut=userDao.getUserByToken(token.toString());
						if(ut==null){
							retrunFaile(request, response);
						}else{
							 request.getSession().setAttribute("user", ut);
							 filterChain.doFilter(request, response);
						}
					} else {
						retrunFaile(request, response);
					}
				} else {
					filterChain.doFilter(request, response);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		} else {*/
			filterChain.doFilter(request, response);
//		}
	}

	public void retrunFaile(HttpServletRequest request,
			HttpServletResponse response){
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			JSONObject jo = new JSONObject();
			jo.put(ResultCode.STATUS, ResultCode.FAIL);
			PrintWriter out = response.getWriter();
			out.println(jo.toString());
			out.flush();
			out.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
