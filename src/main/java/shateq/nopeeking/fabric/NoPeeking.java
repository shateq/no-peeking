package shateq.nopeeking.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class NoPeeking implements ClientModInitializer {
    public static shateq.nopeeking.fabric.NoPeekingSettings SETTINGS = shateq.nopeeking.fabric.NoPeekingSettings.createAndLoad();

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            SETTINGS.functional(true);

            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                dispatcher.register(
                    ClientCommandManager.literal("what").executes(c -> {
                            c.getSource().getPlayer().sendSystemMessage(
                                logSettings()
                            );
                            return 0;
                        }
                    ));
                dispatcher.register(
                    ClientCommandManager.literal("yesno").executes(context -> {
                        SETTINGS.functional(!SETTINGS.functional());
                        return 0;
                    })
                );
            });
        }
    }

    private Component logSettings() {
        return Component.literal("\nNoPeeking Settings\n").withStyle(ChatFormatting.BOLD)
            .append("Functional: " + SETTINGS.functional() + "\n")
            .append("Render Self: " + SETTINGS.renderSelf() + "\n")
            .append("As Whitelist: " + SETTINGS.asWhitelist() + "\n")
            .append("List: ")
            .append(SETTINGS.blacklist().toString());
    }
}
