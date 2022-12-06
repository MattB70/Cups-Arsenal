package com.mattborle.cupsarsenal.client;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.entities.RodArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RodArrowRenderer extends ArrowRenderer<RodArrowEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(CupsArsenal.MOD_ID, "textures/entity/rod_arrow.png");

    public RodArrowRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(RodArrowEntity arrow) {
        return TEXTURE;
    }
}