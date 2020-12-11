package cn.dooling.samlazure.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
    boolean expire(String key, long time);

    long getExpire(String key);

    boolean hasKey(String key);

    void del(String... key);

    String get(String key);

    <T> T getObject(String key, Class<T> clazz);

    <T> List<T> getList(String key, Class<T> clz);

    boolean set(String key, String value);

    boolean set(String key, String value, long time);

    long incr(String key, long delta);

    long decr(String key, long delta);

    Object hget(String key, String item);

    Map<String, Object> hmget(String key);

    boolean hmset(String key, Map<String, Object> map);

    boolean hmset(String key, Map<String, Object> map, long time);

    boolean hset(String key, String item, Object value);

    boolean hset(String key, String item, Object value, long time);

    void hdel(String key, Object... item);

    boolean hHasKey(String key, String item);

    double hincr(String key, String item, double by);

    double hdecr(String key, String item, double by);

    Set<Object> sGet(String key);

    boolean sHasKey(String key, Object value);

    long sSet(String key, Object... values);

    long sSetAndTime(String key, long time, Object... values);

    long sGetSetSize(String key);

    long setRemove(String key, Object... values);

    List<Object> lGet(String key, long start, long end);

    List<Object> lGetAll(String key);

    long lGetListSize(String key);

    Object lGetIndex(String key, long index);

    boolean lSet(String key, Object value);

    boolean lSet(String key, Object value, long time);

    boolean lSet(String key, List<Object> value);

    boolean lSet(String key, List<Object> value, long time);

    boolean lUpdateIndex(String key, long index, Object value);

    long lRemove(String key, long count, Object value);
}
