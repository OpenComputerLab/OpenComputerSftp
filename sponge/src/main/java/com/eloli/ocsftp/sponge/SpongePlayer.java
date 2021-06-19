package com.eloli.ocsftp.sponge;

import com.eloli.ocsftp.core.AbstractPlayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class SpongePlayer extends AbstractPlayer {

    public Player handle;

    public SpongePlayer(Player handle) {
        super(handle.getName(), handle.getUniqueId());
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(Text.of(message));
    }

    @Override
    public boolean isOnline() {
        return handle.isOnline();
    }
}
