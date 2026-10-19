package com.vlmslnk.kingdomrestoration;

import net.minecraft.world.level.levelgen.structure.StructureType;

public final class ModStructureTypes {
    public static final StructureType<TerrainCheckedJigsawStructure>
        TERRAIN_CHECKED_JIGSAW = () -> TerrainCheckedJigsawStructure.CODEC;

    private ModStructureTypes() {
    }
}
