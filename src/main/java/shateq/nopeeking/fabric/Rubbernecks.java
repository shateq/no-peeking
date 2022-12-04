package shateq.nopeeking.fabric;

import blue.endless.jankson.Comment;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityType! it was here all that time!
 */
@Modmenu(modId = "nopeeking")
@Config(name = "nopeeking", wrapperName = "NoPeekingSettings")
public class Rubbernecks {
    @Comment("Is mod enabled? Should mod filter render requests?")
    public boolean functional = true;
    @Comment("Should local player be rendered even despite blocking?")
    public boolean renderSelf = false;
    @Comment("Do we have to treat blacklist as whitelist?")
    public boolean asWhitelist = false;
    @Comment("List entities banned from rendering.")
    public List<EntityType<?>> blacklist = new ArrayList<>();
}
