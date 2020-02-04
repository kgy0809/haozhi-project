package com.haozhi.item;

import com.haozhi.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/19 16:15
 */
@SpringBootApplication
@MapperScan("com.haozhi.item.dao")
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }

    //定义IdWorker的Bean
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

}
