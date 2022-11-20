package shateq.nopeeking.fabric;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPeeking implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(NoPeeking.class);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Logger added");
    }
}
