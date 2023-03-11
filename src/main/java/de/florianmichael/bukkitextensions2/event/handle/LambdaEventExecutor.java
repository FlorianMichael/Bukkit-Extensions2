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
package de.florianmichael.bukkitextensions2.event.handle;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

/**
 * The implementation of {@link EventExecutor} to register in the handler list
 */
public record LambdaEventExecutor<T extends Event>(Class<T> eventClass, LambdaHandler<T> handler) implements EventExecutor {

    @Override
    public void execute(Listener listener, Event event) {
        if (this.eventClass.isAssignableFrom(event.getClass())) {
            this.handler.handle((T) event);
        }
    }
}
