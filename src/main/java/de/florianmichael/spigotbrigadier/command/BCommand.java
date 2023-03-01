package de.florianmichael.spigotbrigadier.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import de.florianmichael.spigotbrigadier.brigadier.SpigotCommandSource;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class BCommand extends BukkitCommand {
    public static final int SUCCESS_FLAG = 1;
    public static final int ERROR_FLAG = 0;

    private LiteralArgumentBuilder<SpigotCommandSource> rootNode;
    private CommandDispatcher<SpigotCommandSource> commandDispatcher;

    public BCommand(final String name) {
        this(name, null);

    }
    public BCommand(final String name, final String description) {
        this(name, description, null);
    }

    public BCommand(final String name, final String description, final String usage) {
        this(name, description, usage, null);
    }

    public BCommand(final String name, final String description, final String usage, final String permission) {
        this(name, description, usage, permission, new ArrayList<>());
    }

    public BCommand(final String name, final String description, final String usage, final String permission, final List<String> aliases) {
        super(name);

        this.setDescription(description);
        this.setUsage(usage);
        this.setPermission(permission);
        this.setAliases(aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final SpigotCommandSource commandSource = new SpigotCommandSource(sender, commandLabel);

        final String arguments = String.join(" ", args);
        final String command = commandLabel + (!arguments.isEmpty() ? " " + arguments : "");

        try {
            this.commandDispatcher.execute(command, commandSource);
        } catch (CommandSyntaxException e) {
            final ParseResults<SpigotCommandSource> parseResults = this.commandDispatcher.parse(command, commandSource);
            final CommandNode<SpigotCommandSource> lastNode = parseResults.getContext().getNodes().get(parseResults.getContext().getNodes().size() - 1).getNode();

            sender.sendMessage("Use: /" + commandLabel + " " + lastNode.getChildren().stream().map(CommandNode::getUsageText).collect(Collectors.joining(" ")));
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        final SpigotCommandSource commandSource = new SpigotCommandSource(sender, alias);

        final String arguments = String.join(" ", args);
        final String command = alias + (!arguments.isEmpty() ? " " + arguments : "");
        final ParseResults<SpigotCommandSource> parseResults = this.commandDispatcher.parse(command, commandSource);

        try {
            final Suggestions completionSuggestions = this.commandDispatcher.getCompletionSuggestions(parseResults).get();

            return completionSuggestions.getList().stream().map(Suggestion::getText).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException ignored) {}

        return super.tabComplete(sender, alias, args);
    }

    public LiteralArgumentBuilder<SpigotCommandSource> literal(final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public RequiredArgumentBuilder<SpigotCommandSource, ?> argument(final String name, final ArgumentType<?> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public abstract LiteralArgumentBuilder<SpigotCommandSource> builder(final LiteralArgumentBuilder<SpigotCommandSource> builder);

    public void setup(final CommandDispatcher<SpigotCommandSource> dispatcher) {
        final List<String> names = new ArrayList<>(getAliases());
        names.add(getName());
        for (String alias : names) {
            this.rootNode = literal(alias);
            dispatcher.register(literal(alias).redirect(dispatcher.register(builder(this.rootNode))));
        }
        this.commandDispatcher = dispatcher;
    }

    public LiteralArgumentBuilder<SpigotCommandSource> getRootNode() {
        return rootNode;
    }
}
