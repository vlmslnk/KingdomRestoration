package com.vlmslnk.kingdomrestoration.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LastSurvivorEntity extends PathfinderMob {

    private static final double LOOK_DISTANCE = 6.0D;

    public LastSurvivorEntity(
            EntityType<? extends LastSurvivorEntity> type,
            Level level
    ) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D);
    }

    @Override
    protected void registerGoals() {
        // Выживший пока не ходит и не убегает.
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            Player player = level().getNearestPlayer(this, LOOK_DISTANCE);

            if (player != null) {
                lookAt(player, 30.0F, 30.0F);
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
