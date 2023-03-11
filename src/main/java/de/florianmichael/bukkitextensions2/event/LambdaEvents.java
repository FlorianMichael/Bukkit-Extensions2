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
package de.florianmichael.bukkitextensions2.event;

import de.florianmichael.bukkitextensions2.event.handle.LambdaEventExecutor;
import de.florianmichael.bukkitextensions2.event.handle.LambdaHandler;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Register simple lambdas as event handler for bukkit events
 */
public class LambdaEvents<T extends Event> {

    /**
     * The default value for the priority of the handler if none is specified
     */
    public static final EventPriority DEFAULT_PRIORITY = EventPriority.NORMAL;
    /**
     * The default value for the ignoreCancelled state of the handler if none is specified
     */
    public static final boolean DEFAULT_IGNORE_CANCELLED = false;

    /**
     * Register a lambda as the handler of the given event
     *
     * @param plugin     The owner plugin of the event handler
     * @param eventClass The class of the event
     * @param handler    The event handler itself
     * @return The registered LambdaEvents instance
     */
    public static <T extends Event> LambdaEvents<T> registerEvent(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler) {
        return registerEvent(plugin, eventClass, handler, DEFAULT_PRIORITY, DEFAULT_IGNORE_CANCELLED);
    }

    /**
     * Register a lambda as the handler of the given event
     *
     * @param plugin     The owner plugin of the event handler
     * @param eventClass The class of the event
     * @param handler    The event handler itself
     * @param priority   The priority of the event handler
     * @return The registered LambdaEvents instance
     */
    public static <T extends Event> LambdaEvents<T> registerEvent(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler, final EventPriority priority) {
        return registerEvent(plugin, eventClass, handler, priority, DEFAULT_IGNORE_CANCELLED);
    }

    /**
     * Register a lambda as the handler of the given event
     *
     * @param plugin          The owner plugin of the event handler
     * @param eventClass      The class of the event
     * @param handler         The event handler itself
     * @param ignoreCancelled If cancelled events should be ignored by this handler
     * @return The registered LambdaEvents instance
     */
    public static <T extends Event> LambdaEvents<T> registerEvent(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler, final boolean ignoreCancelled) {
        return registerEvent(plugin, eventClass, handler, DEFAULT_PRIORITY, ignoreCancelled);
    }

    /**
     * Register a lambda as the handler of the given event
     *
     * @param plugin          The owner plugin of the event handler
     * @param eventClass      The class of the event
     * @param handler         The event handler itself
     * @param priority        The priority of the event handler
     * @param ignoreCancelled If cancelled events should be ignored by this handler
     * @return The registered LambdaEvents instance
     */
    public static <T extends Event> LambdaEvents<T> registerEvent(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler, final EventPriority priority, final boolean ignoreCancelled) {
        HandlerList handlerList = getHandlerList(eventClass);
        RegisteredListener registeredListener = new RegisteredListener(handler, new LambdaEventExecutor<>(eventClass, handler), priority, plugin, ignoreCancelled);
        LambdaEvents<T> lambdaEvents = new LambdaEvents<>(handlerList, eventClass, registeredListener);
        lambdaEvents.register();
        return lambdaEvents;
    }

    /**
     * Get the handler list of a given event class<br>
     * See {@code SimplePluginManager#getEventListeners(Class)} for reference
     *
     * @param eventClass The class of the event
     * @return The handler list of the event
     * @throws IllegalStateException        If the {@code static getHandlerList} method could not be invoked
     * @throws IllegalPluginAccessException If the {@code static getHandlerList} method could not be found
     */
    public static HandlerList getHandlerList(final Class<? extends Event> eventClass) {
        Class<?> clazz = eventClass;
        do {
            try {
                Method getHandlerList = clazz.getDeclaredMethod("getHandlerList");
                getHandlerList.setAccessible(true);
                return (HandlerList) getHandlerList.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Unable to invoke getHandlerList method for event " + eventClass.getName());
            } catch (NoSuchMethodException ignored) {
            }

            clazz = clazz.getSuperclass();
        } while (clazz != null && Event.class.isAssignableFrom(clazz) && !Event.class.equals(clazz));
        throw new IllegalPluginAccessException("Unable to find handler list for event " + eventClass.getName() + ". Static getHandlerList method required!");
    }

    private final HandlerList handlerList;
    private final Class<T> eventClass;
    private final RegisteredListener registeredListener;

    public LambdaEvents(final HandlerList handlerList, final Class<T> eventClass, final RegisteredListener registeredListener) {
        this.handlerList = handlerList;
        this.eventClass = eventClass;
        this.registeredListener = registeredListener;
    }

    public Class<T> getEventClass() {
        return this.eventClass;
    }

    /**
     * Register the event handler in the handler list
     */
    public void register() {
        this.handlerList.register(this.registeredListener);
    }

    /**
     * Unregister the event handler from the handler list
     */
    public void unregister() {
        this.handlerList.unregister(this.registeredListener);
    }
}
