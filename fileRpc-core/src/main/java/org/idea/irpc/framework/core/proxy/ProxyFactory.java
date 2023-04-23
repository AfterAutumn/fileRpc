package org.idea.irpc.framework.core.proxy;


import org.idea.irpc.framework.core.client.RpcReferenceWrapper;

/**
 * 动态代理机制
 */
public interface ProxyFactory {

    <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;
}