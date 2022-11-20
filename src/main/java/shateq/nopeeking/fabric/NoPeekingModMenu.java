package shateq.nopeeking.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.network.chat.TranslatableComponent;

public class NoPeekingModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        ConfigBuilder builder = ConfigBuilder.create()
            .setTitle(new TranslatableComponent("title.nopeeking.config"));
        return parent -> builder.setParentScreen(parent).build();
    }
}
