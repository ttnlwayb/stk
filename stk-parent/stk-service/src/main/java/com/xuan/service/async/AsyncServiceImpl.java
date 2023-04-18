package com.xuan.service.async;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.xuan.en.MinCountRecord;
import com.xuan.en.MaxCountRecord;
import com.xuan.repository.MaxCountRecordRepository;
import com.xuan.repository.MinCountRecordRepository;

@Transactional
@Service
public class AsyncServiceImpl implements AsyncService {

	@Autowired
	MinCountRecordRepository minCountRecordRepository;

	@Autowired
	MaxCountRecordRepository maxCountRecordRepository;
	
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	
	/*@PostConstruct
	public void init() {
		threadPoolTaskExecutor.setCorePoolSize(1);
		threadPoolTaskExecutor.setMaxPoolSize(1);
	}*/
	@Async
	@Override
	public void saveMinCountRecord(int[] mins) {
		/*Thread t = new Thread(() -> {
		});
		t.start();
        threadPoolTaskExecutor.submit(new Thread(()->{
        	
        }));*/
    	_saveMinCountRecord(mins);

	}
	
	public void _saveMinCountRecord(int[] mins) {
		MinCountRecord minCountRecord = new MinCountRecord();
		minCountRecord.setInserttime("" + System.currentTimeMillis());
		minCountRecord.setCount1(mins[0]);
		minCountRecord.setCount2(mins[1]);
		minCountRecord.setCount3(mins[2]);
		minCountRecord.setCount4(mins[3]);
		minCountRecord.setCount5(mins[4]);
		minCountRecord.setCount6(mins[5]);
		minCountRecord.setCount7(mins[6]);
		minCountRecord.setCount8(mins[7]);
		minCountRecord.setCount9(mins[8]);
		minCountRecord.setCount10(mins[9]);
		minCountRecord.setCount11(mins[10]);
		minCountRecord.setCount12(mins[11]);
		minCountRecordRepository.save(minCountRecord);
	}

	@Async
	@Override
	public void saveMaxCountRecord(int[] maxs) {
    	_saveMaxCountRecord(maxs);
	}

	public void _saveMaxCountRecord(int[] maxs) {
		MaxCountRecord maxCountRecord = new MaxCountRecord();
		maxCountRecord.setInserttime("" + System.currentTimeMillis());
		maxCountRecord.setCount1(maxs[0]);
		maxCountRecord.setCount2(maxs[1]);
		maxCountRecord.setCount3(maxs[2]);
		maxCountRecord.setCount4(maxs[3]);
		maxCountRecord.setCount5(maxs[4]);
		maxCountRecord.setCount6(maxs[5]);
		maxCountRecord.setCount7(maxs[6]);
		maxCountRecord.setCount8(maxs[7]);
		maxCountRecord.setCount9(maxs[8]);
		maxCountRecord.setCount10(maxs[9]);
		maxCountRecord.setCount11(maxs[10]);
		maxCountRecord.setCount12(maxs[11]);
		maxCountRecordRepository.save(maxCountRecord);
	}
}
