package de.florianmichael.spigotbrigadier.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements Listener {
    private final List<BCommand> commands = new ArrayList<>();

    public void create(final List<BCommand> commands) {
        for (BCommand command : commands) {
            try {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

                commandMap.register(command.getName(), command);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        this.commands.addAll(commands);
    }

    public List<BCommand> getCommands() {
        return commands;
    }
}
