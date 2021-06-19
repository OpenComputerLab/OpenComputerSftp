package com.eloli.ocsftp.sponge.commands.ocsftp;

import com.eloli.ocsftp.core.OcSftpCore;
import com.eloli.ocsftp.sponge.SpongePlayer;
import com.eloli.ocsftp.sponge.commands.ICommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

public class MountCommand implements ICommand {
    private OcSftpCore core;

    public MountCommand(OcSftpCore core){
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
        return "mount";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Sponge.getScheduler().createTaskBuilder().execute(()->{
        if (src instanceof Player) {
                Player player = (Player) src;
                Optional<ItemStack> itemStack = player.getItemInHand(HandTypes.MAIN_HAND);
                String name = (String) args.getOne("name").get();
                if (!name.matches("[a-zA-Z0-9]*")) {
                    player.sendMessage(Text.of("Name should only include characters and number."));
                    return;
                }
                if (!itemStack.isPresent()
                        || !"item.oc.storage".equals(itemStack.get().getType().toString())) {
                    player.sendMessage(Text.of("No disk found."));
                    return;
                }
                Optional<String> address = itemStack.get().toContainer().getString(DataQuery.of("UnsafeData", "oc:data", "node", "address"));
                if (!address.isPresent()) {
                    player.sendMessage(Text.of("No address found."));
                    return;
                }
                core.onMount(new SpongePlayer(player), name, UUID.fromString(address.get()));
            }
        }).async().submit(core.adapter);
        return CommandResult.success();
    }
}
