package com.mattborle.cupsarsenal.client.triggers;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.registry.ItemRegistry;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


/**
 * Example, how to trigger animations on specific players
 * Always trigger animation on client-side.  Maybe as a response to a network packet or event
 */
@Mod.EventBusSubscriber(modid = CupsArsenal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerAnimationTrigger {
    Item heldItem;

    @SubscribeEvent
    public void onHeldItemChanged(TickEvent.PlayerTickEvent event){//TODO: of course this is bad, we will need a mixin to detect changed held item
        CupsArsenal.LOGGER.debug("1");
        var player = Minecraft.getInstance().player;

        if (player == null) return;
        CupsArsenal.LOGGER.debug("2");
        if(Minecraft.getInstance().player.getInventory().getSelected() == null) return;
        CupsArsenal.LOGGER.debug("3");

        heldItem = Minecraft.getInstance().player.getInventory().getSelected().getItem();
        CupsArsenal.LOGGER.debug("4");
        // Get the animations for the player, if this fails, print an error and return without crashing.
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(CupsArsenal.MOD_ID, "animation"));
        if (animation == null) {
            CupsArsenal.LOGGER.error("Cup's Arsenal - Missing player animation reference @ PlayerAnimationTrigger.java. Please report this and what you did before this error was logged.");
            return;
        }
        CupsArsenal.LOGGER.debug("5");
        // Animation selection:
        if (heldItem == ItemRegistry.MAGMA_SPEWER.get())
        {
            animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("cupsarsenal", "animation.model.gun.large.low.idle"))));
            CupsArsenal.LOGGER.debug("6");
        }
        CupsArsenal.LOGGER.debug("7");
    }
}