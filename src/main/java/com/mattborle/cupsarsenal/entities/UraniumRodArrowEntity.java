package com.mattborle.cupsarsenal.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.Random;

/*
BlazeRodArrowEntity Extends AbstractArrow; an explosive projectile to be fired out of a Launcher
    - Small block breaking, fire starting explosion
    - Large AOE explosion
    - High direct impact damage
    - Penetrates entities, exploding on impact and continuing to fly
*/

public class UraniumRodArrowEntity extends AbstractArrow{

    // ADJUSTERS === Change these to edit stats ========================================================================
    private static final float  DMG_DIRECT_IMPACT = 20.0f;          // Damage to apply to entities upon impact
    private static final float  DMG_EXPLOSION_SIZE = 3.2f;          // Size of AEO damage causing explosion
    private static final float  FIRE_BREAK_EXPLOSION_SIZE = 1.0f;   // Size of block breaking/fire starting explosion
    private static final int    EXPLOSION_DELAY = 60;               // Ticks until the rod explodes when stuck in ground

    // CONSTRUCTORS ====================================================================================================
    public UraniumRodArrowEntity(EntityType<UraniumRodArrowEntity> entityType, Level world){
        super(entityType, world);
    }
    public UraniumRodArrowEntity(EntityType<UraniumRodArrowEntity> entityType, double x, double y, double z, Level world){
        super(entityType, x, y, z, world);
    }
    public UraniumRodArrowEntity(EntityType<UraniumRodArrowEntity> entityType, LivingEntity shooter, Level world){
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
        // play additional "piercing" sound
        ray.getEntity().hurt(DamageSource.GENERIC, DMG_DIRECT_IMPACT); // Damage entity with direct impact
        explode();
    }
    @Override
    protected void onHitBlock(BlockHitResult ray){
        playEffects(8, 1.0f);
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
        Random r = new Random();
        if (EXPLOSION_DELAY - this.inGroundTime <= 20){ // "about to explode" fire spray
            level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(),
                    -0.15f + r.nextFloat() * (0.3f),
                    0.2f,
                    -0.15f + r.nextFloat() * (0.3f));
            level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()),
                    ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.burn")),
                    SoundSource.PLAYERS,
                    0.5f-(this.inGroundTime/EXPLOSION_DELAY),
                    (float)this.inGroundTime/EXPLOSION_DELAY);
        } else {
            level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(),
                    -0.04f + r.nextFloat() * (0.08f),
                    0.05f,
                    -0.04f + r.nextFloat() * (0.08f));
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(),
                    -0.04f + r.nextFloat() * (0.08f),
                    0.08f,
                    -0.04f + r.nextFloat() * (0.08f));
        }
    }
    @Override
    public Packet<?> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // HELPERS =========================================================================================================
    private void explode(){
        if (!this.level.isClientSide) {
            // small block breaking, fire starting explosion
            this.level.explode(this, this.getX(), this.getY(), this.getZ(),
                    FIRE_BREAK_EXPLOSION_SIZE,
                    true,
                    Explosion.BlockInteraction.BREAK);
            // large fire-less explosion
            this.level.explode(this, this.getX(), this.getY(), this.getZ(),
                    DMG_EXPLOSION_SIZE,
                    false,
                    Explosion.BlockInteraction.NONE);
            // falling fire on adjacent blocks, one block up.
            FallingBlockEntity.fall(level, new BlockPos(this.getX()-1, this.getY()+1, this.getZ()), Blocks.FIRE.defaultBlockState());
            FallingBlockEntity.fall(level, new BlockPos(this.getX()+1, this.getY()+1, this.getZ()), Blocks.FIRE.defaultBlockState());
            FallingBlockEntity.fall(level, new BlockPos(this.getX(), this.getY()+1, this.getZ()-1), Blocks.FIRE.defaultBlockState());
            FallingBlockEntity.fall(level, new BlockPos(this.getX(), this.getY()+1, this.getZ()+1), Blocks.FIRE.defaultBlockState());
            // explosion to launch the falling fire blocks
            this.level.explode(this, this.getX(), this.getY(), this.getZ(),
                    1.0f,
                    false,
                    Explosion.BlockInteraction.NONE);
        }
    }
    //                       if entity hit,      velocity scalar
    private void playEffects(int numParticlesSets, float vScale){
        // null, x, y, z, sound, source, volume, pitch
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()),
                ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.creeper.primed")),
                SoundSource.PLAYERS,
                0.8f,
                0.5f);
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()),
                ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.burn")),
                SoundSource.PLAYERS,
                0.8f,
                0.8f);
        // Play entity pierced sound
        level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()),
                ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.strong")),
                SoundSource.PLAYERS,
                0.8f,
                1.0f);
        for(int i = 0; i < numParticlesSets; i++){
            Random r = new Random();
            // Entity hit particles
            level.addParticle(ParticleTypes.ENCHANTED_HIT, this.getX(), this.getY(), this.getZ(),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)));
        }
        // spawn particles with random vectors
        for(int i = 0; i < numParticlesSets; i++){
            Random r = new Random();
            // particle, x, y, z, vx, vy, vz
            // Flames
            level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)));
            // Smoke
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(),
                    vScale * (-0.15f + r.nextFloat() * (0.3f)),
                    vScale * (-0.15f + r.nextFloat() * (0.3f)),
                    vScale * (-0.15f + r.nextFloat() * (0.3f)));
            // Ash from above
            level.addParticle(ParticleTypes.ASH, this.getX(), this.getY()+1, this.getZ(),
                    vScale * (-0.005f + r.nextFloat() * (0.01f)),
                    vScale * (-0.1f + r.nextFloat() * (-0.01f)),
                    vScale * (-0.005f + r.nextFloat() * (0.01f)));
            // Ash at impact location
            level.addParticle(ParticleTypes.ASH, this.getX(), this.getY(), this.getZ(),
                    vScale * (-0.005f + r.nextFloat() * (0.01f)),
                    vScale * (-0.1f + r.nextFloat() * (-0.01f)),
                    vScale * (-0.005f + r.nextFloat() * (0.01f)));
        }
    }
}
