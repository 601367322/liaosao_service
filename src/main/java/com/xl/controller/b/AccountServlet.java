package com.xl.controller.b;

import com.xl.bean.Account;
import com.xl.bean.UserTable;
import com.xl.dao.AccountDao;
import com.xl.util.MyJSONUtil;
import com.xl.util.MyRequestUtil;
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
        UserTable user = MyRequestUtil.getUserTable(session);
        Account myAccount = accountDao.getAccountByDeviceId(user.getDeviceId());
        if (myAccount == null) {
            myAccount = new Account();
            myAccount.setDeviceId(user.getDeviceId());
        }

        if (account != null) {
            if (account.getZhifubao() != null)
                myAccount.setZhifubao(account.getZhifubao());
        }

        accountDao.saveOrUpdate(myAccount);
        return MyJSONUtil.getSuccessJsonObject(myAccount);
    }

    /**
     * 获取用户账户信息
     *
     * @return
     */
    @RequestMapping(value = "/getaccount")
    public
    @ResponseBody
    Object getAccountDetail() throws Exception {
        UserTable user = MyRequestUtil.getUserTable(session);
        Account myAccount = accountDao.getAccountByDeviceId(user.getDeviceId());
        if (myAccount == null) {
            myAccount = new Account();
            myAccount.setDeviceId(user.getDeviceId());
            accountDao.save(myAccount);
        }
        return MyJSONUtil.getSuccessJsonObject(myAccount);
    }
}
