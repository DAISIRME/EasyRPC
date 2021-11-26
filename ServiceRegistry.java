package provider;

import ZooKeeper.ZkApi;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class ServiceRegistry {

    @Autowired
    ZkApi zkApi;

    public  static final String DATA_PATH = "easyRpc/data/";


    public void exposeService(String serviceName, String ipaddress)
    {
        String data = zkApi.getData(DATA_PATH + serviceName, null);
        JSONArray serveripList = null;
        if(Objects.nonNull(data)){
            serveripList = JSONArray.parseArray(data);
        }
        else{
            serveripList = new JSONArray();
        }
        serveripList.add(ipaddress);
        zkApi.updateNode(DATA_PATH + serviceName, serveripList.toJSONString());
    }

}
