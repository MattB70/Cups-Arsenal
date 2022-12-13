package com.mattborle.cupsarsenal.client.renderers;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.entities.LeadRodArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LeadRodArrowRenderer extends ArrowRenderer<LeadRodArrowEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(CupsArsenal.MOD_ID, "textures/entity/leadrod_arrow.png");

    public LeadRodArrowRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(LeadRodArrowEntity arrow) {
        return TEXTURE;
    }
}