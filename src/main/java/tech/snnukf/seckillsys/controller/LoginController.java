package tech.snnukf.seckillsys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.snnukf.seckillsys.service.IUserService;
import tech.snnukf.seckillsys.vo.LoginVo;
import tech.snnukf.seckillsys.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author simple.jbx
 * @ClassName LoginController
 * @description:
 * @date 2021年09月30日 16:04
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * @author simple.jbx
     * @email jb.xue@qq.com
     * @github https://github.com/simple-jbx
     * @description 跳转登陆页面
     * @date 16:06 2021/9/30
     * @param		
     * @return String
     **/
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /**
     * @author simple.jbx
     * @description 登陆功能
     * @date 15:40 2021/10/1
     * @param	loginVo	
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        return userService.doLogin(loginVo, request, response);
    }
}
