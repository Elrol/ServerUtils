package dev.elrol.serverutilities.api.claims;

public interface IClaimSettingRegistry {

    void register(String name, dev.elrol.serverutilities.api.claims.IClaimSettingEntry setting);
    dev.elrol.serverutilities.api.claims.IClaimSettingEntry get(String name);

}
