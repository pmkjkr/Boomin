package xyz.dongaboomin.etc.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xyz.dongaboomin.etc.env.Env;

public class JedisUse {

    private JedisPool pool;

    public JedisUse(){
        Env env = new Env();
        String redisHost = env.getENV().getOrDefault("REDIS.HOST","localhost");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        this.pool = new JedisPool(jedisPoolConfig,redisHost);
    }

    public String getContent(String key) {
        JedisPool readPool = pool;
        Jedis jedis = null;

        try {
            jedis = readPool.getResource();
            jedis.auth("Q!2dltnals");
            return jedis.get(key);
        } catch (Exception ex) {
            throw ex;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public boolean setContent(String key,String value){
        JedisPool readPool = pool;
        Jedis jedis = null;
        try {
            jedis = readPool.getResource();
            jedis.auth("Q!2dltnals");
            jedis.set(key, value);
            return true;
        } catch (Exception ex) {
            return false;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
