package com.kingpopen.mybatisspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 彭锦波
 * @project Default (Template) Project
 * @description ${description}
 * @date 2024/5/5 13:20:36
 */
@MapperScan("com.kingpopen.mybatisspringboot.infrastructure.dao")
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}