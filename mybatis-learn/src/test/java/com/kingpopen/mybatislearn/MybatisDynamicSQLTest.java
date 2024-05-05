package com.kingpopen.mybatislearn;

import com.kingpopen.mybatislearn.model.User;
import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/5/3 10:32:51
 */
class MybatisDynamicSQLTest {

  @Test
  public void testQueryByIf() {
    MybatisDynamicSQL api = new MybatisDynamicSQL();
    User conditionUser = User.createUser("李四", 1002, "");
    api.queryByIf(conditionUser);
  }

  @Test
  public void testQueryByWhere() {
    MybatisDynamicSQL api = new MybatisDynamicSQL();
    User conditionUser = User.createUser("", 1002, "");
    api.queryByWhere(conditionUser);
  }

  @Test
  public void testQueryByTrim() {
    MybatisDynamicSQL api = new MybatisDynamicSQL();
    User conditionUser = User.createUser("", 1002, "");
    api.queryByTrim(conditionUser);
  }

  @Test
  public void testQueryByChoose() {
    MybatisDynamicSQL api = new MybatisDynamicSQL();
    User conditionUser = User.createUser("张三", 100, "");
    api.queryByChoose(conditionUser);
  }

  @Test
  public void testQueryByForeach() {
    MybatisDynamicSQL api = new MybatisDynamicSQL();
    Integer[] identityIds = {1001, 1002, 1003};
    api.queryByForeach(identityIds);
  }

  @Test
  public void testQueryBySql() {
    MybatisDynamicSQL api = new MybatisDynamicSQL();
    api.queryBySql(2);
  }
}