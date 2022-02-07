package com.github.elrol.elrolsutilities.api.data;

import java.util.UUID;

public interface IPlayerDatabase {

    boolean isPresent(UUID uuid);

    IPlayerData get(UUID uuid);

    void save(UUID uuid);

    void save(IPlayerData data);
}
