package com.xl.controller.a;

import com.xl.bean.Album;
import com.xl.bean.UserBean;
import com.xl.bean.UserTable;
import com.xl.bean.Vip;
import com.xl.dao.AlbumDao;
import com.xl.dao.UserDao;
import com.xl.dao.VipDao;
import com.xl.util.MyJSONUtil;
import com.xl.util.MyRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/a")
public class LoginServlet {
    @Resource
    public UserDao userDao;
    @Resource
    public VipDao vipDao;
    @Resource
    public AlbumDao albumDao;
    @Autowired
    HttpServletRequest request;


    /**
     * 查询用户信息
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getuserinfo")
    public
    @ResponseBody
    Object getUserInfo(@RequestParam String deviceId) throws Exception {
        //先从数据库查询，如果没有，则创建一个用户
        UserTable newUt = userDao.getUserByDeviceId(deviceId);
        if (newUt == null) {
            newUt = new UserTable(deviceId);
            UserBean ub = new UserBean();
            newUt.setUserBean(ub);//设置用户的基本信息
            userDao.save(newUt);
        }
        //直接更改对象，会直接更改到缓存，从而会影响到其他地方
        UserTable ut = (UserTable) newUt.clone();

        //获取VIP信息
        UserBean ub = ut.getUserBean();
        ub.setLogo(MyRequestUtil.getHost(request) + ub.getLogo());
        Vip vip = vipDao.getVipByDeviceId(deviceId);
        List<Album> albums = albumDao.getAlbumListByDevicdId(deviceId);
        for (int i = 0; i < albums.size(); i++) {
            albums.get(i).setPath(MyRequestUtil.getHost(request) + albums.get(i).getPath());
        }
        ub.setAlbum(albums);
        ub.setVip(vip == null ? false : true);
        ut.setUserBean(ub);

        //将用户信息放回
        return MyJSONUtil.getSuccessJsonObject(ut);
    }
}
