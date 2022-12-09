package com.mattborle.cupsarsenal.client;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.entities.EndRodArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EndRodArrowRenderer extends ArrowRenderer<EndRodArrowEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(CupsArsenal.MOD_ID, "textures/entity/endrod_arrow.png");

    public EndRodArrowRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(EndRodArrowEntity arrow) {
        return TEXTURE;
    }
}