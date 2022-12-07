package com.mattborle.cupsarsenal.init;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.entities.BlazeRodArrowEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// Entity registration using ReferredRegister. Entities are initialized here as to avoid the hassle of static referencing.
public class EntityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CupsArsenal.MOD_ID);

    public static final RegistryObject<EntityType<BlazeRodArrowEntity>> EXPLOSIVE_ARROW = ENTITY_TYPES.register("rod_arrow",
            () -> EntityType.Builder.of((EntityType.EntityFactory<BlazeRodArrowEntity>) BlazeRodArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("explosive_arrow"));
}