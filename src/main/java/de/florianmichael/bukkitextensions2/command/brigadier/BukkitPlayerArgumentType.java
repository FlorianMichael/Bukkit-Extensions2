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
package de.florianmichael.bukkitextensions2.command.brigadier;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Basic implementation of a Bukkit.getOnlinePlayers() argument type for brigadier
 */
public class BukkitPlayerArgumentType implements ArgumentType<Player> {

    /**
     * throws if the input player is not online
     */
    public static final DynamicCommandExceptionType invalidPlayerException = new DynamicCommandExceptionType(o -> new LiteralMessage("This player is not online: " + o));

    /**
     * Creates a new instance for brigadier
     */
    public static BukkitPlayerArgumentType create() {
        return new BukkitPlayerArgumentType();
    }

    /**
     * @param context Given context to get the player
     * @param name    Name of the player
     * @return        the player
     */
    public static Player get(final CommandContext<SpigotCommandSource> context, final String name) {
        return context.getArgument(name, Player.class);
    }

    /**
     *
     * @param reader Default input reader provided by brigadier
     * @return Parse result as a player
     * @throws CommandSyntaxException If brigadier can not parse the player
     */
    @Override
    public Player parse(StringReader reader) throws CommandSyntaxException {
        final String readContext = reader.readString();

        final Player player = Bukkit.getPlayer(reader.readString());
        if (player == null) throw invalidPlayerException.createWithContext(reader, readContext);
        return player;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Player player : Bukkit.getOnlinePlayers()) builder.suggest(player.getName());
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.singleton("Notch");
    }
}
