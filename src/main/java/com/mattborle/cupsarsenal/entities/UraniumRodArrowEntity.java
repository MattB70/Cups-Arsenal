package com.mattborle.cupsarsenal.entities;

import com.mattborle.cupsarsenal.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    private static final float  DMG_DIRECT_IMPACT = 22.0f;          // Damage to apply to entities upon impact
    private static final float  DMG_EXPLOSION_SIZE = 3.2f;          // Size of AEO damage causing explosion
    private static final float  FIRE_BREAK_EXPLOSION_SIZE = 1.0f;   // Size of block breaking/fire starting explosion
    private static final int    DECAY_DELAY = 300;                  // Ticks until the rod decays when stuck in ground
    private static final ParticleOptions SMOKE_PARTICLE = ParticleTypes.SPORE_BLOSSOM_AIR;
    private static final ParticleOptions FLAME_PARTICLE = ParticleTypes.SNEEZE;

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
        playEffects(8, 1.0f, true);
        // Damage entity with direct impact
        ray.getEntity().hurt(DamageSource.GENERIC, DMG_DIRECT_IMPACT);
        //this.discard(); skip the discard, allow the rod to continue piercing entities until it rests at a block.
    }
    @Override
    protected void onHitBlock(BlockHitResult ray){
        playEffects(8, 1.0f, false);
        super.onHitBlock(ray); // stick in block
    }
    @Override
    protected void tickDespawn(){
        if (this.inGroundTime > DECAY_DELAY){
            decay();
            this.discard();
        }
    }
    @Override
    public void tick(){
        super.tick();
        Random r = new Random();
        if (DECAY_DELAY - this.inGroundTime <= 40){ // "about to explode" fire spray
            level.addParticle(FLAME_PARTICLE, this.getX(), this.getY(), this.getZ(),
                    -0.15f + r.nextFloat() * (0.3f),
                    0.3f,
                    -0.15f + r.nextFloat() * (0.3f));
            level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()),
                    ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.burn")),
                    SoundSource.PLAYERS,
                    0.5f-(this.inGroundTime/DECAY_DELAY),
                    (float)this.inGroundTime/DECAY_DELAY);
        } else {
            level.addParticle(FLAME_PARTICLE, this.getX(), this.getY(), this.getZ(),
                    -0.1f + r.nextFloat() * (0.2f),
                    0.1f,
                    -0.1f + r.nextFloat() * (0.2f));
            level.addParticle(SMOKE_PARTICLE, this.getX(), this.getY(), this.getZ(),
                    -0.1f + r.nextFloat() * (0.2f),
                    0.2f,
                    -0.1f + r.nextFloat() * (0.2f));
        }
    }
    @Override
    public Packet<?> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // HELPERS =========================================================================================================
    private void decay(){
        if (!this.level.isClientSide) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            this.discard(); // discard uranium rod
            LeadRodArrowEntity leadRod = new LeadRodArrowEntity(EntityRegistry.LEADROD_ARROW.get(), x,y,z, this.level);
            leadRod.setDeltaMovement(0,0,0); // no movement.
            level.addFreshEntity(leadRod); // replace uranium rod with lead rod (decayed)
        }
    }
    //                       if entity hit,      velocity scalar
    private void playEffects(int numParticlesSets, float vScale, boolean hitEntity){
        // If the rod pierced and entity, play these effects and skip the rest
        if(hitEntity){
            // Play entity pierced sounds
            level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()),
                    ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.strong")),
                    SoundSource.PLAYERS,
                    0.8f,
                    1.0f);
            level.playSound(null, new BlockPos(this.getX(), this.getY(), this.getZ()),
                    ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.burn")),
                    SoundSource.PLAYERS,
                    0.8f,
                    0.8f);
            for(int i = 0; i < numParticlesSets; i++){
                Random r = new Random();
                // Entity hit particles
                level.addParticle(ParticleTypes.ENCHANTED_HIT, this.getX(), this.getY(), this.getZ(),
                        vScale * (-1f + r.nextFloat() * (2f)),
                        vScale * (-1f + r.nextFloat() * (2f)),
                        vScale * (-1f + r.nextFloat() * (2f)));
            }
        }
        // If the rod did not pierce an entity, play these effects
        else{
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
}
