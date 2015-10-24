package com.saike.grape.base.cons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.saike.grape.base.configure.CustomizedPropertyPlaceholderConfigurer;

/**
 * 读取加载统一配置等操作类
 */
public class InitSystemConstants {
    
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(InitSystemConstants.class);

    static {
        // 加载统一配置文件
        new ClassPathXmlApplicationContext("classpath:/application-base-project.xml");
    }
    
    //获取全局同意配置常量（测试的时候需要写死即可）
    public final static String URL = CustomizedPropertyPlaceholderConfigurer
            .getContextProperty("orm.mysql.jdbc.url");
    public final static String REDIS_IP = CustomizedPropertyPlaceholderConfigurer
            .getContextProperty("global.redis.scl.ip");
    public final static String REDIS_PORT = CustomizedPropertyPlaceholderConfigurer
            .getContextProperty("global.redis.scl.port");

    public final static String URL_TEST = "";
    //main测试方法
    public static void main(String args[]) {
        System.out.println(InitSystemConstants.URL_TEST);
    }

}
