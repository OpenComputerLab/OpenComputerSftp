package com.eloli.ocsftp.sponge;

import com.eloli.ocsftp.core.OcSftpCore;
import com.eloli.ocsftp.core.PlatformAdapter;
import com.eloli.ocsftp.sponge.commands.OcSftpCommand;
import com.eloli.sodioncore.dependency.DependencyManager;
import com.eloli.sodioncore.file.BaseFileService;
import com.eloli.sodioncore.logger.AbstractLogger;
import com.eloli.sodioncore.orm.AbstractSodionCore;
import com.eloli.sodioncore.sponge.SodionCore;
import com.eloli.sodioncore.sponge.SpongeLogger;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(id = "ocsftp",
        description = "hi",
        dependencies = {
                @Dependency(id = "sodioncore"),
        }
)
public class SpongeLoader implements PlatformAdapter {
    public static SpongeLoader instance;
    public SodionCore sodionAuthCore;
    public OcSftpCore core;
    public OcSftpCommand command;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path privateConfigDir;

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        instance = this;
        try{
            core = new OcSftpCore(this);
            core.init();
        } catch (Exception e) {
            logger.warn("Failed to load ocsftp.",e);
        }

        command = new OcSftpCommand(core);
        Sponge.getCommandManager().register(
                this,
                command.getCommandSpec(),
                command.getName());
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join join){
        core.join(new SpongePlayer(join.getTargetEntity()));
    }

    @Override
    public DependencyManager getDependencyManager() {
        return ((SodionCore) getSodionCore()).getDependencyManager(this);
    }

    @Override
    public BaseFileService getBaseFile() {
        return new BaseFileService(privateConfigDir.toString());
    }

    @Override
    public AbstractSodionCore getSodionCore() {
        return (AbstractSodionCore) Sponge.getPluginManager().getPlugin("sodioncore").get().getInstance().get();
    }

    @Override
    public AbstractLogger getLogger() {
        return new SpongeLogger(logger);
    }

    @Override
    public Path getDataPath() {
        return privateConfigDir;
    }
}
