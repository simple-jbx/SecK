package tech.snnukf.seckillsys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("tech.snnukf.seckillsys.mapper")
public class SecKillSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecKillSysApplication.class, args);
    }
}
