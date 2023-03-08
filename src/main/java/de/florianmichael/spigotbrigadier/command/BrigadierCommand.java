/*
 * This file is part of SpigotBrigadier - https://github.com/FlorianMichael/SpigotBrigadier
 * Copyright (C) 2023 FlorianMichael/EnZaXD and contributors
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
import de.florianmichael.spigotbrigadier.SpigotBrigadier;
import de.florianmichael.spigotbrigadier.brigadier.SpigotCommandSource;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class BrigadierCommand extends DefaultCommand {
    public static final int SUCCESS_FLAG = 1;
    public static final int ERROR_FLAG = 0;

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
    public void init() {
        final CommandDispatcher<SpigotCommandSource> dispatcher = SpigotBrigadier.getCommandHandler().getCommandDispatcher();

        final List<String> names = new ArrayList<>(getAliases());
        names.add(getName());
        for (String alias : names) {
            this.rootNode = literal(alias);
            dispatcher.register(literal(alias).redirect(dispatcher.register(builder(this.rootNode))));
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final SpigotCommandSource commandSource = new SpigotCommandSource(sender, commandLabel);

        final String arguments = String.join(" ", args);
        final String command = commandLabel + (!arguments.isEmpty() ? " " + arguments : "");

        try {
            SpigotBrigadier.getCommandHandler().getCommandDispatcher().execute(command, commandSource);
        } catch (CommandSyntaxException e) {
            final ParseResults<SpigotCommandSource> parseResults = SpigotBrigadier.getCommandHandler().getCommandDispatcher().parse(command, commandSource);
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
        final ParseResults<SpigotCommandSource> parseResults = SpigotBrigadier.getCommandHandler().getCommandDispatcher().parse(command, commandSource);

        try {
            final Suggestions completionSuggestions = SpigotBrigadier.getCommandHandler().getCommandDispatcher().getCompletionSuggestions(parseResults).get();

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

    public LiteralArgumentBuilder<SpigotCommandSource> getRootNode() {
        return rootNode;
    }
}
