package cn.dooling.samlazure.domain.dto;

import java.io.Serializable;

public class ResponseDTO<T> implements Serializable {
    protected Integer code;

    protected String msg;

    protected Boolean success;

    protected T data;

    public ResponseDTO() {
    }

    public ResponseDTO(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ResponseDTO(Integer code, String msg, Boolean success, T data) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public ResponseDTO(String msg, Boolean success) {
        this.msg = msg;
        this.success = success;
    }

    public ResponseDTO(Boolean success) {
        this.success = success;
    }

    public ResponseDTO(Integer code, Boolean success) {
        this.code = code;
        this.success = success;
    }

    public ResponseDTO(Integer code) {
        this.code = code;
    }

    public ResponseDTO(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
