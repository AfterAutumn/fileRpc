package org.idea.irpc.framework.core.filter;

import org.idea.irpc.framework.core.protocol.RpcInvocation;


/**
 * 服务端过滤器
 *
 * @Author jiangshang
 * @Date created in 7:57 下午 2023/1/29
 */
public interface IServerFilter extends IFilter {

    /**
     * 执行核心过滤逻辑
     *
     * @param rpcInvocation
     */
    void doFilter(RpcInvocation rpcInvocation);
}
