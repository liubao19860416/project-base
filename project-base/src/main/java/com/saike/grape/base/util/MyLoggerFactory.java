package com.saike.grape.base.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.saike.grape.base.BaseEnvironment;

/**
 * LoggerFactory 日志工厂工具类
 * 
 */
public class MyLoggerFactory {

    private static final Logger logger =LoggerFactory.getLogger( MyLoggerFactory.class );

    private static final String LOGBACK_FILE_NAME = "logback.xml";
    
    //@SuppressWarnings("unused")
    private static final String LOGBACK_FILE_PATH ;
    
    static{
        //初始化静态常量（构造方法或者静态代码块）
        LOGBACK_FILE_PATH=BaseEnvironment.LOG_ENV + File.separator + LOGBACK_FILE_NAME;
    }
    
    // 清空LogContext日志缓存
    public static void resetLogContext() {
        LoggerContext context =(LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            
            configurator.setContext(context);

            //String path = BaseEnvironment.LOG_ENV + File.separator + LOGBACK_FILE_NAME;
            //logger.info( "reset log context with file: " + path );
            logger.info( "reset log context with file: " + LOGBACK_FILE_PATH );
            
            // 最终的清空方法
            context.reset();
            
            //加载当前用户的logback日志文件
            configurator.doConfigure( Thread.currentThread().getContextClassLoader()
                                    .getResourceAsStream( LOGBACK_FILE_PATH ) );
            
            logger.info( "reset log context completed!" );

        } catch ( JoranException ex ) {
            logger.error( "加载当前用户的logback日志文件失败！！！", ex );
            throw new RuntimeException( ex );
        }

        // TODO 什么意思?
        if ( logger.isInfoEnabled() ) {
            StatusPrinter.printInCaseOfErrorsOrWarnings( context );
        }
    }
    
    //测试
    public static void main(String[] args) {
        System.out.println(File.separator);//"\"
        System.out.println(File.pathSeparator);//";"
    }

}
