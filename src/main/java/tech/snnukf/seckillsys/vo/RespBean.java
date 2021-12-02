package tech.snnukf.seckillsys.vo;

import lombok.*;

/**
 * @author jb.xue@qq.com https://github.com/simple-jbx
 * @ClassName RespBean
 * @description: 公共返回对象枚举
 * @date 2021年10月01日 11:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    /**
     * @author simple.jbx https://github.com/simple-jbx
     * @description success result
     * @date 15:24 2021/10/1
     * @param
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    public static RespBean success() {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), null);
    }

    public static RespBean success(Object obj) {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), obj);
    }

    /**
     * @author simple.jbx
     * @email jb.xue@qq.com
     * @github https://github.com/simple-jbx     * @description 失败返回结果
     * @date 15:27 2021/10/1
     * @param	respBeanEnum
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    public static RespBean error(RespBeanEnum respBeanEnum) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), null);
    }

    public static RespBean error(RespBeanEnum respBeanEnum, Object obj) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), obj);
    }
}
