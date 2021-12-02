package tech.snnukf.seckillsys.service;

import tech.snnukf.seckillsys.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.snnukf.seckillsys.vo.RespBean;
import tech.snnukf.seckillsys.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author simple.jbx
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * */
public interface IUserService extends IService<User> {

    /**
     * @return tech.snnukf.seckillsys.vo.RespBean
     * @author simple.jbx
     * @description Login
     * @date 15:48 2021/10/1
     * @param    loginVo
     * @param request
     * @param response
     **/
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);


    /**
     * @author simple.jbx
     * @description 根据Cookie获取用户
     * @date 15:14 2021/11/25
     * @param	userTicket
     * @param	request
     * @param	response
     * @return tech.snnukf.seckillsys.pojo.User
     **/
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);


    /**
     * @author simple.jbx
     * @description 更新密码
     * @date 16:22 2021/10/30
     * @param	userTicket
     * @param	password
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request,
                            HttpServletResponse response);
}
