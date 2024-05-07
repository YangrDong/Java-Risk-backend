package com.example.vueadminjava.common.lang;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public void succ(T data) {
        this.setCode(200);
        this.setMsg("执行成功");
        this.setData(data);
    }
    public void succ(int code, String msg, T data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }
    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
