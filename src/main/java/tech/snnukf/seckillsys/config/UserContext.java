package tech.snnukf.seckillsys.config;

import tech.snnukf.seckillsys.pojo.User;

/**
 * @author simple.jbx
 * @ClassName UserContext
 * @description //TODO
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021/11/23/ 19:46
 */
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}
