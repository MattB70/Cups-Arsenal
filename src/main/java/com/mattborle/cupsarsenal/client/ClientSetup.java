package com.mattborle.cupsarsenal.client;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.init.EntityInit;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CupsArsenal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityInit.BLAZEROD_ARROW.get(), BlazeRodArrowRenderer::new);
        EntityRenderers.register(EntityInit.ENDROD_ARROW.get(), EndRodArrowRenderer::new);
        EntityRenderers.register(EntityInit.LEADROD_ARROW.get(), LeadRodArrowRenderer::new);
        EntityRenderers.register(EntityInit.URANIUMROD_ARROW.get(), UraniumRodArrowRenderer::new);
    }
}