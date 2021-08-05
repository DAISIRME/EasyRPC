package customer;

import ZooKeeper.LRULoadBalance;
import ZooKeeper.ZkApi;
import common.AddressInfo;
import netty.NettyClient;
import netty.NettyClientHandler;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RpcProxy implements InvocationHandler {

    NettyClientHandler nettyClientHandler;

    @Autowired
    LRULoadBalance lruLoadBalance;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = createRequest(proxy, method, args);
        String addressInfo = lruLoadBalance.getData();
        NettyClient.initClient(addressInfo);
        return nettyClientHandler.call(rpcRequest);
    }

    private RpcRequest createRequest(Object proxy, Method method, Object[] args) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(proxy.getClass().getName());
        rpcRequest.setMethodName(method.getName());
        return rpcRequest;
    }
}
