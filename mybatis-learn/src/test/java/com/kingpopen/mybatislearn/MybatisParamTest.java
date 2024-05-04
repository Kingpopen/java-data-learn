package com.kingpopen.mybatislearn;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/5/2 22:03:17
 */
class MybatisParamTest {

  @Test
  public void testQueryByName(){
    MybatisParam api = new MybatisParam();
    api.queryByUsername("李四");
  }

  @Test
  public void testQueryByLikeName(){
    MybatisParam api = new MybatisParam();
    api.queryByLikeName("李四");
  }
}