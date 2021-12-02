package tech.snnukf.seckillsys.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.jws.soap.SOAPBinding;

/**
 * @author simple.jbx
 * @ClassName SeckillMessage
 * @description 秒杀信息
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021/11/08/ 19:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodId;
}
