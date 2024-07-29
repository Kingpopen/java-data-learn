package com.kingpopen.service;

import com.kingpopen.model.Stock;
import java.util.List;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 遇到Bug的service
 * @date 2024/07/22 20:20:39
 */
public interface ProblemService {

  /**
   * 批量插入Stocks
   * @param stocks
   */
  void insertStocks(final List<Stock> stocks);
}
