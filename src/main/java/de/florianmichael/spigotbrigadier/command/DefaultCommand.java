package de.florianmichael.spigotbrigadier.command;

import de.florianmichael.spigotbrigadier.util.ObjectArrayHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultCommand extends BukkitCommand {
    public DefaultCommand(final String name) {
        this(name, null);

    }
    public DefaultCommand(final String name, final String description) {
        this(name, description, null);
    }

    public DefaultCommand(final String name, final String description, final String usage) {
        this(name, description, usage, null);
    }

    public DefaultCommand(final String name, final String description, final String usage, final String permission) {
        this(name, description, usage, permission, new ArrayList<>());
    }

    public DefaultCommand(final String name, final String description, final String usage, final String permission, final List<String> aliases) {
        super(name);

        this.setDescription(description);
        this.setUsage(usage);
        this.setPermission(permission);
        this.setAliases(aliases);
    }

    public void init() {
    }

    public void execute(final CommandSender sender, final String label, final ObjectArrayHelper args) {
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        this.execute(sender, commandLabel, new ObjectArrayHelper(args));
        return false;
    }
}
