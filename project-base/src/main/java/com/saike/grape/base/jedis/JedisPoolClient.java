package com.saike.grape.base.jedis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.saike.grape.base.cons.InitSystemConstants;

/**
 * redis操作客户端
 */
public class JedisPoolClient {
    private static final Logger logger = LoggerFactory.getLogger(JedisPoolClient.class);
    private static Jedis jedis = null;
    private static final int MAX_WAIT_MILLIS = 20000;
    private static final int MAX_IDLE = 100;

    private static JedisPool jedisPool = null;

    static {
        init();
    }

    // jedispool初始化
    protected static synchronized boolean init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(MAX_WAIT_MILLIS);
        config.setMaxIdle(MAX_IDLE);
        jedisPool = new JedisPool(config, InitSystemConstants.REDIS_IP,
                Integer.valueOf(InitSystemConstants.REDIS_PORT));
        logger.info("REDIS_IP:"+InitSystemConstants.REDIS_IP+",REDIS_PORT:"+InitSystemConstants.REDIS_PORT
                +",MAX_WAIT_MILLIS:"+MAX_WAIT_MILLIS+",MAX_IDLE:"+MAX_IDLE+"。");
        return true;
    }

    // 创建jedis
    public static Jedis getInstance() {
        if (jedis == null) {
            jedis = jedisPool.getResource();
        }
        return jedis;
    }

    // 私有化构造函数使之不被new出来
    private JedisPoolClient() {

    }

    // /**
    // * @Description: 创建jedis
    // * @return Jedis
    // * @throws
    // */
    // protected synchronized Jedis createJedis() {
    // if (jedis == null) {
    // jedis = jedisPool.getResource();
    // }
    // return jedis;
    // }

    /**
     * @Description: 添加信息到redis
     * @param key:根据appCode+平台构成的key
     * @param userToken:用户token
     * @param entity:保存到redis的实体类
     */
    public static boolean setMap(String key, String userToken, String value) {
        try {
            logger.info("jedis set,key:" + key + ",userToken:" + userToken + ",entity:" + value);
            getInstance().hset(key, userToken, value);
            return true;
        } catch (Exception ex) {
            logger.error("jedis set error!!!,message:" + ex.getMessage());
        } finally {
             //jedisPool.returnResource(jedis);
        }
        return false;
    }

    // 设置属性和对应的值（用户信息）
    public static boolean setString(String key, String value) {
        try {
            getInstance().set(key, value);
        } catch (Exception ex) {
            logger.error("jedis set error!!!,message:" + ex.getMessage());
            return false;
        } finally {
            // jedisPool.returnResource(jedis);
        }
        return true;
    }

    //根据key获取redis上存储的所有用户信息
    public static Map<String, String> getMap(String key) {
        logger.info("get key is " + key);
        try {
            return getInstance().hgetAll(key);
        } catch (Exception ex) {
            logger.error("get method error!!!message:" + ex.getMessage());
            return new HashMap<String, String>();
        } finally {
            // jedisPool.returnResource(jedis);
        }
    }

    //根据key获取redis上存储的单个用户信息
    public static String getString(String key) {
        logger.info("get key is " + key);
        try {
            return getInstance().get(key);
        } catch (Exception ex) {
            logger.error("get method error!!!message:" + ex.getMessage());
            return null;
        } finally {
            // jedisPool.returnResource(jedis);
        }
    }

    // 根据key删除redis上存储的用户信息
    public static boolean delete(String key) {
        boolean flag = true;
        try {
            logger.info("delete from redis,key:" + key);
            long result = getInstance().del(key);
            if (result < 0) {
                flag = false;
                logger.info("delete failure！key:" + key);
            }
        } catch (Exception ex) {
            return false;
        } finally {
            // jedisPool.returnResource(jedis);
        }
        return flag;
    }

}
