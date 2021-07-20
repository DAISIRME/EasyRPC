package customer;

import Annotation.ERConsumer;
import netty.NettyClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

@Component
public class CustomerService implements ApplicationContextAware, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public Object createProxy(Class<?> interfaceType){
        return Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, new RpcProxy());
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ERConsumer.class);
        beansWithAnnotation.forEach((name,bean)->{
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getName().equals(name)) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(bean, createProxy(declaredField.getType()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
