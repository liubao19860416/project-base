package com.saike.grape.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meidusa.fastjson.JSONArray;
import com.saike.grape.base.util.MyLoggerFactory;

/**
 * 基本环境加载配置类
 */
public final class BaseEnvironment {

    private static final Logger logger = LoggerFactory.getLogger(BaseEnvironment.class);

    //最终需要给外部或其他位置访问的一些属性常量值
    public static final String SYS_ENV;                //基本环境常量（被外部访问，所以为public）
    public static final String LOG_ENV;                //基本log日志文件环境（被外部访问，所以为public）
    public static final String PROJECT_NAME;           //当前工程名称常量（被外部访问，所以为public）
   
    //默认的配置的一些文件名，对应加载各自的环境属性的配置文件
    private static final String PROJECT_SYS_ENV = "PROJECT_SYS_ENV";          //当前系统环境常量配置文件名称(该文件可以不存在，则以SIT为主)
    private static final String PROJECT_LOG_ENV = "PROJECT_LOG_ENV";          //当前系统日志环境常量配置文件名称(该文件可以不存在，则以默认值为主；否则和系统的一样)
    private static final String PROJECT_NAME_ENV = "PROJECT_NAME_ENV";      //当前系统工程名称常量配置文件名称(该文件可以不存在，则以默认值为主，当前为默认值)
    
    //上面的默认文件中的一些默认值
    private static final String DEFAULT_SYS_ENV = "SIT";                                    //默认的系统环境常量为SIT（固定）
    private static final String DEFAULT_IP_ENV_VALUE = "IP";                                //默认的IP环境配置名称常量（固定）
    private static final String DEFAULT_PROJECT_NAME = "project-base";                      //默认的系统工程名称常量（固定）
    private static final String DEFAULT_PROJECT_IP_ENV_NAME = "PROJECT_IP_ENV.properties";  //当前默认的IP环境配置文件名称（固定）

    // BaseEnvironment should not be newed
    private BaseEnvironment() {
    }
    
    //获取动态系统配置环境变量，默认为SIT
    static {
        String config = retrieveEnvValue(PROJECT_SYS_ENV, DEFAULT_SYS_ENV);

        // 1.读取对应的系统环境变量信息
        if (StringUtils.equals(DEFAULT_IP_ENV_VALUE, config)) {
            // 根据配置的IP信息，动态进行统一配置文件和log文件的加载；
            List<String> ips = getLocalHostIP();
            // 根据当前主机的IP信息，动态获取对应的环境配置信息
            SYS_ENV = getEnvNameByIps(ips);
        } else {
            // 以读取的config配置进行加载对应的配置信息，如PRE,SIT,DEV等；
            SYS_ENV = config;
        }
        logger.info("最终加载的系统环境变量BaseEnvironment.SYS_ENV = " + SYS_ENV);

        //2.读取对应过的log配置文件环境信息
        // LOG_ENV = retrieveEnvValue(GRAPE_LOG_ENV, SYS_ENV);
        config = retrieveEnvValue(PROJECT_LOG_ENV, SYS_ENV);
        if (StringUtils.equals(DEFAULT_IP_ENV_VALUE, config)) {
            // 根据配置的IP信息，动态进行统一配置文件和log文件的加载；
            List<String> ips = getLocalHostIP();
            // 根据当前主机的IP信息，动态获取对应的环境配置信息
            LOG_ENV = getEnvNameByIps(ips);
        } else {
            // 以读取的config配置进行加载对应的配置信息，如PRE,SIT,DEV等；
            LOG_ENV = config;
        }
        logger.info("最终加载的系统日志环境变量BaseEnvironment.LOG_ENV = " + LOG_ENV);

        //3.读取对应过的工程名称配置文件环境信息
        PROJECT_NAME = retrieveEnvValue(PROJECT_NAME_ENV,DEFAULT_PROJECT_NAME);
        logger.info("最终加载的BaseEnvironment.PROJECT_NAME = " + PROJECT_NAME);

        // reset the log context according to the BaseEnvironment.LOG_ENV
        MyLoggerFactory.resetLogContext();
        
        //输出当前的配置信息
        logger.info("SYS_ENV=====>"+SYS_ENV+",LOG_ENV=====>"+LOG_ENV+",PROJECT_NAME========>"+PROJECT_NAME);

    }

