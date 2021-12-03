package tech.snnukf.seckillsys.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import tech.snnukf.seckillsys.pojo.User;


/**
 * @author simple.jbx
 * @ClassName UserArgumentResolve
 * @description 用户自定义参数
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021年10月08日 19:46
 */
@Component
@Slf4j
public class UserArgumentResolve implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return UserContext.getUser();
    }
}
