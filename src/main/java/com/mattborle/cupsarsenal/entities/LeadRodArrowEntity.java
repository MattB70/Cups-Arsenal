package com.mattborle.cupsarsenal.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.item.Items;
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

public class LeadRodArrowEntity extends AbstractArrow{

    // ADJUSTERS === Change these to edit stats ========================================================================
    private static final float  DMG_DIRECT_IMPACT = 18.0f;          // Damage to apply to entities upon impact
    private static final ParticleOptions FLAME_PARTICLE = ParticleTypes.CRIT;
    private static final ParticleOptions SMOKE_PARTICLE = ParticleTypes.SMOKE;

    // CONSTRUCTORS ====================================================================================================
    public LeadRodArrowEntity(EntityType<LeadRodArrowEntity> entityType, Level world){
        super(entityType, world);
    }
    public LeadRodArrowEntity(EntityType<LeadRodArrowEntity> entityType, double x, double y, double z, Level world){
        super(entityType, x, y, z, world);
    }
    public LeadRodArrowEntity(EntityType<LeadRodArrowEntity> entityType, LivingEntity shooter, Level world){
        super(entityType, shooter, world);
    }

    // METHODS =========================================================================================================
    @Override
    protected ItemStack getPickupItem(){
        return new ItemStack(Items.IRON_INGOT); // TODO: replace with lead rod item from mod
    }
    @Override
    protected void onHitEntity(EntityHitResult ray){
        // play additional "piercing" sound
        ray.getEntity().hurt(DamageSource.GENERIC, DMG_DIRECT_IMPACT); // Damage entity with direct impact
    }
    @Override
    protected void onHitBlock(BlockHitResult ray){
        playEffects(8, 1.0f);
        super.onHitBlock(ray); // stick in block
    }
    @Override
    protected void tickDespawn(){
        super.tickDespawn();
    }
    @Override
    public void tick(){
        super.tick();
        // Flames
        Random r = new Random();
        if(!this.inGround){ // Display particles whilst flying
            level.addParticle(FLAME_PARTICLE, this.getX(), this.getY(), this.getZ(),
                    (-0.1f + r.nextFloat() * (0.2f)),
                    (-0.1f + r.nextFloat() * (0.2f)),
                    (-0.1f + r.nextFloat() * (0.2f)));
        }
    }
    @Override
    public Packet<?> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // HELPERS =========================================================================================================
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
            level.addParticle(FLAME_PARTICLE, this.getX(), this.getY(), this.getZ(),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)),
                    vScale * (-0.1f + r.nextFloat() * (0.2f)));
            // Smoke
            level.addParticle(SMOKE_PARTICLE, this.getX(), this.getY(), this.getZ(),
                    vScale * (-0.15f + r.nextFloat() * (0.3f)),
                    vScale * (-0.15f + r.nextFloat() * (0.3f)),
                    vScale * (-0.15f + r.nextFloat() * (0.3f)));
        }
    }
}
