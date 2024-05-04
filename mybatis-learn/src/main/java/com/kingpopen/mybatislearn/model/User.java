package com.kingpopen.mybatislearn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 用户类
 * @date 2024/04/24 21:16:51
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String username;
  private int identityId;
  private String address;

  public static User createUser(final String username,
      final int identityId, final String address) {
    return new User(username, identityId, address);
  }
}
