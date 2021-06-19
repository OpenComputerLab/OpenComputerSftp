package com.eloli.ocsftp.sponge.commands.ocsftp;

import com.eloli.ocsftp.core.OcSftpCore;
import com.eloli.ocsftp.sponge.SpongePlayer;
import com.eloli.ocsftp.sponge.commands.ICommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListCommand implements ICommand {
    private OcSftpCore core;

    public ListCommand(OcSftpCore core){
        this.core = core;
    }
    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .description(Text.of("List all mounted disks."))
                .executor(this::execute)
                .build();
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Sponge.getScheduler().createTaskBuilder().execute(()->{
            if (src instanceof Player) {
                    Player player = (Player) src;
                    List<String> disks = core.onTabDisks(new SpongePlayer(player));
                    for (String disk : disks) {
                        player.sendMessage(Text.of("Found: "+disk));
                    }
                    if(disks.isEmpty()){
                        player.sendMessage(Text.of("No mounted disk found."));
                    }
            }
        }).async().submit(core.adapter);
        return CommandResult.success();
    }
}
