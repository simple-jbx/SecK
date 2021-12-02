package tech.snnukf.seckillsys.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tech.snnukf.seckillsys.validator.IsMobile;

import javax.validation.constraints.NotNull;

/**
 * @author simple.jbx
 * @ClassName LoginVo
 * @description //TODO
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021年10月01日 15:39
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;
}
