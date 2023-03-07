package org.idea.irpc.framework.core.server;

import org.js.fileRpc.interfaces.UserService;

/**
 * @Author jiangshang
 * @Date created in 7:45 下午 2023/1/8
 */
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("test");
    }
}
