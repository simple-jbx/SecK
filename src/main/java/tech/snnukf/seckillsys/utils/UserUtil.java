package tech.snnukf.seckillsys.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.vo.RespBean;

import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author simple.jbx
 * @ClassName UserUtil
 * @description 生成用户工具类
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021/10/22/ 20:53
 */
public class UserUtil {
    private static void createUser(int count) throws Exception{
        List<User> userList = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(15000000000L + i);
            user.setNickname("user" + i);
            user.setSlat("a1b2c3d");
            user.setPassword(MD5Util.inputPassToDBPass("112233", user.getSlat()));
            user.setLoginCount(1);
            user.setRegisterDate(new Date());
            userList.add(user);
        }

        System.out.println("create user");
        //insert into db
        Connection conn = getConn();
        String sql = "insert into t_user(login_count, nickname, register_date, slat, password, id) " +
                "values(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            preparedStatement.setInt(1, user.getLoginCount());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            preparedStatement.setString(4, user.getSlat());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getId());
            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();
        preparedStatement.clearParameters();
        conn.close();
        System.out.println("insert into db");

        //create cookie
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("F:\\Desktop\\config.txt");
        if(file.exists()) {
            file.delete();
        }

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out= co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPass2FormPass("112233");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObj();
            System.out.println("create userTicket :" + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file :" + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    private static Connection getConn() throws Exception {
        final String url = "jdbc:mysql://192.168.152.130:3306/sec_kill_sys?useUnicode=true&useSSL=false&characterEncoding=utf8";
        final String username = "root";
        final String password = "41512241";
        final String driver = "com.mysql.cj.jdbc.Driver";

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception{
        createUser(5000);
    }
}
