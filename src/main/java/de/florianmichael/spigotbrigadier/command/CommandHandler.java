package de.florianmichael.spigotbrigadier.command;

import com.mojang.brigadier.CommandDispatcher;
import de.florianmichael.spigotbrigadier.brigadier.SpigotCommandSource;
import de.florianmichael.spigotbrigadier.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class CommandHandler implements Listener {
    private final CommandDispatcher<SpigotCommandSource> commandDispatcher = new CommandDispatcher<>();

    public void create(final Consumer<Pair<String, CommandSender>> errorCallback, final List<DefaultCommand> commands) {
        for (DefaultCommand command : commands) {
            command.init();

            this.registerBukkitCommand(command);
        }
    }

    public void registerBukkitCommand(final BukkitCommand bukkitCommand) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(bukkitCommand.getName(), bukkitCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommandDispatcher<SpigotCommandSource> getCommandDispatcher() {
        return commandDispatcher;
    }
}
