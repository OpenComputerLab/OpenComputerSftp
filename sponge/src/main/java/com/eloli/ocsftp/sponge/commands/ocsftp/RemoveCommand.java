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
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RemoveCommand implements ICommand {
    private OcSftpCore core;

    public RemoveCommand(OcSftpCore core){
        this.core = core;
    }
    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .description(Text.of("Mount the disk in hand"))
                .arguments(GenericArguments.string(Text.of("name")))
                .executor(this::execute)
                .build();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Sponge.getScheduler().createTaskBuilder().execute(()->{
            if (src instanceof Player) {
                Player player = (Player) src;
                core.onRemove(new SpongePlayer(player), (String) args.getOne("name").get());
            }
        }).async().submit(core.adapter);
        return CommandResult.success();
    }
}
