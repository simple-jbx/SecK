package tech.snnukf.seckillsys.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.rabbitmq.MQSender;
import tech.snnukf.seckillsys.vo.RespBean;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author simple.jbx
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;


    /**
     * @author simple.jbx
     * @description 用户信息（测试）
     * @date 16:09 2021/10/18
     * @param	user
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    @RequestMapping("info")
    @ResponseBody
    public RespBean info(User user) {
        return RespBean.success(user);
    }


//    /**
//     * @author simple.jbx
//     * @description 测试RabbitMQ发送消息
//     * @date 20:42 2021/11/3
//     * @param
//     * @return void
//     **/
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq() {
//        mqSender.send("Hello");
//    }
//
//    @ResponseBody
//    @RequestMapping("/mq/direct01")
//    public void mq02() {
//        mqSender.send01("Hello,Red");
//    }
//
//    @ResponseBody
//    @RequestMapping("/mq/direct02")
//    public void mq03() {
//        mqSender.send02("Hello,Green");
//    }
//
//    /**
//     * @author simple.jbx
//     * @description Topic
//     * @date 16:34 2021/11/8
//     * @param
//     * @return void
//     **/
//    @ResponseBody
//    @RequestMapping("/mq/topic01")
//    public void mq04() {
//        mqSender.send03("Hello,Red");
//    }
//
//    @ResponseBody
//    @RequestMapping("/mq/topic02")
//    public void mq05() {
//        mqSender.send04("Hello,Green");
//    }
//
//    @ResponseBody
//    @RequestMapping("/mq/header01")
//    public void mq06() {
//        mqSender.send05("Hello,Head01");
//    }
//
//    @ResponseBody
//    @RequestMapping("/mq/header02")
//    public void mq07() {
//        mqSender.send06("Hello,Head02");
//    }
}
