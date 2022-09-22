package dev.elrol.serverutilities.api.data;

import java.util.UUID;

public interface IPlayerDatabase {

    boolean isPresent(UUID uuid);

    dev.elrol.serverutilities.api.data.IPlayerData get(UUID uuid);

    dev.elrol.serverutilities.api.data.IPlayerData get(long id);

    void link(UUID uuid, long id);

    void save(UUID uuid);

    void save(dev.elrol.serverutilities.api.data.IPlayerData data);
}
