package com.xuan.service.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogWarnServiceImpl implements LogWarnService {

	private final static Logger logger = LoggerFactory.getLogger(LogWarnServiceImpl.class);

	@Override
	public void warn(String log) {
		logger.warn(log);
	}
}
