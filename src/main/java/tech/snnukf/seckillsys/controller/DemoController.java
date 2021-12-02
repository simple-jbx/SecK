package tech.snnukf.seckillsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author simple.jbx
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @ClassName DemoController
 * @description:
 * @date 2021年09月29日 09:34
 */

@Controller
@RequestMapping("/demo")
public class DemoController {
    
    /**
     * @author simple.jbx
     * @description 测试页面跳转
     * @date 15:59 2021/9/29
     * @param	model	
     * @return java.lang.String
     **/
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "simple");
        return "hello";
    }
}
