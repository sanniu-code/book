package cn.duansanniu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.duansanniu.mapper")
public class BookApplication {

    public static void main(String[] args) {


        SpringApplication.run(BookApplication.class, args);
    }

}
