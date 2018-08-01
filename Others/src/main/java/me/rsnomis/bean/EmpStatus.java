package me.rsnomis.bean;

public enum EmpStatus {
    LOGIN(100, "login"), LOGOUT(200,"logout"), REMOVE(300,"unreachable");

    private Integer code;
    private String msg;
    private EmpStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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

    public static EmpStatus getEmpStatusByCode(Integer code) {
        switch (code) {
            case 100:
                return LOGIN;
            case 200:
                return LOGOUT;
            case 300:
                return REMOVE;
            default:
                return LOGIN;
        }
    }
}
