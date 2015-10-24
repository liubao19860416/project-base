package com.saike.grape.base.configure;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.saike.grape.MyVenusPropertyPlaceholderConfigurer;

//import com.gagu.ucm.agent.venus.placeholder.VenusPropertyPlaceholderConfigurer;
/**
 * 获取全局统一配置的用户属性配置文件加载类，只能调用和获取静态方法和静态属性；
 */

public class CustomizedPropertyPlaceholderConfigurer extends MyVenusPropertyPlaceholderConfigurer
        /*VenusPropertyPlaceholderConfigurer*/ {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomizedPropertyPlaceholderConfigurer.class);

    //存储全局统一配置属性静态常量存储map
    public static Map<String, String> ctxPropertiesMap;

    //保护的方法，防止该类被人工创建
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
            Properties props) throws BeansException {
        
        super.processProperties(beanFactoryToProcess, props);
        
        ctxPropertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
            //System.out.println(key + "=========打印输出ctxPropertiesMap的值========》" + value);
            //logger.info(key + "========打印输出ctxPropertiesMap打的值=========》" + value);
        }
    }

    //静态方法，获取对应的属性值；
    public static String getContextProperty(String name) {
        logger.info(name + "=================》" + ctxPropertiesMap.get(name));
        return ctxPropertiesMap.get(name);
    }

}
