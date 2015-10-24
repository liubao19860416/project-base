package com.saike.grape.common.test;
import org.junit.Before;
import org.junit.Test;
/**
 * 测试基本配置信息的类
 */
public class TestConfigure {
    
    private final String env = "LOCAL";
    private final String projectName = "project-base";

    @Before
    public void setUp() throws Exception {
        System.setProperty("GRAPE_SYS_ENV", env);
        System.setProperty("GRAPE_PROJECT_NAME", projectName);
    }
    
	@Test
	public void TestConfig(){
		String ss[]={"GrapeToBIosToken","GrapeToCIosToken","MongoToCIosToken","MongoToBIosToken","GrapeToBAndroidToken","GrapeToCAndroidToken","MongoToCAndroidToken","MongoToBAndroidToken"};
    	for(int i=0;i<ss.length;i++){
        	//new JedisPoolClient().delete(ss[i]);
    	}
		
	} 
}
