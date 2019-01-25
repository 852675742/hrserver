package org.sang.zk;

/**
 * 一致性哈希接口
 */
public interface HashFunc {
    public Long hash(Object key);
}