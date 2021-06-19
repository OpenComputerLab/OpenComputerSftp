package com.eloli.ocsftp.core;

import java.util.UUID;

public abstract class AbstractPlayer {
    protected final String name;
    protected final UUID uuid;

    public AbstractPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public abstract void sendMessage(String message);

    public abstract boolean isOnline();
}
