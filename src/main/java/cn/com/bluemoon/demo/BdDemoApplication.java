package cn.com.bluemoon.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@MapperScan("cn.com.bluemoon.demo.mapper")
public class BdDemoApplication {

	public static void main(String[] args) {
        SpringApplication.run(BdDemoApplication.class, args);
	}

}