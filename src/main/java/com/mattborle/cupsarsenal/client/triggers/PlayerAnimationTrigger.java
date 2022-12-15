package com.mattborle.cupsarsenal.client.triggers;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.registry.ItemRegistry;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.Objects;


/**
 * Example, how to trigger animations on specific players
 * Always trigger animation on client-side.  Maybe as a response to a network packet or event
 */
@Mod.EventBusSubscriber(modid = CupsArsenal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerAnimationTrigger {
    Item heldItem;

    @SubscribeEvent
    public void onHeldItemChanged(TickEvent.PlayerTickEvent event){//TODO: of course this is bad, we will need a mixin to detect changed held item
        var player = Minecraft.getInstance().player;

        if (player == null) return;

        heldItem = Minecraft.getInstance().player.getMainHandItem().getItem();
        // Get the animations for the player, if this fails, print an error and return without crashing.
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(CupsArsenal.MOD_ID, "animation"));
        if (animation == null) {
            CupsArsenal.LOGGER.error("Cup's Arsenal - Missing player animation reference @ PlayerAnimationTrigger.java. Please report this and what you did before this error was logged.");
            return;
        }
        // Animation selection:
        if (heldItem == ItemRegistry.MAGMA_SPEWER.get())
        {
            animation.setAnimation(new KeyframeAnimationPlayer(Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(new ResourceLocation("cupsarsenal", "animation.model.gun.large.low.idle")))));
        }
    }
}