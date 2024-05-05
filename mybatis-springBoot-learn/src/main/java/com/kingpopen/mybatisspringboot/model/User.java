package com.kingpopen.mybatisspringboot.model;

import static com.kingpopen.mybatisspringboot.infrastructure.common.Const.ADDRESSES;
import static com.kingpopen.mybatisspringboot.infrastructure.common.Const.ADJECTIVES;
import static com.kingpopen.mybatisspringboot.infrastructure.common.Const.NOUNS;

import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 用户实体
 * @date 2024/5/5 13:43:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private Integer id;
  private String username;
  private String identityId;
  private String address;

  public static User createUser(final Integer id, final String username, final String identityId,
      final String address) {
    return new User(id, username, identityId, address);
  }

  public static User createUserByRandom(){
    Random random = new Random();
    final String username = ADJECTIVES.get(random.nextInt(10)) + NOUNS.get(random.nextInt(10));
    final String identityId = UUID.randomUUID().toString();
    final String address = ADDRESSES.get(random.nextInt(10));
    return new User(null, username, identityId, address);
  }

}
