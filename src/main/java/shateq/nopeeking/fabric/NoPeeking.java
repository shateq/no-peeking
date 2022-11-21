package shateq.nopeeking.fabric;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityType! it was here all that time!
 */
public class NoPeeking implements ClientModInitializer, ModMenuApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoPeeking.class);
    public static final Rubbernecks peeking = Rubbernecks.read(Rubbernecks.configDir().resolve("rubbernecks.json"));

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.debug("Development Environment: Always Functional");
            peeking.functional = true;
        }
        // Debug
        peeking.self_visible = true;
        peeking.blacklist.add(EntityType.CREEPER);
        peeking.blacklist.add(EntityType.BLAZE);

        ClientCommandManager.DISPATCHER.register(
            ClientCommandManager.literal("yesno").executes(context -> {
                peeking.functional = !peeking.functional;
//                try {
//                    LOGGER.info("Config write");
//                    peeking.write();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                return 0;
            })
        );
    }
}
