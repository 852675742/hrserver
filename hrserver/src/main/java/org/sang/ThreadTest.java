package org.sang;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Rick on 2019/3/20.
 *
 * @ Description：线程测试
 */
public class ThreadTest {

    static Semaphore semaphore = new Semaphore(2);

    private static ExecutorService threadPool = Executors
            .newFixedThreadPool(20);

    public static void main(String[] args) {
        for (int i = 0; i < 20;i++) {
            threadPool.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(new Date() + ":" + Thread.currentThread().getName() + "开始执行");
                    Thread.sleep(3000);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        threadPool.shutdown();
    }

}
