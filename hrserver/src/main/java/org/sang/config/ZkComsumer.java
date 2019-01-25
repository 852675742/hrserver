package org.sang.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.sang.zk.ConsistentHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ZkComsumer implements Watcher {

    //本地缓存服务列表
    public static Map<String, List<String>> servermap;

    @Autowired
    private CuratorFramework curatorClient;

    private final String rootPath = "/services";

    private ConsistentHash consistentHash;

    /**
     * 获取服务节点数据
     * @param serverName
     * @return
     * @throws Exception
     */
    private List<String> getNodeList(String serverName) throws Exception {
        if (servermap == null) {
            servermap = new ConcurrentHashMap<String, List<String>>();
        }
        if (!Optional.ofNullable(curatorClient.checkExists().forPath(rootPath + "/" + serverName)).isPresent()) {
            log.error("不存在节点数据:{}",serverName);
            return null;
        }
        List<String> serverList = servermap.get(serverName);
        if (serverList != null && serverList.size() > 0) {//直接从缓存中取
            consistentHash = new ConsistentHash(serverList.size(),serverList);
            return serverList;
        }

        log.debug("获取子节点:{}",rootPath + "/" + serverName);
        List<String> children = curatorClient.getChildren().forPath(rootPath + "/" + serverName);//获取子节点
        List<String> list = new ArrayList<>();
        children.stream().forEach(r->{
            System.out.println("当前遍历的子节点" + r);
            byte[] data = null;
            try {
                data = curatorClient.getData().forPath(rootPath + "/" + serverName + "/" + r);
            } catch (Exception e) {
                log.error("获取节点数据失败",e);
            }
            String nodeData = new String(data);
            JSONObject nodeJson = (JSONObject)JSONObject.parse(new String(data));
            if(!"stop".equals(nodeJson.getString("status"))) {
                list.add(nodeData);
            }
        });
        consistentHash = new ConsistentHash(list.size(),list);
        servermap.put(serverName, list);
        return list;
    }

    /**
     * 获取服务数据
     * @param serverName
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws IOException
     */
    public String getServerinfo(String serverName) throws Exception {
            List<String> nodeList = getNodeList(serverName);//获取服务的节点数据
            if (CollectionUtils.isEmpty(nodeList)) {
                return null;
            }

            //String snode = nodeList.get((int) (Math.random() * nodeList.size()));//这里使用得随机负载策略，如需需要自己可以实现其他得负载策略
            String snode = (String)consistentHash.get(nodeList.get(0));//从一致性Hash环的第一个服务开始查找
            JSONObject nodeJson = (JSONObject)JSONObject.parse(snode);
            List<String> children = curatorClient.getChildren().forPath(rootPath + "/" + serverName);
            //随机负载后,将随机取得节点后的状态更新为run
            for (String s : children) {
                byte[] data = curatorClient.getData().forPath(rootPath + "/" + serverName + "/" + s);
                String datas = new String(data);
                if (snode.equals(datas)) {
                    //把随机取到的节点数据设置为run
                    nodeJson.put("status","run");
                    curatorClient.setData().forPath(rootPath + "/" + serverName + "/" + s,JSONObject.toJSONString(nodeJson).getBytes());
                    break;
                }
            }
            return JSONObject.toJSONString(nodeJson);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //如果服务节点数据发生变化则清空本地缓存
        log.debug("进入zk监控");
        if (watchedEvent.getType().equals(Event.EventType.NodeChildrenChanged)) {
            log.info("服务节点数据发生改变，清空缓存");
            servermap = null;
        }
    }
}