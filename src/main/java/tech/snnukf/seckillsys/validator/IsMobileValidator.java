package tech.snnukf.seckillsys.validator;

import org.apache.commons.lang3.StringUtils;
import tech.snnukf.seckillsys.utils.ValidatorUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author simple.jbx
 * @ClassName IsMobileValidator
 * @description 手机号码校验规则
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021年10月07日 15:05
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required) {
            return ValidatorUtil.isMobile(value);
        }else {
            if(StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
