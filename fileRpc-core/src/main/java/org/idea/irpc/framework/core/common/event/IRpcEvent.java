package org.idea.irpc.framework.core.common.event;

/**
 * @Author jiangshang
 * @Date created in 10:10 上午 2021/12/16
 */
public interface IRpcEvent {

    Object getData();

    IRpcEvent setData(Object data);
}
