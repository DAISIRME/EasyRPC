package netty;

import ZooKeeper.ZkApi;
import com.alibaba.fastjson.JSONArray;
import customer.ClientBootstrap;
import customer.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import provider.HelloServiceImpl;
import provider.RpcResponse;

import java.lang.reflect.Method;
import java.util.*;

// Server Handler 入站时处理 调用的具体逻辑
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static List<String> interfaceList;

    private final String serverIp = "127.0.0.1";

    public  static final String DATA_PATH = "easyRpc/data/";

    public Map<String,Object> BeanMap = new HashMap<>();

    @Autowired
    ZkApi zkApi;

    public void add(String interfaceName, Object objectbean){
        exposeService(interfaceName, serverIp);
        BeanMap.put(interfaceName,objectbean);
    }

    public void exposeService(String serviceName, String ipaddress)
    {
        String data = zkApi.getData(DATA_PATH + serviceName, null);
        JSONArray serveripList = null;
        if(Objects.nonNull(data)){
            serveripList = JSONArray.parseArray(data);
            serveripList.add(ipaddress);}
        else{
            serveripList = new JSONArray();
            serveripList.add(ipaddress);
        }
        zkApi.updateNode(DATA_PATH + serviceName, serveripList.toJSONString());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        String className = rpcRequest.getClassName();
        String methodName = rpcRequest.getMethodName();
        String args = rpcRequest.getArgs();
        Object o = BeanMap.get(className);
        Method method = o.getClass().getMethod(methodName, Class.forName(args));
        Object invoked = method.invoke(o, args);
        RpcResponse response = new RpcResponse();
        response.setResult(invoked);
        ctx.channel().writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
