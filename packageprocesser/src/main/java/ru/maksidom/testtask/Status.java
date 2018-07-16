package ru.maksidom.testtask;

public enum Status {
    SEND(0),
    IN_PROCESS(1),
    DONE(2);

    Status(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer getCode() {
        return code;
    }
}
