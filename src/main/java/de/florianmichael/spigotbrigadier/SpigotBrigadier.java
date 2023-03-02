package de.florianmichael.spigotbrigadier;

import de.florianmichael.spigotbrigadier.command.BrigadierCommand;
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
