package tech.snnukf.seckillsys.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.service.IOrderService;
import tech.snnukf.seckillsys.vo.OrderDetailVo;
import tech.snnukf.seckillsys.vo.RespBean;
import tech.snnukf.seckillsys.vo.RespBeanEnum;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author simple.jbx
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    /**
     * @author simple.jbx
     * @description 订单详情
     * @date 19:58 2021/11/1
     * @param	user
     * @param	orderId
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }

}