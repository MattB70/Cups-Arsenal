package com.mattborle.cupsarsenal.client;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.client.renderers.BlazeRodArrowRenderer;
import com.mattborle.cupsarsenal.client.renderers.EndRodArrowRenderer;
import com.mattborle.cupsarsenal.client.renderers.LeadRodArrowRenderer;
import com.mattborle.cupsarsenal.client.renderers.UraniumRodArrowRenderer;
import com.mattborle.cupsarsenal.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CupsArsenal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityRegistry.BLAZEROD_ARROW.get(), BlazeRodArrowRenderer::new);
        EntityRenderers.register(EntityRegistry.ENDROD_ARROW.get(), EndRodArrowRenderer::new);
        EntityRenderers.register(EntityRegistry.LEADROD_ARROW.get(), LeadRodArrowRenderer::new);
        EntityRenderers.register(EntityRegistry.URANIUMROD_ARROW.get(), UraniumRodArrowRenderer::new);
    }
}