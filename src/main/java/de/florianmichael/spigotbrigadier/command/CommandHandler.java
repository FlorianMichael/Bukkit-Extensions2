/*
 * This file is part of SpigotBrigadier - https://github.com/FlorianMichael/SpigotBrigadier
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
