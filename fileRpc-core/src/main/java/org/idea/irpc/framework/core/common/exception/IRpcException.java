package org.idea.irpc.framework.core.common.exception;

import org.idea.irpc.framework.core.common.RpcInvocation;

/**
 * @Author jiangshang
 * @Date created in 9:04 上午 2023/3/3
 */
public class IRpcException extends RuntimeException {

    private RpcInvocation rpcInvocation;

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }

    public void setRpcInvocation(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

    public IRpcException(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

}
