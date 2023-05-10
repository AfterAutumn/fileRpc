package org.idea.irpc.framework.core.filter.client;

import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.protocol.RpcInvocation;
import org.idea.irpc.framework.core.filter.IClientFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jiangshang
 * @Date created in 8:18 下午 2023/1/29
 */
public class ClientFilterChain {

    private static List<IClientFilter> iClientFilterList = new ArrayList<>();

    public void addClientFilter(IClientFilter iClientFilter) {
        iClientFilterList.add(iClientFilter);
    }

    public void doFilter(List<ChannelFutureService> src, RpcInvocation rpcInvocation) {
        for (IClientFilter iClientFilter : iClientFilterList) {
            iClientFilter.doFilter(src, rpcInvocation);
        }
    }

}
