package org.idea.irpc.framework.core.tolerant;

import java.util.concurrent.TimeUnit;

/**
 * @Author jiangshang
 * @Date created in 4:04 下午 2023/3/1
 */
public abstract class CounterLimit {

    protected int limitCount;

    protected long limitTime;

    protected TimeUnit timeUnit;

    protected volatile boolean isLimited;

    protected abstract boolean tryCount();
}
