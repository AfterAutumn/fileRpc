package org.idea.irpc.framework.core.common.event;

/**
 * 服务销毁事件
 *
 * @Author jiangshang
 * @Date created in 3:20 下午 2023/1/8
 */
public class IRpcDestroyEvent implements IRpcEvent{

    private Object data;

    public IRpcDestroyEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public IRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
