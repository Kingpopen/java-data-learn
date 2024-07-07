package com.kingpopen.service.impl;

import com.kingpopen.infrastructure.dao.StockMapper;
import com.kingpopen.model.Stock;
import com.kingpopen.service.StockService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description StockService 声明式事务实现
 * @date 2024/7/7 12:30:25
 */
@Slf4j
@Service
public class StockServiceDeclarativeImpl implements StockService {

  @Autowired
  private StockMapper mapper;

  @Override
  public Stock getById(Integer id) {
    return mapper.findById(id);
  }

  @Override
  public int removeById(Integer id) {
    return mapper.deleteById(id);
  }

  @Override
  public int updateStock(Stock stock){
    return mapper.update(stock);
  }

  @Override
  public int saveStock(Stock stock) {
    return mapper.save(stock);
  }

  @Transactional
  @Override
  public int updateStockWithTransactionAndRunTimeException(Stock stock){
    int cnt = mapper.update(stock);
    // 返回一个 RunTimeException的子类
    return cnt / 0;
  }

  @Transactional(rollbackFor = IOException.class)
  @Override
  public int updateStockWithTransactionAndUnCheckException(Stock stock) throws IOException {
    mapper.update(stock);
    throw new IOException("unCheck Exception!");
  }

}
