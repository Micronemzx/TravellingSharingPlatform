package com.example.sharingplatform.utils;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(){
    }

    public Result(T data){
        this.data =data;
    }

    public static Result success(int code,String msg ){
        Result result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>(data);
        result.setCode(0);
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success(T data,String msg){
        Result<T> result = new Result<>(data);
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data,int code,String msg){
        Result<T> result = new Result<>(data);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }



    public static Result error(int code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static <T> Result<T> error(T data,int code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
