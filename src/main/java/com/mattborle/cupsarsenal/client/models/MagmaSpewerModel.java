package com.mattborle.cupsarsenal.client.models;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.items.MagmaSpewerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MagmaSpewerModel extends AnimatedGeoModel<MagmaSpewerItem> {
    @Override
    public ResourceLocation getModelLocation(MagmaSpewerItem object) {
        return new ResourceLocation(CupsArsenal.MOD_ID, "geo/magma_spewer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MagmaSpewerItem object) {
        return new ResourceLocation(CupsArsenal.MOD_ID, "textures/item/magma_spewer.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MagmaSpewerItem animatable) {
        return new ResourceLocation(CupsArsenal.MOD_ID, "animations/magma_spewer.animation.json");
    }
}
