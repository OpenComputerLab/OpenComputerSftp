package com.eloli.ocsftp.sponge.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;

public interface ICommand {
    CommandSpec getCommandSpec();
    String getName();
    CommandResult execute(CommandSource src, CommandContext args) throws CommandException;
}