    //通用的读取配置信息的方法
    private static String retrieveEnvValue(String name, String defaultValue) {
        String env = null;
        // 1st: check in jvm app arguments
        env = System.getProperty(name);
        // 2nd: check in environment variables
        if (env == null || "".equals(env)) {
            env = System.getenv().get(name);
        }
        
        // 3rd: check in the according file
        if (env == null || "".equals(env)) {
            InputStream ii = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            if (ii != null) {
                InputStreamReader r = new InputStreamReader(ii);
                // limited to 100 chars is enough
                char[] cbuf = new char[100]; 
                try {
                    int n = r.read(cbuf);
                    if (n > 0) {
                        env = new String(cbuf, 0, n).trim();
                    }
                } catch (Exception ex) {
                    // should not go here
                    logger.info("BaseEnvironment.retrieveEnvValue  method exception。。。", ex);
                } finally {
                    try {
                        if(ii!=null){
                            ii.close();
                        }
                    } catch (Exception ex) {
                        logger.error("释放资源异常！",ex);
                    }finally{
                        ii=null;
                    }
                }
            }
        }

        //添加对获取工程项目根路径名称的代码；
        if(env==null&&PROJECT_NAME_ENV.equalsIgnoreCase(name)){
            //基本不用
            Map<String, String> envs = System.getenv();
            for (String key1 : envs.keySet()) {
                String value1 = envs.get(key1);
                //System.out.println(key1+"====>"+value1);
            }
            //System.out.println("===============================");
            Properties p = System.getProperties();
            Enumeration<Object> e = p.elements();
            while(e.hasMoreElements()){
                String key = (String) e.nextElement();  
                //System.out.println(key+"====>"+p.getProperty(key));
            }
            
            //这样获取的是D:\eclipse-20141015
            String userDir = System.getProperty("user.dir");
            logger.info("从System中读取获得 userDir =" + userDir);
            //这样获取的是/D:/eclipse-20141015/apache-tomcat-7.0.47/wtpwebapps/personal-web-project/WEB-INF/classes/
            userDir=BaseEnvironment.class.getResource("/").getPath();
            logger.info("从getResource(\"/\").getPath()中读取获得 userDir =" + userDir);
            if(userDir!=null){
                //userDir=userDir.substring(0, userDir.indexOf("/WEB-INF/classes"));
                //env=userDir.substring(userDir.lastIndexOf("\\"));
                //env=userDir.substring(userDir.lastIndexOf("/"));
                //添加多次判断，对于linux和windows都适用
                String fileSeparator = System.getProperty("file.separator");
                if(fileSeparator==null){
                    fileSeparator=File.separator;
                    logger.info("【System.getProperty】从系统中获取文件分隔符为空！！！");
                }
                if(fileSeparator==null){
                    logger.info("【File.separator】从系统中获取文件分隔符为空！！！");
                    fileSeparator="/";
                }
                if(userDir.indexOf(fileSeparator)<0){
                    fileSeparator="\\";
                }
                //这一个不能少
                if(userDir.indexOf(fileSeparator)<0){
                    fileSeparator="/";
                }
                logger.info("当前系统中使用的文件分隔符为：【"+fileSeparator+"】");
                if(userDir.indexOf(fileSeparator+"WEB-INF"+fileSeparator+"classes")>0){
                    userDir=userDir.substring(0, userDir.indexOf(fileSeparator+"WEB-INF"+fileSeparator+"classes"));
                }
                if(userDir.lastIndexOf(fileSeparator)>=0){
                    env=userDir.substring(userDir.lastIndexOf(fileSeparator));
                }
                logger.info("项目名称获取方式：从getResource(\"/\").getPath()中截取的 项目名称信息 =" + env);
            }
        }else if(PROJECT_NAME_ENV.equalsIgnoreCase(name)){
            logger.info("项目名称获取方式：从配置文件中读取获得项目名称信息=" + env);
        }
        
        // 对应的默认值
        if (env == null || "".equals(env)) {
            env = defaultValue;
            logger.info("加载的是对应的【"+name+"】配置信息的默认值:" + env);
        }
        
        return env;
    }

