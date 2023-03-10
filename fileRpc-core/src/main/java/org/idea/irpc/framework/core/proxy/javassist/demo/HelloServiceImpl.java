package org.idea.irpc.framework.core.proxy.javassist.demo;

/**
 * @Author jiangshang
 * @Date created in 5:42 下午 2021/12/4
 */
public class HelloServiceImpl implements HelloService{

    @Override
    public void say(String msg) {
        System.out.println("this is say");
    }

    @Override
    public String echo(String msg) {
        System.out.println("this is echo");
        return msg;
    }

    @Override
    public String[] getHobbies() {
        System.out.println("this is getHobbies");
        return new String[0];
    }
}
