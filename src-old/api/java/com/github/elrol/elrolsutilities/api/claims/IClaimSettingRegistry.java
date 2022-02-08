package com.github.elrol.elrolsutilities.api.claims;

public interface IClaimSettingRegistry {

    void register(String name, IClaimSettingEntry setting);
    IClaimSettingEntry get(String name);

}
