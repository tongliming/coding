package juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description Coding
 * @Author TLM
 * @Date 2023/3/20 14:49
 * @Version 1.0
 */
public class AQSExample {
    @Test
    public void test() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
    }
}
