package com.xuan.service.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

	private final static Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
	
	@Autowired
	private LogWarnService logWarnService;

	@Override
	public void info(String log) {
		logger.info(log);
	}

	@Override
	public void warn(String log) {
		logWarnService.warn(log);
	}
}
