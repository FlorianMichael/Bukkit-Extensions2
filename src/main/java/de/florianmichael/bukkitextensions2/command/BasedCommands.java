/*
 * This file is part of Bukkit-Extensions2 - https://github.com/FlorianMichael/Bukkit-Extensions2
 * Copyright (C) 2023 FlorianMichael/MrLookAtMe (EnZaXD) and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.florianmichael.bukkitextensions2.command;

import com.mojang.brigadier.CommandDispatcher;
import de.florianmichael.bukkitextensions2.command.brigadier.SpigotCommandSource;
import de.florianmichael.bukkitextensions2.command.type.DefaultCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Register simple and brigadier based commands
 */
public class BasedCommands implements Listener {

    /**
     * Command dispatcher for brigadier based commands
     */
    public final static CommandDispatcher<SpigotCommandSource> COMMAND_DISPATCHER = new CommandDispatcher<>();

    /**
     * Register all commands and create a new CommandHandler instance
     *
     * @param commandProvider Provides all commands via consumer
     */
    public static void registerCommands(final Consumer<List<DefaultCommand>> commandProvider) {
        registerCommands((usage, sender) -> sender.sendMessage(usage), commandProvider);
    }

    /**
     * Register all commands and create a new CommandHandler instance
     *
     * @param errorCallback   Callback if the command dispatcher fails with parsing the command
     * @param commandProvider Provides all commands via consumer
     */
    public static void registerCommands(final ErrorCallback errorCallback, final Consumer<List<DefaultCommand>> commandProvider) {
        final List<DefaultCommand> commands = new ArrayList<>();
        commandProvider.accept(commands);

        for (DefaultCommand command : commands) {
            command.init(errorCallback);
            registerBukkitCommand(command);
        }
    }

    /**
     * Register a bukkit command using NMS Reflection
     *
     * @param bukkitCommand The command that should be registered
     */
    public static void registerBukkitCommand(final BukkitCommand bukkitCommand) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(bukkitCommand.getName(), bukkitCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
