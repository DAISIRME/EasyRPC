package provider;

import Annotation.ERProvider;
import ZooKeeper.ZkApi;
import netty.NettyServer;
import netty.NettyServerHandler;
import org.apache.zookeeper.client.ZKClientConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class ProviderService implements ApplicationContextAware, InitializingBean {

    @Autowired
    NettyServer nettyServer;

    private static final String localIp = "127.0.0.1";

    private static final Integer host = 8181;

    @Autowired
    NettyServerHandler nettyServerHandler;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ERProvider.class);
        beansWithAnnotation.values().forEach(bean ->{
            ERProvider annotation = bean.getClass().getAnnotation(ERProvider.class);
            String interfaceName = annotation.val();
            nettyServerHandler.add(interfaceName, bean);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        nettyServer.startServer0(localIp, host, nettyServerHandler);
    }
}
