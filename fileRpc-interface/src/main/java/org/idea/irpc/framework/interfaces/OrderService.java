package org.idea.irpc.framework.interfaces;

import java.util.List;

/**
 * @Author jiangshang
 * @Date created in 6:41 下午 2023/3/8
 */
public interface OrderService {

    List<String> getOrderNoList();

    String testMaxData(int i);
}
