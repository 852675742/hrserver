package org.sang;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.sang.config.RedisLockTool;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by Rick on 2019/3/13.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HrserverApplication.class)
public class RedisLockTest {

    public static final int RANDOM_NUMBER_RANGE = 3;

    @Test
    public void test1( ) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        IntStream.range(0,1).forEach(idx->{
            Thread thread = new Thread(new Tester(), "Thread-" + idx);
            latch.countDown();
            thread.start();
        });
        latch.await();
        System.out.println("down");
        Thread.sleep(Integer.MAX_VALUE);
    }

    class Tester implements Runnable {

        @Override
        public void run() {
            boolean result = RedisLockTool.tryGetDistributedLock();
            if(result) {
                System.out.println(Thread.currentThread().getName() + "： 获取到锁");
            } else {
                System.err.println(Thread.currentThread().getName() + "： 获取锁超时");
            }

            if (result) {
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(RANDOM_NUMBER_RANGE));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RedisLockTool.releaseDistributedLock();
                System.out.println(Thread.currentThread().getName() + "： 释放锁");
            }
        }
    }

}
