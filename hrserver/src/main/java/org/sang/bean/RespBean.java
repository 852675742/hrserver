package org.sang.bean;

/**
 * Created by sang on 2017/12/29.
 */
public class RespBean {
    private String status;
    private String msg;

    private Object data;

    public RespBean() {
    }

    public RespBean(String status, String msg) {

        this.status = status;
        this.msg = msg;
    }

    public RespBean(String status, Object data) {

        this.status = status;
        this.data = data;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
