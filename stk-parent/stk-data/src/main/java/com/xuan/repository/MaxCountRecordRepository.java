package com.xuan.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xuan.en.MaxCountRecord;
import com.xuan.en.MaxCountRecord;

@Repository
public interface MaxCountRecordRepository extends JpaRepository<MaxCountRecord, String>{

    @Query(value = "select * from max_count_record where inserttime > :inserttime ", nativeQuery = true)
	List<MaxCountRecord> findByInserttime(String inserttime);
    
    @Transactional
    @Modifying
    @Query(value = "delete from max_count_record where inserttime < :inserttime ", nativeQuery = true)
	void removeByInserttime(String inserttime);
}
