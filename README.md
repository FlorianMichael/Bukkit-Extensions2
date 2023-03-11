# Bukkit-Extensions2
Powerful Bukkit API for Plugin Developers v2

## How to add this to your plugin
Just copy this part to your *build.gradle*:
```groovy
repositories {
    maven {
        name = "Jitpack"
        url = "https://jitpack.io"
    }
}

dependencies {
    implementation "com.github.FlorianMichael:BukkitExtensions2:7a0f12a47d"
}
```

## Features
- Advanced command implementation for brigadier 
- Advanced lambda based event system wrapper for Bukkit

## BasedCommands
Bukkit-Extensions2 offers two types of commands, the *BrigadierCommand* and the *DefaultCommand*, Brigadier <br>
Commands use the [Brigadier](https://github.com/Mojang/brigadier) framework from Mojang, Default Commands remind strongly
of the classic command system from Spigot, where the args are passed as ObjectArrayHelper element (Util from the library)

### Example
```java
public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        BasedCommands.registerCommands(commands -> {
            commands.add(new BrigadierBasedCommand());
            commands.add(new SimpleCommand());
        });
    }

    public static class BrigadierBasedCommand extends BrigadierCommand {

        public BrigadierBasedCommand() {
            super("test");
        }

        @Override
        public LiteralArgumentBuilder<SpigotCommandSource> builder(LiteralArgumentBuilder<SpigotCommandSource> builder) {
            return builder.then(argument("count", IntegerArgumentType.integer()).executes(context -> {
                context.getSource().getCommandSender().sendMessage("Count: " + IntegerArgumentType.getInteger(context, "count"));
                return SUCCESS_FLAG;
            }));
        }
    }

    // Same syntax and functionality as the BrigadierBasedCommand, but without Brigadier
    public static class SimpleCommand extends DefaultCommand {

        public SimpleCommand() {
            super("test2");
        }

        @Override
        public void execute(CommandSender sender, String label, ObjectArrayHelper args) {
            if (args.isEmpty() || !args.isInteger(0)) {
                sender.sendMessage("Please enter an count as integer");
                return;
            }
            sender.sendMessage("Count: " + args.getInteger(0, 0));
            super.execute(sender, label, args);
        }
    }
}
```

## LambdaEvents
Bukkit-Extensions2 offers a lambda based event system using bukkit internals

### Example
```java
public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        LambdaEvents.registerEvent(this, PlayerJoinEvent.class, event -> {
            Bukkit.broadcastMessage("Player " + event.getPlayer().getName() + " joined!");
            // to your stuff...
        });
    }
}
```