package dev.elrol.serverutilities.api.data;

import java.util.UUID;

public interface IPlayerDatabase {

    boolean isPresent(UUID uuid);

    IPlayerData get(UUID uuid);

    IPlayerData get(long id);

    void link(UUID uuid, long id);

    void save(UUID uuid);

    void save(IPlayerData data);
}
