package org.idea.irpc.framework.core.common.exception;

import org.idea.irpc.framework.core.protocol.RpcInvocation;

/**
 * @Author jiangshang
 * @Date created in 9:53 下午 2023/3/5
 */
public class MaxServiceLimitRequestException extends IRpcException{

    public MaxServiceLimitRequestException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }
}
