package provider;

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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        String className = rpcRequest.getClassName();
        String methodName = rpcRequest.getMethodName();
        String args = rpcRequest.getArgs();
        Object o = ProviderService.getBean(className);
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
