package com.xuan.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xuan.en.MinCountRecord;

@Repository
public interface MinCountRecordRepository extends JpaRepository<MinCountRecord, String>{

    @Query(value = "select * from min_count_record where inserttime > :inserttime ", nativeQuery = true)
	List<MinCountRecord> findByInserttime(String inserttime);
    
    @Transactional
    @Modifying
    @Query(value = "delete from min_count_record where inserttime < :inserttime ", nativeQuery = true)
	void removeByInserttime(String inserttime);
}
