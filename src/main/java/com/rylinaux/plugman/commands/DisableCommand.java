package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class DisableCommand extends AbstractCommand {

    public static final String DESCRIPTION = "Disable a plugin.";

    public static final String PERMISSION = "plugman.disable";

    public static final String USAGE = "/plugman disable [plugin|all]";

    public static final String[] SUB_PERMISSIONS = {"all"};

    public DisableCommand(CommandSender sender) {
        super(sender, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.specify-plugin"));
            return;
        }

        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
            if (hasPermission("all")) {
                PluginUtils.disableAll();
                sender.sendMessage(PlugMan.getInstance().getMessageManager().format("disable.all"));
            } else {
                sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            }
            return;
        }

        Plugin target = PluginUtils.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.invalid-plugin"));
            return;
        }

        if (PluginUtils.isIgnored(target)) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.ignored"));
            return;
        }

        if (!target.isEnabled()) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("disable.already-disabled", target.getName()));
            return;
        }

        PluginUtils.disable(target);

        sender.sendMessage(PlugMan.getInstance().getMessageManager().format("disable.disabled", target.getName()));

    }

}