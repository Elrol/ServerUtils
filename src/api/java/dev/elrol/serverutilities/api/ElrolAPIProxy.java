package dev.elrol.serverutilities.api;

class ElrolAPIProxy {

    private static final ElrolAPIProxy proxy = new ElrolAPIProxy();

    private IElrolAPI apiInstance;

    private ElrolAPIProxy() {}

    public static ElrolAPIProxy getProxy() {
        return proxy;
    }

    public IElrolAPI getApi() {
        return apiInstance;
    }

    public void setApi(final IElrolAPI api) {
        apiInstance = api;
    }

}
