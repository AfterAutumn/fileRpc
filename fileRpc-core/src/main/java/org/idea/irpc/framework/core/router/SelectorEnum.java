package org.idea.irpc.framework.core.router;

/**
 * @Author jiangshang
 * @Date created in 8:24 下午 2023/1/5
 */
public enum SelectorEnum {

    RANDOM_SELECTOR(0,"random");

    int code;
    String desc;

    SelectorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
