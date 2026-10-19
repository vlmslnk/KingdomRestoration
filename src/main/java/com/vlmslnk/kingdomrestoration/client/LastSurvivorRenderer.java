package com.vlmslnk.kingdomrestoration.client;

import com.vlmslnk.kingdomrestoration.KingdomRestoration;
import com.vlmslnk.kingdomrestoration.entity.LastSurvivorEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LastSurvivorRenderer
        extends MobRenderer<LastSurvivorEntity, HumanoidModel<LastSurvivorEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomRestoration.MOD_ID,
                    "textures/entity/last_survivor.png"
            );

    public LastSurvivorRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(LastSurvivorEntity entity) {
        return TEXTURE;
    }
}
