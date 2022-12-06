package com.mattborle.cupsarsenal.items;

import com.mattborle.cupsarsenal.entities.RodArrowEntity;
import com.mattborle.cupsarsenal.init.EntityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class Launcher extends Item {
    public Launcher(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // On use, get look direction vectors from the player
        Vec3 vec3 = player.getViewVector(1.0f);
        float velocity = 3.0f;
        // Create our projectile entity and shoot it
        if (!player.level.isClientSide()){
            RodArrowEntity arrow = new RodArrowEntity(EntityInit.EXPLOSIVE_ARROW.get(), player, player.level);
            arrow.setDeltaMovement(vec3.x*velocity, vec3.y*velocity, vec3.z*velocity); // shoot in the direction the player is looking
            player.level.addFreshEntity(arrow);
            player.level.playSound(null, player.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.blaze.shoot")), SoundSource.BLOCKS, 1, 1);
        }
        return super.use(world, player, hand);
    }
}