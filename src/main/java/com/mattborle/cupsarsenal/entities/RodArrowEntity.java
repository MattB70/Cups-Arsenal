package com.mattborle.cupsarsenal.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

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
        // null, x, y, z, sound, source, volume, pitch
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.lava.extinguish")), SoundSource.PLAYERS, (float) 0.8f, 1.0f);
        // particle, x, y, z, ?, ?, ?
        level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), -0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.1f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, -0.1f);
        super.onHitEntity(ray); // damage the entity by the vanilla amount
        // this, x, y, z, explosionStrength, setsFires, breakMode
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, true, Explosion.BlockInteraction.BREAK);
        if (!level.isClientSide()){ // Spawn Fire
            FallingBlockEntity.fall(level, new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.FIRE.defaultBlockState());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult ray) {
        // null, x, y, z, sound, source, volume, pitch
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.lava.extinguish")), SoundSource.PLAYERS, (float) 0.8f, 1.0f);
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.creeper.primed")), SoundSource.PLAYERS, (float) 0.5f, 1.5f);
        // particle, x, y, z, velocity_x, velocity_y, velocity_z
        level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), -0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.1f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, -0.1f);
        super.onHitBlock(ray); // stick in block
        //BlockState blockHit = this.level.getBlockState(ray.getBlockPos());
    }

    @Override
    protected void tickDespawn() {
        if (this.inGroundTime > 20){
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, true, Explosion.BlockInteraction.BREAK);
            if (!level.isClientSide()){ // Spawn Falling Fire
                FallingBlockEntity.fall(level, new BlockPos(this.getX(), this.getY()+4, this.getZ()), Blocks.FIRE.defaultBlockState());
            }
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f, 0.0f);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
