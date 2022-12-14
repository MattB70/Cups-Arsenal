package com.mattborle.cupsarsenal.client.renderers;

import com.mattborle.cupsarsenal.client.models.MagmaSpewerModel;
import com.mattborle.cupsarsenal.items.MagmaSpewerItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MagmaSpewerRenderer extends GeoItemRenderer<MagmaSpewerItem> {

    public MagmaSpewerRenderer() {
        super(new MagmaSpewerModel());
    }
}