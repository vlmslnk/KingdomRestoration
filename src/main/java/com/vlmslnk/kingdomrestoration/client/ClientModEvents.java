package com.vlmslnk.kingdomrestoration.client;

import com.vlmslnk.kingdomrestoration.KingdomRestoration;
import com.vlmslnk.kingdomrestoration.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = KingdomRestoration.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class ClientModEvents {

    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.LAST_SURVIVOR.get(),
                LastSurvivorRenderer::new
        );
    }
}
