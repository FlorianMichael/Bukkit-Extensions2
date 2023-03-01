package de.florianmichael.spigotbrigadier.brigadier;

import org.bukkit.command.CommandSender;

public class SpigotCommandSource {
    private final CommandSender commandSender;
    private final String label;

    public SpigotCommandSource(CommandSender commandSender, String label) {
        this.commandSender = commandSender;
        this.label = label;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }

    public String getLabel() {
        return label;
    }
}
