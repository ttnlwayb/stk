package com.xuan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.xuan.en.Stock;

@Repository
public interface StockRepository  extends JpaRepository<Stock, String>{

    @Query(value = "select * from stock where stkgroup = '元大50'", nativeQuery = true)
	List<Stock> find0050();
}
