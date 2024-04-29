package com.kingpopen.jdbclearn.model;

import static com.kingpopen.jdbclearn.common.Const.ADDRESSES;
import static com.kingpopen.jdbclearn.common.Const.ADJECTIVES;
import static com.kingpopen.jdbclearn.common.Const.NOUNS;

import java.util.Random;
import java.util.UUID;
import lombok.Value;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 用户实体类
 * @date 2024/4/21 00:45:49
 */
@Value
public class User {
  String username;
  String identityId;
  String address;

  // 随机创建user
  public static User createUser(){
    Random random = new Random();
    final String username = ADJECTIVES.get(random.nextInt(10)) + NOUNS.get(random.nextInt(10));
    final String identityId = UUID.randomUUID().toString();
    final String address = ADDRESSES.get(random.nextInt(10));
    return new User(username, identityId, address);
  }
}
