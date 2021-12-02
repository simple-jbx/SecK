package tech.snnukf.seckillsys.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author simple.jbx
 * @ClassName UUIDUtil
 * @description //TODO
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021年10月07日 17:17
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

