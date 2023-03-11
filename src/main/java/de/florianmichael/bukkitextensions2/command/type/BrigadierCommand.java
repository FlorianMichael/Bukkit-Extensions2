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

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import de.florianmichael.bukkitextensions2.command.BasedCommands;
import de.florianmichael.bukkitextensions2.command.ErrorCallback;
import de.florianmichael.bukkitextensions2.command.brigadier.SpigotCommandSource;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class BrigadierCommand extends DefaultCommand {

    /**
     * Default flags for brigadier
     */
    public static final int SUCCESS_FLAG = 1;
    public static final int ERROR_FLAG = 0;

    /**
     * Root node of the command tree from the current command
     */
    private LiteralArgumentBuilder<SpigotCommandSource> rootNode;

    public BrigadierCommand(String name) {
        super(name);
    }

    public BrigadierCommand(String name, String description) {
        super(name, description);
    }

    public BrigadierCommand(String name, String description, String usage) {
        super(name, description, usage);
    }

    public BrigadierCommand(String name, String description, String usage, String permission) {
        super(name, description, usage, permission);
    }

    public BrigadierCommand(String name, String description, String usage, String permission, List<String> aliases) {
        super(name, description, usage, permission, aliases);
    }

    @Override
    public void init(ErrorCallback errorCallback) {
        super.init(errorCallback);

        final List<String> names = new ArrayList<>(getAliases());
        names.add(getName());
        for (String alias : names) {
            this.rootNode = literal(alias);
            BasedCommands.COMMAND_DISPATCHER.register(literal(alias).redirect(BasedCommands.COMMAND_DISPATCHER.register(builder(this.rootNode))));
        }
    }

    /**
     * Execute logic of the brigadier command
     *
     * @param sender Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args All arguments passed to the command, split via ' '
     * @return always false
     */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final SpigotCommandSource commandSource = new SpigotCommandSource(sender, commandLabel);

        final String arguments = String.join(" ", args);
        final String command = commandLabel + (!arguments.isEmpty() ? " " + arguments : "");

        try {
            BasedCommands.COMMAND_DISPATCHER.execute(command, commandSource);
        } catch (CommandSyntaxException e) {
            final ParseResults<SpigotCommandSource> parseResults = BasedCommands.COMMAND_DISPATCHER.parse(command, commandSource);
            final CommandNode<SpigotCommandSource> lastNode = parseResults.getContext().getNodes().get(parseResults.getContext().getNodes().size() - 1).getNode();

            getErrorCallback().print(commandLabel + " " + lastNode.getChildren().stream().map(CommandNode::getUsageText).collect(Collectors.joining(" ")), sender);
        }
        return false;
    }

    /**
     * Tab complete logic of the brigadier command
     *
     * @param sender Source object which is executing this command
     * @param alias the alias being used
     * @param args All arguments passed to the command, split via ' '
     * @return the tab completion results
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        final SpigotCommandSource commandSource = new SpigotCommandSource(sender, alias);

        final String arguments = String.join(" ", args);
        final String command = alias + (!arguments.isEmpty() ? " " + arguments : "");
        final ParseResults<SpigotCommandSource> parseResults = BasedCommands.COMMAND_DISPATCHER.parse(command, commandSource);

        try {
            final Suggestions completionSuggestions = BasedCommands.COMMAND_DISPATCHER.getCompletionSuggestions(parseResults).get();

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

    /**
     * Wrapper function for the implementor to create the brigadier tree
     *
     * @param builder The input builder
     * @return The input builder with the command tree
     */
    public abstract LiteralArgumentBuilder<SpigotCommandSource> builder(final LiteralArgumentBuilder<SpigotCommandSource> builder);

    public LiteralArgumentBuilder<SpigotCommandSource> getRootNode() {
        return rootNode;
    }
}
