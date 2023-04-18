package com.xuan.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xuan.Application;
import com.xuan.en.MinCountRecord;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = Application.class)
public class MinCountRecordRepositoryTest {

	@Autowired
	private MinCountRecordRepository minCountRecordRepository;
	
	@Test
	public void initTest() throws Exception {
		MinCountRecord  minCountRecord = new MinCountRecord();
		minCountRecord.setInserttime("" + System.currentTimeMillis());
		minCountRecordRepository.save(minCountRecord);
		List<MinCountRecord> list = minCountRecordRepository.findByInserttime("" + (System.currentTimeMillis()  - 100000));
		assertNotNull(list);
	}
	
	@Test
	public void removeTest() throws Exception {
		minCountRecordRepository.removeByInserttime("" + System.currentTimeMillis());
		MinCountRecord  minCountRecord = new MinCountRecord();
		minCountRecord.setInserttime("" + System.currentTimeMillis());
		minCountRecordRepository.save(minCountRecord);
		List<MinCountRecord> list = minCountRecordRepository.findByInserttime("" + (System.currentTimeMillis()  - 100000));
		assertNotNull(list);
	}
}
