package org.idea.irpc.framework.core.spi.jdk;

/**
 * @Author jiangshang
 * @Date created in 2:49 下午 2023/2/4
 */
public class DefaultISpiTest implements ISpiTest{

    @Override
    public void doTest() {
        System.out.println("执行测试方法");
    }

}
