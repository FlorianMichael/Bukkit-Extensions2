package de.florianmichael.bukkitextensions2.util;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
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
    private static final EventPriority DEFAULT_PRIORITY = EventPriority.NORMAL;
    /**
     * The default value for the ignoreCancelled state of the handler if none is specified
     */
    private static final boolean DEFAULT_IGNORE_CANCELLED = false;

    /**
     * Register a lambda as the handler of the given event
     *
     * @param plugin     The owner plugin of the event handler
     * @param eventClass The class of the event
     * @param handler    The event handler itself
     * @return The registered LambdaEvents instance
     */
    public static <T extends Event> LambdaEvents<T> register(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler) {
        return register(plugin, eventClass, handler, DEFAULT_PRIORITY, DEFAULT_IGNORE_CANCELLED);
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
    public static <T extends Event> LambdaEvents<T> register(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler, final EventPriority priority) {
        return register(plugin, eventClass, handler, priority, DEFAULT_IGNORE_CANCELLED);
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
    public static <T extends Event> LambdaEvents<T> register(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler, final boolean ignoreCancelled) {
        return register(plugin, eventClass, handler, DEFAULT_PRIORITY, ignoreCancelled);
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
    public static <T extends Event> LambdaEvents<T> register(final Plugin plugin, final Class<T> eventClass, final LambdaHandler<T> handler, final EventPriority priority, final boolean ignoreCancelled) {
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
    private static HandlerList getHandlerList(final Class<? extends Event> eventClass) {
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

    private LambdaEvents(final HandlerList handlerList, final Class<T> eventClass, final RegisteredListener registeredListener) {
        this.handlerList = handlerList;
        this.eventClass = eventClass;
        this.registeredListener = registeredListener;
    }

    /**
     * Get the class of the handled event
     */
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


    /**
     * The interface for lambda event handlers
     */
    public interface LambdaHandler<T extends Event> extends Listener {
        void handle(T event);
    }

    /**
     * The implementation of {@link EventExecutor} to register in the handler list
     */
    private record LambdaEventExecutor<T extends Event>(Class<T> eventClass, LambdaHandler<T> handler) implements EventExecutor {

        @Override
        public void execute(Listener listener, Event event) {
            if (this.eventClass.isAssignableFrom(event.getClass())) this.handler.handle((T) event);
        }
    }
}
