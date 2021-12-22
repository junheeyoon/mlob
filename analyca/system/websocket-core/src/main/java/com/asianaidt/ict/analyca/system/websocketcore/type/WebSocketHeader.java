package com.asianaidt.ict.analyca.system.websocketcore.type;

public enum WebSocketHeader {
    HOSTNAME("hostname"),
    IP("ip"),
    USER("user");

    private String name;

    WebSocketHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
