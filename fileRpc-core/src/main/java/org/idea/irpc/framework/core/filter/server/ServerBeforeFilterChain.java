package org.idea.irpc.framework.core.filter.server;

import org.idea.irpc.framework.core.common.RpcInvocation;
import org.idea.irpc.framework.core.filter.IServerFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jiangshang
 * @Date created in 8:40 下午 2023/1/29
 */
public class ServerBeforeFilterChain {

    private static List<IServerFilter> iServerFilters = new ArrayList<>();

    public void addServerFilter(IServerFilter iServerFilter) {
        iServerFilters.add(iServerFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (IServerFilter iServerFilter : iServerFilters) {
            iServerFilter.doFilter(rpcInvocation);
        }
    }
}
