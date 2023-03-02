package de.florianmichael.spigotbrigadier.defaults;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.florianmichael.spigotbrigadier.brigadier.SpigotCommandSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class BukkitPlayerArgumentType implements ArgumentType<Player> {
    public static final DynamicCommandExceptionType invalidPlayerException = new DynamicCommandExceptionType(o -> new LiteralMessage("This player is not online: " + o));

    public static BukkitPlayerArgumentType create() {
        return new BukkitPlayerArgumentType();
    }

    public static Player get(final CommandContext<SpigotCommandSource> context, final String name) {
        return context.getArgument(name, Player.class);
    }

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
