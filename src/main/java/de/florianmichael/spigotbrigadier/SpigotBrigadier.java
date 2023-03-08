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
package de.florianmichael.spigotbrigadier;

import de.florianmichael.spigotbrigadier.command.CommandHandler;
import de.florianmichael.spigotbrigadier.command.DefaultCommand;
import de.florianmichael.spigotbrigadier.util.Pair;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SpigotBrigadier {
    public static final Logger LOGGER = LogManager.getLogManager().getLogger("SpigotBrigadier");
    private static CommandHandler commandHandler;

    public static void setup(final Consumer<List<DefaultCommand>> commandProvider) {
        setup(error -> error.getValue().sendMessage("Use: /" + error.getKey()), commandProvider);
    }

    public static void setup(final Consumer<Pair<String, CommandSender>> errorCallback, final Consumer<List<DefaultCommand>> commandProvider) {
        final List<DefaultCommand> commands = new ArrayList<>();
        commandProvider.accept(commands);

        if (commandHandler == null) {
            commandHandler = new CommandHandler();
            LOGGER.severe("no good? no, this man is definitely up to evil.");
        }

        commandHandler.create(errorCallback, commands);
    }

    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
