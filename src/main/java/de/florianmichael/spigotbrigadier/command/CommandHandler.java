package de.florianmichael.spigotbrigadier.command;

import com.mojang.brigadier.CommandDispatcher;
import de.florianmichael.spigotbrigadier.brigadier.SpigotCommandSource;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.List;

public class CommandHandler implements Listener {
    private final CommandDispatcher<SpigotCommandSource> commandDispatcher = new CommandDispatcher<>();

    public void create(final List<BCommand> commands) {
        for (BCommand command : commands) {
            command.setup(this.commandDispatcher);

            try {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

                commandMap.register(command.getName(), command);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CommandDispatcher<SpigotCommandSource> getCommandDispatcher() {
        return commandDispatcher;
    }
}
