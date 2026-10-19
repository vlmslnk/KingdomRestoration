package com.vlmslnk.kingdomrestoration;

import com.vlmslnk.kingdomrestoration.entity.LastSurvivorEntity;
import com.vlmslnk.kingdomrestoration.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KingdomRestoration.MOD_ID)
public class KingdomRestoration {

    public static final String MOD_ID = "kingdomrestoration";

    public KingdomRestoration(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::registerStructureTypes);

        ModEntities.ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(this::registerEntityAttributes);
    }

    private void registerStructureTypes(RegisterEvent event) {
        event.register(
                Registries.STRUCTURE_TYPE,
                ResourceLocation.fromNamespaceAndPath(
                        MOD_ID, "terrain_checked_jigsaw"),
                () -> ModStructureTypes.TERRAIN_CHECKED_JIGSAW
        );
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.LAST_SURVIVOR.get(),
                LastSurvivorEntity.createAttributes().build()
        );
    }
}
