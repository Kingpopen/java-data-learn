package com.kingpopen.infrastructure.dao;

import com.kingpopen.model.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description StockMapper
 * @date 2024/7/7 12:18:55
 */
@Mapper
public interface StockMapper {
  Stock findById(@Param("id") final Integer id);

  int update(@Param("stock") final Stock stock);

  int deleteById(@Param("id") final Integer id);

  int save(@Param("stock") final Stock stock);
}
