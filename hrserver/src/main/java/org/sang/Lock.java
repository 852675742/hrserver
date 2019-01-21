package org.sang;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.apache.curator.framework.CuratorFramework;  
import org.apache.curator.framework.CuratorFrameworkFactory;  
import org.apache.curator.framework.recipes.locks.InterProcessMutex;  
import org.apache.curator.retry.RetryOneTime;  
  
public class Lock {  
  
    private static ExecutorService service;  
  
    static final CuratorFramework curator;  
    //A re-entrant mutex  
    static final InterProcessMutex zkMutex;  
    static {  
//        curator = CuratorFrameworkFactory.newClient("server1:2182,server2:2181,server3:2181", new RetryOneTime(2000));  
        curator = CuratorFrameworkFactory.newClient("localhost:2181", new RetryOneTime(2000));
        curator.start();
        try {
            if (!Optional.ofNullable(curator.checkExists().forPath("/mutex")).isPresent()) {
                curator.create().forPath("/mutex");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        zkMutex = new InterProcessMutex(curator,"/mutex");  
    }  
      
      
    public static void count(int threadNum,final int workers) throws Exception{  
        final CountDownLatch latch = new CountDownLatch(threadNum);  
        service = Executors.newFixedThreadPool(threadNum);  
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; ++i) {
            service.execute(()->{
                IntStream.range(0,workers).forEach(idx->{
                    try {
                        zkMutex.acquire();
                        System.out.println("获取到锁:" + idx);
                        Thread.currentThread().sleep(10000);//模拟业务执行时间
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            zkMutex.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                latch.countDown();
            });
        }  
        service.shutdown();  
        latch.await();  
        long end=System.currentTimeMillis();  
        System.out.println("Thread Num:"+threadNum+" workers per Thread:"+workers+" cost time:"+(end-start) +" avg "+ (threadNum*workers)*1000/(end-start));  
    }  
      
    public static void main(String[] args) throws Exception {  
        Lock.count(1, 20);
        /*
        Lock.count(10, 500);  
        Lock.count(20, 500);  
        Lock.count(30, 500);  
        Lock.count(40, 500);  
        Lock.count(50, 500);  
        Lock.count(60, 500);  
        Lock.count(70, 500);  
        Lock.count(80, 500);  
        Lock.count(90, 500);  
        Lock.count(100, 500);
        */
    }  
}  