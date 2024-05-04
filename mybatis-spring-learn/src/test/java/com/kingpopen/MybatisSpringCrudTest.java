package com.kingpopen;

import static org.junit.jupiter.api.Assertions.*;

import com.kingpopen.infrastructure.common.MybatisConfig;
import com.kingpopen.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/5/4 20:06:01
 */
class MybatisSpringCrudTest {
  @Test
  public void testMybatisCrud(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(MybatisConfig.class);
    context.register(MybatisSpringCrud.class);
    context.refresh();

    MybatisSpringCrud api = context.getBean("mybatisSpringCrud", MybatisSpringCrud.class);

    User user;
    int cnt = 0;
    user = User.createUser("黄老爷", 2001, "北京市");
    cnt = api.save(user);
    assertEquals(1, cnt);

    user.setAddress("武汉市");
    cnt = api.updateByUsername(user);
    assertEquals(1, cnt);

    api.queryByUsername("黄老爷");

    cnt = api.deleteByUsername("黄老爷");
    assertEquals(1, cnt);
  }
}