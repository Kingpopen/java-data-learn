package com.kingpopen;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description crud的测试类
 * @date 2024/4/20 22:10:21
 */
class JdbcCrudTest {
  @Test
  public void testJdbcCrudInsert(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::insert);
  }

  @Test
  public void testJdbcCrudQuery(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::query);
  }

  @Test
  public void testJdbcCrudUpdate(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::update);
  }

  @Test
  public void testJdbcCrudDelete(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::delete);
  }

  @Test
  public void testJdbcCrudInsertPrepare(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::insertPrepare);
  }

  @Test
  public void testJdbcCrudQueryPrepare(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::queryPrepare);
  }

  @Test
  public void testJdbcCrudUpdatePrepare(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::updatePrepare);
  }

  @Test
  public void testJdbcCrudDeletePrepare(){
    JdbcCrud api = new JdbcCrud();
    api.doCrud(api::deletePrepare);
  }


}