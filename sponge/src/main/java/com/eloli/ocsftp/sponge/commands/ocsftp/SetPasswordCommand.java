package com.eloli.ocsftp.sponge.commands.ocsftp;

import com.eloli.ocsftp.core.OcSftpCore;
import com.eloli.ocsftp.sponge.SpongePlayer;
import com.eloli.ocsftp.sponge.commands.ICommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.*;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SetPasswordCommand implements ICommand {
    private OcSftpCore core;

    public SetPasswordCommand(OcSftpCore core){
        this.core = core;
    }
    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .description(Text.of("Mount the disk in hand"))
                .arguments(GenericArguments.string(Text.of("password")))
                .executor(this::execute)
                .build();
    }

    @Override
    public String getName() {
        return "setpassword";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Sponge.getScheduler().createTaskBuilder().execute(()->{
            if (src instanceof Player) {
                Player player = (Player) src;
                core.onSetPassword(new SpongePlayer(player), (String) args.getOne("password").get());
            }
        }).async().submit(core.adapter);
        return CommandResult.success();
    }
}
