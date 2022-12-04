package shateq.nopeeking.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shateq.nopeeking.fabric.NoPeeking;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    // TODO not cir return, but allow to check if should (hell you know)
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(@NotNull T entity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
        // TODO: problem, does not include optimization
        if (!NoPeeking.SETTINGS.functional()) return;

        EntityType<?> type = entity.getType();
        boolean forPlayer = type == EntityType.PLAYER;

        // Render LocalPlayer when renderSElf even if entity blacklisted
        if (NoPeeking.SETTINGS.renderSelf() && type == EntityType.PLAYER) {
            if (Minecraft.getInstance().player != null) {
                var matches = Minecraft.getInstance().player.getUUID().equals(entity.getUUID());

                cir.setReturnValue(matches);
            }
        }

//        var contains = NoPeeking.peeking.blacklist.contains(type); //not
//        cir.setReturnValue(!contains);
//
//        var whitelist = contains && NoPeeking.peeking.as_whitelist; //not
//        cir.setReturnValue(whitelist);
    }
}
