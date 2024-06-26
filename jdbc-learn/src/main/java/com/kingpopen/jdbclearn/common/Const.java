package com.kingpopen.jdbclearn.common;

import java.util.Arrays;
import java.util.List;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 常量类
 * @date 2024/4/21 00:32:15
 */
public class Const {
  public static final String URL_NORMAL = "jdbc:mysql://localhost:13306/mydatabase";

  public static final String URL_BATCH = "jdbc:mysql://localhost:13306/mydatabase?rewriteBatchedStatements=true";

  public static final String USERNAME = "root";
  public static final String PASSWORD = "root";

  // 用户构造用户信息  username = adjective + nouns
  public static final List<String> ADJECTIVES = Arrays.asList("优雅的", "可靠的", "仔细的", "帅气的", "冷漠的", "幽默的", "无私的", "聪明的", "顽皮的", "漂亮的");
  public static final List<String> NOUNS = Arrays.asList("有钱人", "普通人", "明星", "程序员", "老板", "打工人", "建筑师", "设计师", "hr", "产品经理");
  public static final List<String> ADDRESSES = Arrays.asList("深圳市", "广州市", "重庆市", "武汉市", "天津市", "成都市", "香港", "澳门", "台湾", "上海市");
}
