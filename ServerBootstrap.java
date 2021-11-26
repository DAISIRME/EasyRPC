package provider;


import ZooKeeper.ZkApi;
import org.apache.zookeeper.KeeperException;

import java.util.HashMap;

// ServerBootstrap 会启动一个服务提供者，就是 NettyServer
public class ServerBootstrap  {
    private static HashMap hashMap;
    private static String hostName = "127.0.0.1";
    private static String port = "7000";
    private static String packageName = "/publicinterface.HelloService3/334";
    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZkApi zkApi = new ZkApi();
        boolean node1 = zkApi.createNode(packageName, "1");
        System.out.println(node1);
        boolean node = zkApi.createNode(packageName, "1");
        System.out.println(node);
        System.out.println(zkApi.getChildren("/publicinterface.HelloService3"));
        zkApi.deleteNode(packageName);
        System.out.println(zkApi.getChildren("/publicinterface.HelloService3"));
    }
}
