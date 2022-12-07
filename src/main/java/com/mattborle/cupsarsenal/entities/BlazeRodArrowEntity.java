package com.mattborle.cupsarsenal.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

/*
BlazeRodArrowEntity Extends AbstractArrow; an explosive projectile to be fired out of a Launcher
    - Small block breaking, fire starting explosion
    - Large AOE explosion
    - High direct impact damage
    - Penetrates entities, exploding on impact and continuing to fly
*/

public class BlazeRodArrowEntity extends AbstractArrow{

    // ADJUSTERS === Change these to edit stats ========================================================================
    private static final float  DMG_DIRECT_IMPACT = 20.0f;          // Damage to apply to entities upon impact
    private static final float  DMG_EXPLOSION_SIZE = 2.8f;          // Size of AEO damage causing explosion
    private static final float  FIRE_BREAK_EXPLOSION_SIZE = 0.8f;   // Size of block breaking/fire starting explosion
    private static final int    EXPLOSION_DELAY = 20;               // Ticks until the rod explodes when stuck in ground

    // CONSTRUCTORS ====================================================================================================
    public BlazeRodArrowEntity(EntityType<BlazeRodArrowEntity> entityType, Level world){
        super(entityType, world);
    }
    public BlazeRodArrowEntity(EntityType<BlazeRodArrowEntity> entityType, double x, double y, double z, Level world){
        super(entityType, x, y, z, world);
    }
    public BlazeRodArrowEntity(EntityType<BlazeRodArrowEntity> entityType, LivingEntity shooter, Level world){
        super(entityType, shooter, world);
    }

    // METHODS =========================================================================================================
    @Override
    protected ItemStack getPickupItem(){
        //return new ItemStack(Items.BLAZE_ROD);
        return ItemStack.EMPTY;
    }
    @Override
    protected void onHitEntity(EntityHitResult ray){
        impactParticles(); // display impact particles
        ray.getEntity().hurt(DamageSource.GENERIC, DMG_DIRECT_IMPACT); // Damage entity with direct impact
        explode();
    }
    @Override
    protected void onHitBlock(BlockHitResult ray){
        impactParticles(); // display impact particles
        super.onHitBlock(ray); // stick in block
    }
    @Override
    protected void tickDespawn(){
        if (this.inGroundTime > EXPLOSION_DELAY){
            explode();
            this.discard();
        }
    }
    @Override
    public void tick(){
        super.tick();
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.05f, 0.0f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0f, 0.08f, 0.0f);
    }
    @Override
    public Packet<?> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // HELPERS =========================================================================================================
    private void explode(){
        if (!this.level.isClientSide) {
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), FIRE_BREAK_EXPLOSION_SIZE, true, Explosion.BlockInteraction.BREAK); // small block breaking, fire starting explosion
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), DMG_EXPLOSION_SIZE, false, Explosion.BlockInteraction.NONE); // large fire-less explosion
        }
    }
    private void impactParticles(){
        // null, x, y, z, sound, source, volume, pitch
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.creeper.primed")), SoundSource.PLAYERS, (float) 0.8f, 0.5f);
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.burn")), SoundSource.PLAYERS, (float) 0.8f, 0.8f);
        // Forgot how to do the multi-particle spawning. It ain't pretty, but neither am I so who am I kidding.
        // particle, x, y, z, vx, vy, vz
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.0f); // up
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), -0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.1f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, -0.1f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, -0.1f, 0.0f); // down
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.1f, -0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), -0.1f, -0.1f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, -0.1f, 0.1f);
        level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0f, -0.1f, -0.1f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.0f); // up
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), -0.1f, 0.1f, 0.0f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, 0.1f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0f, 0.1f, -0.1f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0f, -0.1f, 0.0f); // down
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.1f, -0.1f, 0.0f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), -0.1f, -0.1f, 0.0f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0f, -0.1f, 0.1f);
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0f, -0.1f, -0.1f);
    }
}
