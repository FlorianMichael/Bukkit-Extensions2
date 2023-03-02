# SpigotBrigadier
Spigot implementation for Mojang's [Brigadier](https://github.com/Mojang/brigadier) library

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
    implementation "com.github.FlorianMichael:SpigotBrigadier:56fc0c8a67"
}
```

## Usage:
SpigotBrigadier offers two types of commands, the *BrigadierCommand* and the *DefaultCommand*, Brigadier <br>
Commands use the [Brigadier](https://github.com/Mojang/brigadier) framework from Mojang, Default Commands remind strongly
of the classic command system from Spigot, where the args are passed as ObjectArrayHelper element (Util from the library), 
for both command types there is an example below <br>

## Example:
```java
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.florianmichael.spigotbrigadier.SpigotBrigadier;
import de.florianmichael.spigotbrigadier.brigadier.SpigotCommandSource;
import de.florianmichael.spigotbrigadier.command.BrigadierCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        SpigotBrigadier.setup(commands -> {
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
            return builder.then(literal("Test").executes(context -> {
                final CommandSender commandSender = context.getSource().getCommandSender();

                return SUCCESS_FLAG;
            }));
        }
    }

    public class SimpleCommand extends DefaultCommand {

        public SimpleCommand() {
            super("test2");
        }

        @Override
        public void execute(CommandSender sender, String label, ObjectArrayHelper args) {
            if (args.isString(0)) {
                final String first = args.getString(0);
                if (args.isIndexValid(4)) {
                    // ...
                }
            }
            super.execute(sender, label, args);
        }
    }
}
```
