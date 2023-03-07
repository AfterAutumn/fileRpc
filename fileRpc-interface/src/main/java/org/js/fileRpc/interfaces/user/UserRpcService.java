package org.js.fileRpc.interfaces.user;

import java.util.List;
import java.util.Map;

/**
 * @Author jiangshang
 * @Date created in 10:08 上午 2023/3/19
 */
public interface UserRpcService {

    String getUserId();

    List<Map<String, String>> findMyGoods(String userId);
}
