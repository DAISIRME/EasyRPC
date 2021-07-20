package ZooKeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import common.AddressInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LRULoadBalance {

    @Autowired
    ZkApi zkApi;

    public String getData(){
        String data = zkApi.getData(ZK.DATA_PATH.name(), null);
        JSONArray serverIpList = JSONArray.parseArray(data);
        if(Objects.nonNull(serverIpList))
        {
            String serverIp = (String) serverIpList.get(0);
            serverIpList.remove(0);
            serverIpList.add(serverIp);
            zkApi.updateNode(ZK.DATA_PATH.name(), serverIpList.toJSONString());
            return serverIp;
        }
        return null;
    }

}
