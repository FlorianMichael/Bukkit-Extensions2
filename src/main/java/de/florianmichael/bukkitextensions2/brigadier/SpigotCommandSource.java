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
package de.florianmichael.bukkitextensions2.brigadier;

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
