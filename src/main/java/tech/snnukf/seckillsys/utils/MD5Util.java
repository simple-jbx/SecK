package tech.snnukf.seckillsys.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author simple.jbx
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @ClassName MD5Util
 * @description: MD5工具类
 * @date 2021年09月29日 17:14
 */
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "a1b2c3d";

    public static String inputPass2FormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPass2DBPass(String fromPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String formPass = inputPass2FormPass(inputPass);
        String dbPass = formPass2DBPass(formPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPass2FormPass("a30c004154235ca5fd5f88984e086508"));
        System.out.println(formPass2DBPass(inputPass2FormPass("112233"), "a1b2c3d"));
        System.out.println(inputPassToDBPass("a30c004154235ca5fd5f88984e086508", "a1b2c3d"));
    }
}
