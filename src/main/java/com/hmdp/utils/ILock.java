package com.hmdp.utils;

/**
 * @author caoshuai
 * @version 1.1
 */
public interface ILock {
    /*
     * 尝试获取锁
     */
    boolean tryLock(long timeoutSec);

    void unlock();
}
