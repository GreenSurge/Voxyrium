package me.cortex.voxy.client.mixin.minecraft;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import me.cortex.voxy.client.core.VoxyTerrainRendering;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinTerrainRenderPasses {
    // 26.3's caller owns the terrain pass. Opening Voxy's passes inside Sodium's draw would nest them.
    @Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE",
            target = "Lcom/mojang/renderpearl/api/commands/RenderPass;close()V", ordinal = 0, shift = At.Shift.AFTER))
    private void voxy$afterTerrainPass(GpuBufferSlice fog, boolean useOit, ChunkSectionsToRender sections,
                                     FeatureRenderDispatcher.PreparedFrame features, boolean seeThrough,
                                     boolean alwaysOnTop, CallbackInfo ci) {
        VoxyTerrainRendering.renderOpaque();
        if (!useOit) VoxyTerrainRendering.renderTranslucent();
    }

    @Inject(method = "executeOit", at = @At("TAIL"))
    private void voxy$afterOitComposite(CallbackInfo ci) {
        VoxyTerrainRendering.renderTranslucent();
    }
}
