# SpigotBrigadier
Spigot implementation for Mojang's Brigadier library

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
                return SUCCESS_FLAG;
            }));
        }
    }
}
```
