package com.mattborle.cupsarsenal.client.renderers;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.entities.UraniumRodArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class UraniumRodArrowRenderer extends ArrowRenderer<UraniumRodArrowEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(CupsArsenal.MOD_ID, "textures/entity/uraniumrod_arrow.png");

    public UraniumRodArrowRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(UraniumRodArrowEntity arrow) {
        return TEXTURE;
    }
}