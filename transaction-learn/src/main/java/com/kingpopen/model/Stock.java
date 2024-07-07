package com.kingpopen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 仓储类
 * @date 2024/7/7 12:17:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
  private Integer id;
  private String productCode;
  private Integer count;
  private String address;

  /**
   * 修改数目
   * @param num
   */
  public void changeCount(final Integer num){
    count  = num;
  }
}
