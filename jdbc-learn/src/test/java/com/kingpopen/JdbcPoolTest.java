package com.kingpopen;

import com.kingpopen.jdbclearn.JdbcPool;
import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/04/22 22:33:06
 */
class JdbcPoolTest {

  @Test
  public void testDoCrudByDruid() {
    JdbcPool jdbcPool = new JdbcPool();
    jdbcPool.doCrudByDruid();
  }
}