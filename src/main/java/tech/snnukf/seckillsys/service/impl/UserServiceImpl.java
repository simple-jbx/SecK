package tech.snnukf.seckillsys.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tech.snnukf.seckillsys.config.UserContext;
import tech.snnukf.seckillsys.exception.GlobalException;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.mapper.UserMapper;
import tech.snnukf.seckillsys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.snnukf.seckillsys.utils.CookieUtil;
import tech.snnukf.seckillsys.utils.MD5Util;
import tech.snnukf.seckillsys.utils.UUIDUtil;
import tech.snnukf.seckillsys.vo.LoginVo;
import tech.snnukf.seckillsys.vo.RespBean;
import tech.snnukf.seckillsys.vo.RespBeanEnum;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author simple.jbx
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
   @Autowired
   private UserMapper userMapper;

   @Autowired
   private RedisTemplate redisTemplate;


    /**
    * @author simple.jbx
    * @description Login
    * @date 15:56 2021/10/1
    * @param    loginVo
    * @param request
     * @param response
     * @return tech.snnukf.seckillsys.vo.RespBean
    **/
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String pass = loginVo.getPassword();

        //根据手机号获取用户
        User user = userMapper.selectById(mobile);

        if(null == user) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        if(!MD5Util.formPass2DBPass(pass, user.getSlat()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        String cookie = CookieUtil.getCookieValue(request, "userTicket");
        //生成Cookie
        if(StringUtils.isEmpty(cookie)) {
            cookie = UUIDUtil.uuid();
        }

        redisTemplate.opsForValue().set("user:" + cookie, user);
        UserContext.setUser(user);
        CookieUtil.setCookie(request, response, "userTicket", cookie);

        return RespBean.success(cookie);
    }

    /**
     * @author simple.jbx
     * @description 根据cookie获取User
     * @date 16:20 2021/10/30
     * @param	userTicket
     * @param	request
     * @param	response
     * @return tech.snnukf.seckillsys.pojo.User
     **/
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)) {
            return null;
        }

        User user = (User)redisTemplate.opsForValue().get("user:"+userTicket);
        if(user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }


    /**
     * @author simple.jbx
     * @description //TODO
     * @date 16:21 2021/10/30
     * @param	userTicket
     * @param	password
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request,
                               HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);

        if(user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }

        user.setPassword(MD5Util.inputPassToDBPass(password, user.getPassword()));
        int result = userMapper.updateById(user);

        if(1 == result) {
            //删除Redis
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
