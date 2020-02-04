package com.haozhi.greenroom;

import com.haozhi.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 10:18
 */
@SpringBootApplication
@MapperScan("com.haozhi.greenroom.dao")
public class GreenRoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(GreenRoomApplication.class,args);
    }
    //定义IdWorker的Bean
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
