package customer;

import lombok.Data;
import provider.RpcResponse;

@Data
public class RpcRequest {

    private String methodName;

    private String className;

    private String args;

    private RpcResponse response;
}
