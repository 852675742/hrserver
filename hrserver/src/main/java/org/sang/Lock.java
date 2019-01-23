package org.sang;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.apache.curator.framework.CuratorFramework;  
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

public class Lock {  
  
    private static ExecutorService service;  
  
    static final CuratorFramework curator;  
    //A re-entrant mutex  
    static final InterProcessMutex zkMutex;  
    static {  
//        curator = CuratorFrameworkFactory.newClient("server1:2182,server2:2181,server3:2181", new RetryOneTime(2000));  
        //curator = CuratorFrameworkFactory.newClient("localhost:2181", new RetryOneTime(2000));
        //可以采用命名空间隔离
        curator = CuratorFrameworkFactory.builder().connectString("localhost:2181").sessionTimeoutMs(5000).connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(2000,3)).build();
        curator.start();
        try {
            if (!Optional.ofNullable(curator.checkExists().forPath("/mutex")).isPresent()) {
                curator.create().withMode(CreateMode.PERSISTENT).forPath("/mutex");
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
                        Thread.currentThread().sleep(2000);//模拟业务执行时间
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

    /**
     * 监听子节点的增删改
     * @throws Exception
     */
    public static void pathCache() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curator,"/example/pathCache",true);
        pathChildrenCache.start();
        PathChildrenCacheListener listener = (curator,event)->{
             System.out.println("事件类型:" + event.getType());
             Optional.ofNullable(event.getData()).ifPresent(r->{
                 System.out.println("节点数据xx:" + event.getData().getPath() + "=" + new String(event.getData().getData()));
             });
        };
        pathChildrenCache.getListenable().addListener(listener);

        curator.create().creatingParentsIfNeeded().forPath("/example/pathCache/test01","test01".getBytes());
        curator.create().creatingParentsIfNeeded().forPath("/example/pathCache/test02","test02".getBytes());
        curator.setData().forPath("/example/pathCache/test01", "01_V2".getBytes());
        Thread.sleep(1000);
        //遍历当前数据
        pathChildrenCache.getCurrentData().forEach(r->{
            System.out.println("节点数据yy:" + r.getPath() + "=" + new String(r.getData()));
        });
        curator.delete().forPath("/example/pathCache/test01");
        curator.delete().forPath("/example/pathCache/test02");
        
        pathChildrenCache.close();
    }
      
    public static void main(String[] args) throws Exception {  
        //Lock.count(1, 10);
        //curator.create().forPath("/test","init".getBytes());
        //curator.delete().guaranteed().forPath("/test");
        /*
        Stat stat = new Stat();
        String content = new String(curator.getData().storingStatIn(stat).forPath("/test"));
        System.out.println("节点内容:" + content);
        System.out.println("节点状态:" + stat.toString());
        curator.setData().forPath("/test","init2".getBytes());
        List<String> child = curator.getChildren().forPath("/test");
        */
        //事物，操作一连串事件
        /*报错
        curator.inTransaction().check().forPath("/test2")
                .and()
                .create().withMode(CreateMode.PERSISTENT).forPath("/test2","data".getBytes())
                .and()
                .setData().forPath("/test2","data2".getBytes())
                .and()
                .commit();
                */
        pathCache();
        curator.close();
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