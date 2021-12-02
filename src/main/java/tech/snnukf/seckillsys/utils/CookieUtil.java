package tech.snnukf.seckillsys.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @author simple.jbx
 * @ClassName CookieUtil
 * @description //TODO
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021年10月07日 17:18
 */
public final class CookieUtil {

    /**
     * @author simple.jbx
     * @description 得到Cookie的值, 不编码
     * @date 17:26 2021/10/7
     * @param	request
     * @param	cookieName
     * @return java.lang.String
     **/
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * @author simple.jbx
     * @description 得到Cookie的值
     * @date 17:27 2021/10/7
     * @param	request
     * @param	cookieName
     * @param	isDecoder
     * @return java.lang.String
     **/
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }


    /**
     * @author simple.jbx
     * @description 得到Cookie的值
     * @date 17:27 2021/10/7
     * @param	request
     * @param	cookieName
     * @param	encodeString
     * @return java.lang.String
     **/
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }


    /**
     * @author simple.jbx
     * @description 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
     * @date 17:27 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @param	cookieValue
     * @return void
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }


    /**
     * @author simple.jbx
     * @description 设置Cookie的值 在指定时间内生效,但不编码
     * @date 17:28 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @param	cookieValue
     * @param	cookieMaxage
     * @return void
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
    }

    /**
     * @author simple.jbx
     * @description 设置Cookie的值 不设置生效时间,但编码
     * @date 17:28 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @param	cookieValue
     * @param	isEncode
     * @return void
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * @author simple.jbx
     * @description 设置Cookie的值 在指定时间内生效, 编码参数
     * @date 17:28 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @param	cookieValue
     * @param	cookieMaxage
     * @param	isEncode
     * @return void
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }


    /**
     * @author simple.jbx
     * @description 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     * @date 17:28 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @param	cookieValue
     * @param	cookieMaxage
     * @param	encodeString
     * @return void
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
    }


    /**
     * @author simple.jbx
     * @description 删除Cookie带cookie域名
     * @date 17:29 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @return void
     **/
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    
    /**
     * @author simple.jbx
     * @description 设置Cookie的值，并使其在指定时间内生效
     * @date 17:29 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @param	cookieValue
     * @param	cookieMaxage cookie生效的最大秒数
     * @param	isEncode
     * @return void
     **/
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0) {
                cookie.setMaxAge(cookieMaxage);
            }
            if (null != request) {
                // 设置域名的cookie
                String domainName = getDomainName(request);
                //System.out.println("dom " + domainName);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @author simple.jbx
     * @description 设置Cookie的值，并使其在指定时间内生效
     * @date 17:29 2021/10/7
     * @param	request
     * @param	response
     * @param	cookieName
     * @param	cookieValue
     * @param	cookieMaxage cookie生效的最大秒数
     * @param	encodeString
     * @return void
     **/
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0) {
                cookie.setMaxAge(cookieMaxage);
            }
            if (null != request) {
                // 设置域名的cookie
                String domainName = getDomainName(request);
                //System.out.println(domainName);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @author simple.jbx
     * @description 得到cookie的域名
     * @date 17:30 2021/10/7
     * @param	request
     * @return java.lang.String
     **/
    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;
        // 通过request对象获取访问的url地址
        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            // 将url地下转换为小写
            serverName = serverName.toLowerCase();
//            System.out.println("ori serverName ==> " + serverName);
            // 如果url地址是以http://开头  将http://截取
            if (serverName.startsWith("http://")) {
                serverName = serverName.substring(7);
            } else if (serverName.startsWith("https://")) {
                serverName = serverName.substring(8);
            }

            int end = serverName.length();
            // 判断url地址是否包含"/"
            if (serverName.contains("/")) {
                //得到第一个"/"出现的位置
                end = serverName.indexOf("/");
            }

            // 截取
            serverName = serverName.substring(0, end);
//            System.out.println("subServerName ==> " + serverName);
            // 根据"."进行分割
//            final String[] domains = serverName.split("\\.");
//            System.out.println("arrays ==> " + Arrays.toString(domains));
//            int len = domains.length;
//            if (len > 3) {
//                // www.xxx.com.cn
//                domainName = domains[0] + "." + domains[1] + "." + domains[2];
//            } else if (len <= 3 && len > 1) {
//                // xxx.com or xxx.cn
//                domainName = domains[0] + "." + domains[1];
//            } else {
//                domainName = serverName;
//            }
        }

        domainName = serverName;
        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
//        System.out.println("finalDomainName ==> " + domainName);
        return domainName;
    }
}