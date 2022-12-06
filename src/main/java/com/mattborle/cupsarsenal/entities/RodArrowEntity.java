package com.mattborle.cupsarsenal.entities;

import com.mattborle.cupsarsenal.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;

public class RodArrowEntity extends AbstractArrow {
    public RodArrowEntity(EntityType<RodArrowEntity> entityType, Level world) {
        super(entityType, world);
    }

    public RodArrowEntity(EntityType<RodArrowEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public RodArrowEntity(EntityType<RodArrowEntity> entityType, LivingEntity shooter, Level world) {
        super(entityType, shooter, world);
    }

    @Override
    protected ItemStack getPickupItem() {
        //return new ItemStack(Items.BLAZE_ROD);
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult ray) {
        super.onHitEntity(ray); // damage the entity by the vanilla amount
        // this, x, y, z, explosionStrength, setsFires, breakMode
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, false, Explosion.BlockInteraction.BREAK);
        if (!level.isClientSide()){ // Spawn Fire
            FallingBlockEntity.fall(level, new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.FIRE.defaultBlockState());
        }
    }



    @Override
    protected void onHitBlock(BlockHitResult ray) {
        super.onHitBlock(ray); // stick in block
        //BlockState blockHit = this.level.getBlockState(ray.getBlockPos());
    }

    @Override
    protected void tickDespawn() {
        if (this.inGroundTime > 30){
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, false, Explosion.BlockInteraction.BREAK);
            if (!level.isClientSide()){ // Spawn Fire
                FallingBlockEntity.fall(level, new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.FIRE.defaultBlockState());
            }
            this.discard();
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
