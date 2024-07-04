package com.yxj.mod.util;


import com.yxj.mod.config.RedisConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 获取连接池对象
 */
public enum RedisUtils {
    INSTANCE;
    static JedisPool jedisPool = null;

//    static {
//        //1 创建连接池配置对象
//        JedisPoolConfig config = new JedisPoolConfig();
//        //2 进行配置-四个配置
//        config.setMaxIdle(1);//最小连接数
//        config.setMaxTotal(11);//最大连接数
//        config.setMaxWaitMillis(10 * 1000L);//最长等待时间
//        config.setTestOnBorrow(true);//测试连接时是否畅通
//        //3 通过配置对象创建连接池对象
//        Properties properties = null;
//        try {
//            properties = new Properties();
//            properties.load(RedisUtils.class.getClassLoader().getResourceAsStream("redis.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String host = properties.getProperty("redis.host");
//        String port = properties.getProperty("redis.port");
//        String password = properties.getProperty("redis.password");
//        String timeout = properties.getProperty("redis.timeout");
//        System.out.println(host);
//        System.out.println(port);
//        System.out.println(password);
//        System.out.println(timeout);
//        jedisPool = new JedisPool(config, host, Integer.valueOf(port),Integer.valueOf(timeout), password);
//    }
static {
    try {
        ApplicationContext context = new AnnotationConfigApplicationContext(RedisConfig.class);
        RedisConfig redisConfig = context.getBean(RedisConfig.class);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(128);
        config.setMaxIdle(16);
        config.setMinIdle(8);
        config.setTestOnBorrow(true);

        // 使用从 RedisConfig 读取的配置来初始化 JedisPool
        //jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword());

    } catch (Exception e) {
        System.err.println("初始化 Redis 连接池失败: " + e.getMessage());
        e.printStackTrace();
        throw new ExceptionInInitializerError(e);
    }
}

    //获取连接
    public Jedis getSource() {
        return jedisPool.getResource();
    }

    //关闭资源
    public void closeSource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }

    }
    /**
     * 设置字符值
     *
     * @param key
     * @param
     */
    public void del(String key) {
        Jedis jedis = getSource();
        jedis.del(key);
        closeSource(jedis);
    }
    /**
     * 设置字符值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = getSource();
        jedis.set(key, value);
        closeSource(jedis);
    }

    /**
     * 设置
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getSource();
        jedis.set(key, value);
        closeSource(jedis);
    }

    /**
     *
     * @param key
     * @return
     */
    public byte[]  get(byte[] key) {
        Jedis jedis = getSource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSource(jedis);
        }
        return null;

    }

    /**
     * 设置字符值
     *
     * @param key
     */
    public String get(String key) {
        Jedis jedis = getSource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSource(jedis);
        }

        return null;

    }

    public void set(String key, String value, Integer time) {
        Jedis jedis = getSource();
        jedis.setex(key,time,value);
        closeSource(jedis);
    }
}
