# SpigotBrigadier
Spigot implementation for Mojang's Brigadier library

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
    implementation "com.github.FlorianMichael:SpigotBrigadier:1ccd5c1e6b"
}
```

## Usage:
```java
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.florianmichael.spigotbrigadier.SpigotBrigadier;
import de.florianmichael.spigotbrigadier.brigadier.SpigotCommandSource;
import de.florianmichael.spigotbrigadier.command.BCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        SpigotBrigadier.setup(commands -> {
            commands.add(new TestCommand());
        });
    }

    public static class TestCommand extends BCommand {

        public TestCommand() {
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
}
```
