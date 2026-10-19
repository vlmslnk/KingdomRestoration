package com.vlmslnk.kingdomrestoration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

public final class TerrainCheckedJigsawStructure extends Structure {
    private static final int FOOTPRINT = 48;
    private static final int MAX_BELOW_ANCHOR = 2;
    private static final int MAX_ABOVE_ANCHOR = 3;

    public static final MapCodec<TerrainCheckedJigsawStructure> CODEC =
        RecordCodecBuilder.<TerrainCheckedJigsawStructure>mapCodec(instance ->
            instance.group(
                settingsCodec(instance),
                StructureTemplatePool.CODEC.fieldOf("start_pool")
                    .forGetter(s -> s.startPool),
                ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name")
                    .forGetter(s -> s.startJigsawName),
                Codec.intRange(0, 20).fieldOf("size")
                    .forGetter(s -> s.maxDepth),
                HeightProvider.CODEC.fieldOf("start_height")
                    .forGetter(s -> s.startHeight),
                Codec.BOOL.fieldOf("use_expansion_hack")
                    .forGetter(s -> s.useExpansionHack),
                Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap")
                    .forGetter(s -> s.projectStartToHeightmap),
                Codec.intRange(1, 128).fieldOf("max_distance_from_center")
                    .forGetter(s -> s.maxDistanceFromCenter),
                Codec.list(PoolAliasBinding.CODEC)
                    .optionalFieldOf("pool_aliases", List.of())
                    .forGetter(s -> s.poolAliases),
                DimensionPadding.CODEC
                    .optionalFieldOf("dimension_padding", DimensionPadding.ZERO)
                    .forGetter(s -> s.dimensionPadding),
                LiquidSettings.CODEC
                    .optionalFieldOf(
                        "liquid_settings",
                        LiquidSettings.APPLY_WATERLOGGING
                    )
                    .forGetter(s -> s.liquidSettings)
            ).apply(instance, TerrainCheckedJigsawStructure::new)
        );

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final List<PoolAliasBinding> poolAliases;
    private final DimensionPadding dimensionPadding;
    private final LiquidSettings liquidSettings;

    public TerrainCheckedJigsawStructure(
        StructureSettings settings,
        Holder<StructureTemplatePool> startPool,
        Optional<ResourceLocation> startJigsawName,
        int maxDepth,
        HeightProvider startHeight,
        boolean useExpansionHack,
        Optional<Heightmap.Types> projectStartToHeightmap,
        int maxDistanceFromCenter,
        List<PoolAliasBinding> poolAliases,
        DimensionPadding dimensionPadding,
        LiquidSettings liquidSettings
    ) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.poolAliases = poolAliases;
        this.dimensionPadding = dimensionPadding;
        this.liquidSettings = liquidSettings;
    }

    private boolean hasSuitableTerrain(GenerationContext context, int startX, int startZ) {
        var generator = context.chunkGenerator();
        var heightAccessor = context.heightAccessor();
        var randomState = context.randomState();

        int anchorY = generator.getFirstFreeHeight(
            startX, startZ, Heightmap.Types.WORLD_SURFACE_WG,
            heightAccessor, randomState
        );

        for (int x = startX; x < startX + FOOTPRINT; x++) {
            for (int z = startZ; z < startZ + FOOTPRINT; z++) {
                int y = generator.getFirstFreeHeight(
                    x, z, Heightmap.Types.WORLD_SURFACE_WG,
                    heightAccessor, randomState
                );

                if (y < anchorY - MAX_BELOW_ANCHOR
                    || y > anchorY + MAX_ABOVE_ANCHOR) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMinBlockX();
        int z = chunkPos.getMinBlockZ();

        if (!hasSuitableTerrain(context, x, z)) {
            return Optional.empty();
        }

        int y = startHeight.sample(
            context.random(),
            new WorldGenerationContext(
                context.chunkGenerator(),
                context.heightAccessor()
            )
        );

        BlockPos startPos = new BlockPos(x, y, z);

        return JigsawPlacement.addPieces(
            context,
            startPool,
            startJigsawName,
            maxDepth,
            startPos,
            useExpansionHack,
            projectStartToHeightmap,
            maxDistanceFromCenter,
            PoolAliasLookup.create(poolAliases, startPos, context.seed()),
            dimensionPadding,
            liquidSettings
        );
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.TERRAIN_CHECKED_JIGSAW;
    }
}
