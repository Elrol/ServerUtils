package dev.elrol.serverutilities.api;

class ElrolAPIProxy {

    private static final ElrolAPIProxy proxy = new ElrolAPIProxy();

    private dev.elrol.serverutilities.api.IElrolAPI apiInstance;

    private ElrolAPIProxy() {}

    public static ElrolAPIProxy getProxy() {
        return proxy;
    }

    public dev.elrol.serverutilities.api.IElrolAPI getApi() {
        return apiInstance;
    }

    public void setApi(final dev.elrol.serverutilities.api.IElrolAPI api) {
        apiInstance = api;
    }

}
