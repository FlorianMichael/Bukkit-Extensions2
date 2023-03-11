# Bukkit-Extensions2
Powerful Bukkit API for Plugin Developers v2

## How to add this to your plugin:
Just copy this part to your *build.gradle*:
```groovy
repositories {
    maven {
        name = "Jitpack"
        url = "https://jitpack.io"
    }
}

dependencies {
    implementation "com.github.FlorianMichael:BukkitExtensions2:93f5a668ff"
}
```

## Usage:
SpigotBrigadier offers two types of commands, the *BrigadierCommand* and the *DefaultCommand*, Brigadier <br>
Commands use the [Brigadier](https://github.com/Mojang/brigadier) framework from Mojang, Default Commands remind strongly
of the classic command system from Spigot, where the args are passed as ObjectArrayHelper element (Util from the library), 
for both command types there is an example below <br>

## Example:
```java
public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        SpigotBrigadier.setup(commands -> {
            commands.add(new BrigadierBasedCommand());
            commands.add(new SimpleCommand());
        });
    }

    public static class BrigadierBasedCommand extends BrigadierCommand {

        /**
         * Syntax: /test <count> (player name)
         */
        public BrigadierBasedCommand() {
            super("test");
        }

        @Override
        public LiteralArgumentBuilder<SpigotCommandSource> builder(LiteralArgumentBuilder<SpigotCommandSource> builder) {
            return builder.then(argument("count", IntegerArgumentType.integer()).executes(context -> {
                context.getSource().getCommandSender().sendMessage("Count: " + IntegerArgumentType.getInteger(context, "count"));
                return SUCCESS_FLAG;
            }).then(argument("player", BukkitPlayerArgumentType.create()).executes(context ->  {
                context.getSource().getCommandSender().sendMessage("Player with count: " + BukkitPlayerArgumentType.get(context, "player"));
                return SUCCESS_FLAG;
            })));
        }
    }

    // Same syntax and functionality as the BrigadierBasedCommand, but without Brigadier
    public static class SimpleCommand extends DefaultCommand {

        public SimpleCommand() {
            super("test2");
        }

        @Override
        public void execute(CommandSender sender, String label, ObjectArrayHelper args) {
            if (args.isEmpty()) {
                sender.sendMessage("Please enter an count");
                return;
            }
            if (!args.isInteger(0)) {
                sender.sendMessage("The count must be an integer");
                return;
            }
            final int count = args.getInteger(0, 0);
            sender.sendMessage("Count: " + count);
            if (args.isLength(2)) {
                if (args.isLarger(2)) {
                    sender.sendMessage("You can only provide a count or/and a player");
                    return;
                }
                final Player player = Bukkit.getPlayer(args.getString(1, ""));
                if (player == null) {
                    sender.sendMessage("This player is not online: " + args.getString(1, ""));
                } else {
                    sender.sendMessage("Player with count: " + player.getName());
                }
            }
            super.execute(sender, label, args);
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            if (args.length == 1) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            }
            return super.tabComplete(sender, alias, args);
        }
    }
}
```
