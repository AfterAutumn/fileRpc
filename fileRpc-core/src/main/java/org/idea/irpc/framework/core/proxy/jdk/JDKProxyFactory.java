package org.idea.irpc.framework.core.proxy.jdk;


import org.idea.irpc.framework.core.client.RpcReferenceWrapper;
import org.idea.irpc.framework.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 * @Author jiangshang
 */
public class JDKProxyFactory implements ProxyFactory {

    public JDKProxyFactory() {
    }

    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) Proxy.newProxyInstance(rpcReferenceWrapper.getAimClass().getClassLoader(), new Class[]{rpcReferenceWrapper.getAimClass()},
                new JDKProxyHandler(rpcReferenceWrapper));
    }
}
