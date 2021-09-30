package com.yourgamespace.anticooldown.data;

@SuppressWarnings("ALL")
public class Data {

    public Data() {}

    private final Integer currentConfigVersion = 9;
    private boolean protocolLib = true;
    private boolean placeholderApi = true;

    public int getCurrentConfigVersion() {
        return currentConfigVersion;
    }

    public boolean isProtocolLibInstalled() {
        return protocolLib;
    }

    public void setProtocolLib(boolean protocolLib) {
        this.protocolLib = protocolLib;
    }

    public boolean isPlaceholderApiInstalled() {
        return placeholderApi;
    }

    public void setPlaceholderApi(boolean placeholderApi) {
        this.placeholderApi = placeholderApi;
    }
}
