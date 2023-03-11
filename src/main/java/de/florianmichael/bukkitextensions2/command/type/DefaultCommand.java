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

import de.florianmichael.bukkitextensions2.command.ErrorCallback;
import de.florianmichael.bukkitextensions2.java.ObjectArrayHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
/*
 * Default implementation of a based command
 */
public abstract class DefaultCommand extends BukkitCommand {
    /**
     * Instance for the error callback
     */
    private ErrorCallback errorCallback;

    /**
     * Default constructors for providing based information
     *
     * @param name        main command name
     */
    public DefaultCommand(final String name) {
        this(name, null);
    }

    /**
     * Default constructors for providing based information
     *
     * @param name        main command name
     * @param description description what to command does
     */
    public DefaultCommand(final String name, final String description) {
        this(name, description, null);
    }

    /**
     * Default constructors for providing based information
     *
     * @param name        main command name
     * @param description description what to command does
     * @param usage       basic command syntax
     */
    public DefaultCommand(final String name, final String description, final String usage) {
        this(name, description, usage, null);
    }

    /**
     * Default constructors for providing based information
     *
     * @param name        main command name
     * @param description description what to command does
     * @param usage       basic command syntax
     * @param permission  required permission to execute the command
     */
    public DefaultCommand(final String name, final String description, final String usage, final String permission) {
        this(name, description, usage, permission, new ArrayList<>());
    }

    /**
     * Default constructors for providing based information
     *
     * @param name        main command name
     * @param description description what to command does
     * @param usage       basic command syntax
     * @param permission  required permission to execute the command
     * @param aliases     optional command names
     */
    public DefaultCommand(final String name, final String description, final String usage, final String permission, final List<String> aliases) {
        super(name);

        this.setDescription(description);
        this.setUsage(usage);
        this.setPermission(permission);
        this.setAliases(aliases);
    }

    /**
     * Called when the command will be added
     * @param errorCallback callback instance
     */
    public void init(final ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    public void execute(final CommandSender sender, final String label, final ObjectArrayHelper args) {
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        this.execute(sender, commandLabel, new ObjectArrayHelper(args));
        return false;
    }

    public ErrorCallback getErrorCallback() {
        return errorCallback;
    }
}
