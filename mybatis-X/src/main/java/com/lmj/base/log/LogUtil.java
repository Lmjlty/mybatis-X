package com.lmj.base.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日志util
 * @author lmj
 *
 */
public class LogUtil {
    
    private Logger logger;
    
    private LogUtil() {}
    
    
    public static LogUtil getLog(Class<?> clazz) {
        LogUtil log=new LogUtil();
        log.logger=LoggerFactory.getLogger(clazz);
        return log;
    }
    
    public void info(String msg,Object... values) {
        logger.info(msg, values);
    }
    
    public void error(String msg,Object...error) {
        logger.error(msg, error);
    }
    
    public void info(Object object) {
    	logger.info(object.toString());
    }
}
