package tech.snnukf.seckillsys.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.snnukf.seckillsys.vo.RespBeanEnum;

/**
 * @author simple.jbx
 * @ClassName GlobalException
 * @description 全局异常
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021年10月07日 15:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private RespBeanEnum respBeanEnum;
}
