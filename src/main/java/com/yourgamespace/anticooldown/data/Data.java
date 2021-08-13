package com.yourgamespace.anticooldown.data;

@SuppressWarnings("ALL")
public class Data {

    public Data() {}

    private final Integer currentConfigVersion = 9;
    private boolean protocollib = false;

    public int getCurrentConfigVersion() {
        return currentConfigVersion;
    }

    public boolean isProtocollibInstalled() {
        return protocollib;
    }

    public void setProtocollib(boolean protocollib) {
        this.protocollib = protocollib;
    }
}
