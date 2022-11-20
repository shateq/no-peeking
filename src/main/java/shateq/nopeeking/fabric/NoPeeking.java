package shateq.nopeeking.fabric;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NoPeeking implements ClientModInitializer, ModMenuApi {
    public static final Logger LOGGER = LoggerFactory.getLogger(NoPeeking.class);
    public static final Rubbernecks config = Rubbernecks.read(Rubbernecks.configDir().resolve("rubbernecks.json"));
    public static boolean allowCow;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Generated default configurations!");
        allowCow = config.nopeeking.functional;

        ClientCommandManager.DISPATCHER.register(
            ClientCommandManager.literal("yesno")
                .executes(context -> {
                    config.nopeeking.functional = !config.nopeeking.functional;

                    try {
                        config.write();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    allowCow = config.nopeeking.functional;
                    LOGGER.info("yes");
                    return 0;
                })
        );
    }
}
