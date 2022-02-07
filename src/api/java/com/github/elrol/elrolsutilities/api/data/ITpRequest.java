package com.github.elrol.elrolsutilities.api.data;

import java.util.UUID;

public interface ITpRequest {
    void accept();
    void deny();
    void run();
    void cancel();

    UUID getRequester();
    UUID getTarget();
    boolean isTpHere();
}
