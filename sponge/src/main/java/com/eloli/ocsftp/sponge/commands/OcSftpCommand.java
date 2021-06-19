package com.eloli.ocsftp.sponge.commands;

import com.eloli.ocsftp.core.OcSftpCore;
import com.eloli.ocsftp.sponge.commands.ocsftp.ListCommand;
import com.eloli.ocsftp.sponge.commands.ocsftp.MountCommand;
import com.eloli.ocsftp.sponge.commands.ocsftp.RemoveCommand;
import com.eloli.ocsftp.sponge.commands.ocsftp.SetPasswordCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class OcSftpCommand implements ICommand{
    private OcSftpCore core;

    public ListCommand listCommand;
    public MountCommand mountCommand;
    public RemoveCommand removeCommand;
    public SetPasswordCommand setPasswordCommand;

    public OcSftpCommand(OcSftpCore core){
        this.core = core;

        this.listCommand = new ListCommand(core);
        this.mountCommand = new MountCommand(core);
        this.removeCommand = new RemoveCommand(core);
        this.setPasswordCommand = new SetPasswordCommand(core);
    }
    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .description(Text.of("Use sftp manager files in OpenComputers."))
                .child(listCommand.getCommandSpec(), listCommand.getName())
                .child(mountCommand.getCommandSpec(), mountCommand.getName())
                .child(removeCommand.getCommandSpec(), removeCommand.getName())
                .child(setPasswordCommand.getCommandSpec(), setPasswordCommand.getName())
                .executor(this::execute)
                .build();
    }

    @Override
    public String getName() {
        return "ocsftp";
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("sftp address: "+core.config.server.publicHost));
        src.sendMessage(Text.of("sftp address: "+core.config.server.publicPort));
        return CommandResult.success();
    }
}
