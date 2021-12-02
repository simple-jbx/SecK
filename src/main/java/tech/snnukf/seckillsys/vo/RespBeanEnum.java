package tech.snnukf.seckillsys.vo;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Getter;

/**
 * @author simple.jbx
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @ClassName RespBeanEnum
 * @description:
 * @date 2021年10月01日 15:22
 */
@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常"),

    //登陆模块 5002xx
    LOGIN_ERROR(500210, "用户名或密码不正确"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    MOBILE_NOT_EXIST(500212, "手机号码不存在"),
    BIND_ERROR(500213, "参数校验异常"),
    PASSWORD_UPDATE_FAIL(500214, "密码更新失败"),
    SESSION_ERROR(500215, "用户不存在"),

    //秒杀模块 5005xx
    EMPTY_STOCK(500500, "库存不足"),
    REPEATE_ERROR(500501, "该商品每个人限购一件"),
    SECKILL_ERROR(500502, "抢购失败，请重试"),
    REQUEST_ILLEGAL(500503, "请求非法，请重试"),
    ERROR_CAPTCHA(500504, "验证码错误，请重新输入"),
    ACCESS_LIMIT_REACHED(500505, "访问过于频繁，请稍后再试"),


    //订单模块5003xx
    ORDER_NOT_EXIST(500300, "订单信息不存在"),
    ;

    private final Integer code;
    private final String message;
}
