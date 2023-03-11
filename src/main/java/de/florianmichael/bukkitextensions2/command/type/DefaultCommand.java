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
package de.florianmichael.bukkitextensions2.command.type;

import de.florianmichael.bukkitextensions2.util.ObjectArrayHelper;
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
