package org.idea.irpc.framework.core.proxy.jdk;


import org.idea.irpc.framework.core.client.RpcReferenceWrapper;
import org.idea.irpc.framework.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @Author jiangshang
 * @Date created in 8:56 上午 2021/11/30
 */
public class JDKProxyFactory implements ProxyFactory {

    public JDKProxyFactory() {
    }

    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) Proxy.newProxyInstance(rpcReferenceWrapper.getAimClass().getClassLoader(), new Class[]{rpcReferenceWrapper.getAimClass()},
                new JDKClientInvocationHandler(rpcReferenceWrapper));
    }
}
