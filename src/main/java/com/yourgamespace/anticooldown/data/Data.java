package com.yourgamespace.anticooldown.data;

@SuppressWarnings("FieldCanBeLocal")
public class Data {

    private final Integer currentConfigVersion = 18;
    private boolean protocolLib = true;
    private boolean placeholderApi = true;
    public Data() {
    }

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
