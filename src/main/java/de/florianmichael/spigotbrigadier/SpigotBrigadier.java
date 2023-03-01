package de.florianmichael.spigotbrigadier;

import de.florianmichael.spigotbrigadier.command.BCommand;
import de.florianmichael.spigotbrigadier.command.CommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SpigotBrigadier {
    public static final Logger LOGGER = LogManager.getLogManager().getLogger("SpigotBrigadier");
    private static CommandHandler commandHandler;

    public static void setup(final Consumer<List<BCommand>> commandProvider) {
        final List<BCommand> commands = new ArrayList<>();
        commandProvider.accept(commands);

        if (commandHandler == null) {
            commandHandler = new CommandHandler();
            LOGGER.severe("no good? no, this man is definitely up to evil.");
        }

        commandHandler.create(commands);
    }

    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
