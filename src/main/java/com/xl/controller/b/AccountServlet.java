package com.xl.controller.b;

import com.xl.bean.Account;
import com.xl.bean.UserTable;
import com.xl.dao.AccountDao;
import com.xl.socket.StaticUtil;
import com.xl.util.MyRequestUtil;
import com.xl.util.MyUtil;
import com.xl.util.ResultCode;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Shen on 2015/12/20.
 */
@Controller
@RequestMapping("/account")
public class AccountServlet {

    @Autowired
    AccountDao accountDao;
    @Autowired
    HttpSession session;

    /**
     * 设置用户信息
     *
     * @return
     */
    @RequestMapping(value = "/setaccount")
    public
    @ResponseBody
    Object setUserAccount(@ModelAttribute Account account) throws Exception {
        JSONObject jo = new JSONObject();
        UserTable user = MyRequestUtil.getUserTable(session);
        Account myAccount = accountDao.getAccountByDeviceId(user.getDeviceId());
        if (myAccount == null) {
            myAccount = new Account();
            myAccount.setDeviceId(user.getDeviceId());
        }

        if (account != null) {
            if (account.getZhifubao() != null)
                myAccount.setZhifubao(account.getZhifubao());
            if (account.getWeixin() != null)
                myAccount.setWeixin(account.getWeixin());
        }

        accountDao.saveOrUpdate(myAccount);
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.CONTENT, MyUtil.toJsonNoNull(myAccount));
        return jo;
    }

    /**
     * 获取用户账户信息
     *
     * @return
     */
    @RequestMapping(value = "/getaccount")
    public
    @ResponseBody
    Object getUserDetail() throws Exception {
        JSONObject jo = new JSONObject();
        UserTable user = MyRequestUtil.getUserTable(session);
        Account myAccount = accountDao.getAccountByDeviceId(user.getDeviceId());
        if (myAccount == null) {
            myAccount = new Account();
            myAccount.setDeviceId(user.getDeviceId());
        }
        accountDao.saveOrUpdate(myAccount);
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.CONTENT, MyUtil.toJsonNoNull(myAccount));
        return jo;
    }
}
