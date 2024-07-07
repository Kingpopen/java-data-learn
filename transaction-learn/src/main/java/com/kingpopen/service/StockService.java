package com.kingpopen.service;

import com.kingpopen.model.Stock;
import java.io.IOException;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description StockService
 * @date 2024/7/7 12:28:21
 */
public interface StockService {

  /**
   * 查找stock
   * @param id
   * @return
   */
  Stock getById(final Integer id);

  /**
   * 删除stock
    * @param id
   * @return
   */
  int removeById(final Integer id);

  /**
   * 更新stock
   * @param stock
   * @return
   */
  int updateStock(final Stock stock);

  /**
   * 新增stock
   * @param stock
   * @return
   */
  int saveStock(final Stock stock);

  int updateStockWithTransactionAndRunTimeException(final Stock stock);

  int updateStockWithTransactionAndUnCheckException(final Stock stock) throws IOException;
}
