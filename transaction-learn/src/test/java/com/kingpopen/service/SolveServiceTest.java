package com.kingpopen.service;

import com.kingpopen.model.Stock;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/7/7 15:31:54
 */
@SpringBootTest
class SolveServiceTest {

  @Resource
  private SolveService service;

  @Resource
  @Qualifier("reproduceProblemService")
  private ProblemService reproduceProblemService;

  @Resource
  @Qualifier("solveProblem1Service")
  private ProblemService solveProblem1Service;

  @Resource
  @Qualifier("solveProblem2Service")
  private ProblemService solveProblem2Service;

  @Resource
  @Qualifier("solveProblem3Service")
  private ProblemService solveProblem3Service;

  @Resource
  @Qualifier("bestPractiseService")
  private ProblemService bestPractiseService;

  @Test
  public void testReproduceProblem() {
    reproduceProblemService.insertStocks(initData());
  }

  @Test
  public void testSolveProblem1(){
    solveProblem1Service.insertStocks(initData());
  }

  @Test
  public void testSolveProblem2(){
    solveProblem2Service.insertStocks(initData());
  }

  @Test
  public void testSolveProblem3(){
    solveProblem3Service.insertStocks(initData());
  }

  @Test
  public void testBestPractise(){
    bestPractiseService.insertStocks(initData());
  }

  @Test
  public void testIt(){
    service.bestPractice(initData());
  }

  private List<Stock> initData(){
    List<Stock> stocks = new ArrayList<>();
    int total = 10, cnt = 0;
    for (int i = 0; i<total; i++){
      Stock stock1 = new Stock(null, String.valueOf(++cnt), 100, "深圳仓");
      Stock stock2 = new Stock(null, String.valueOf(++cnt), 150, "上海仓");
      stocks.add(stock1);
      stocks.add(stock2);
    }
    return stocks;
  }
}