    // 获取本机IP
    private static List<String> getLocalHostIP() {
        String osName = System.getProperty("os.name");
        logger.info("当前操作系统为:" + osName);
        List<String> ipList = new ArrayList<>();
        if (StringUtils.startsWith(osName, "Windows")) {
            //获取当前Windows系统的ip地址信息
            try {
                //多个IP可以获取吗？
                //ipList.add(getUnixLocalIp());
                ipList.add(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
                logger.info("您获取Windows系统的IP地址信息异常！！！",e);
            }
        } else {
            //获取当前Linux系统的ip地址信息
            try {
                ipList.addAll(getUnixLocalIp());
            } catch (SocketException e) {
                e.printStackTrace();
                logger.info("您获取Linux系统的IP地址信息异常！！！",e);
            }
        }
        logger.info("当前操作系统IP为:" + ipList);
        return ipList;
    }

    // 可能多个ip地址只获取其中一个ip地址 获取Linux 本地IP地址
    private static List<String> getUnixLocalIp() throws SocketException {
        List<String> res = new ArrayList<String>();
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> nii = ni.getInetAddresses();
                while (nii.hasMoreElements()) {
                    ip = nii.nextElement();
                    String hostAddress = ip.getHostAddress();
                    //说明这是获取的IPV4格式的ip地址信息
                    if (hostAddress!=null && hostAddress.indexOf(":") == -1) {
                        //过滤下面的两个本地主机IP地址
                        if ( !StringUtils.equals("127.0.0.1",hostAddress)
                                && !StringUtils.equals("192.168.0.1",hostAddress)) {
                            res.add(hostAddress);
                        }
                    }else{
                        logger.info("您获取的IP地址信息可能是IPV6格式的，不符合条件！！！"+hostAddress);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("您获取Linux系统的IP地址信息异常！！！",e);
        }
        return res;
    }

    //根据当前主机的IP信息，动态获取对应的环境配置信息
    private static String getEnvNameByIps(List<String> ipList) {
        logger.info("*****************************************\r\n");
        logger.info("入参ipList内容为:" + JSONArray.toJSONString(ipList));
        logger.info("*****************************************\r\n");
        String env = "";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\r\n");
        for (String ip : ipList) {
            String name = ip.trim();
            env = System.getProperty(name);
            if (env == null || "".equals(env)) {
                env = System.getenv().get(name);
            }
            //读取配置文件中的ip地址信息和对应的环境名称（key-value格式）
            if (env == null || "".equals(env)) {
                InputStream inStream = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(DEFAULT_PROJECT_IP_ENV_NAME);
                Properties p = new Properties();
                try {
                    p.load(inStream);
                    // 此处循环获取ip 配置文件里面的数据,一条条进行判断
                    for (Object key : p.keySet()) {
                        stringBuffer.append(key.toString() + "=======>");
                        stringBuffer.append(p.getProperty(key.toString()) + "\r\n");
                        //拆分当前的ip地址配置文件中的正则约束信息
                        String[] pats = StringUtils.split(key.toString(), ".");
                        //拆分当前主机真实的ip地址信息，通过正则匹配和匹配成功的个数进行判断
                        String[] ips = StringUtils.split(name, ".");
                        int count = 0;
                        for (int i = 0; i < pats.length; i++) {
                            //正则规则
                            String reg = pats[i];
                            reg = StringUtils.replace(reg, "*", "\\d*");
                            reg = StringUtils.replace(reg, "?", "\\d?");
                            reg = StringUtils.replace(reg, "+", "\\d+");
                            Pattern pat = Pattern.compile(reg);
                            //进行正则匹配
                            Matcher mat = pat.matcher(ips[i]);
                            boolean flag = mat.find();
                            if (flag) {
                                count++;
                            }
                        }
                        //只需要匹配符合4次即可跳出循环
                        if (count == 4) {
                            env = p.getProperty(key.toString());
                            stringBuffer.append("当前本机IP为:").append(ip)
                                    .append(",匹配的配置文件项为:").append(key.toString())
                                    .append(",环境为:").append(env);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("匹配IP地址信息失败！！！:",e);
                } finally {
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            logger.error("释放资源异常！",e);
                        }finally{
                            inStream=null;
                        }
                    }
                }
            }
        }
        logger.info("*****************************************\r\n");
        logger.info(stringBuffer.toString());
        logger.info("*****************************************\r\n");
        return env;
    }

}
