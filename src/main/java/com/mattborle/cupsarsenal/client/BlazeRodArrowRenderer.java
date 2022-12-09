package com.mattborle.cupsarsenal.client;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.entities.BlazeRodArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BlazeRodArrowRenderer extends ArrowRenderer<BlazeRodArrowEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(CupsArsenal.MOD_ID, "textures/entity/rod_arrow.png");

    public BlazeRodArrowRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(BlazeRodArrowEntity arrow) {
        return TEXTURE;
    }
}