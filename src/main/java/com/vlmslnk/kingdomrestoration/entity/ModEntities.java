package com.vlmslnk.kingdomrestoration.entity;

import com.vlmslnk.kingdomrestoration.KingdomRestoration;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KingdomRestoration.MOD_ID);

    public static final RegistryObject<EntityType<LastSurvivorEntity>> LAST_SURVIVOR =
            ENTITY_TYPES.register("last_survivor", () ->
                    EntityType.Builder.<LastSurvivorEntity>of(
                            LastSurvivorEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.8F)
                            .build("kingdomrestoration:last_survivor"));

    private ModEntities() {
    }
}
