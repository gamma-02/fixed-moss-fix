package gamma02.mossfix.features;

import com.mojang.serialization.Codec;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPatchFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class FixedMossVegatationPatchFeature extends Feature<VegetationPatchFeatureConfig> {
    public FixedMossVegatationPatchFeature(Codec<VegetationPatchFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<VegetationPatchFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        VegetationPatchFeatureConfig vegetationPatchFeatureConfig = (VegetationPatchFeatureConfig)context.getConfig();
        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();
        Predicate<BlockState> predicate = (state) -> {
            return state.isIn(vegetationPatchFeatureConfig.replaceable);
        };
        int xRadius = vegetationPatchFeatureConfig.horizontalRadius.get(random) + 1;
        int zRaidus = vegetationPatchFeatureConfig.horizontalRadius.get(random) + 1;
        Set<BlockPos> set = this.placeGroundAndGetPositions(structureWorldAccess, vegetationPatchFeatureConfig, random, blockPos, predicate, xRadius, zRaidus);
        this.generateVegetation(context, structureWorldAccess, vegetationPatchFeatureConfig, random, set, xRadius, zRaidus);
        return !set.isEmpty();
    }

    protected Set<BlockPos> placeGroundAndGetPositions(StructureWorldAccess world, VegetationPatchFeatureConfig config, Random random, BlockPos pos, Predicate<BlockState> replaceable, int radiusX, int radiusZ) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        BlockPos.Mutable mutable2 = mutable.mutableCopy();
        Direction direction = config.surface.getDirection();
        Direction direction2 = direction.getOpposite();
        Set<BlockPos> set = new HashSet();

        for(int xRaidusIterator = -radiusX; xRaidusIterator <= radiusX; ++xRaidusIterator) {
            boolean reachedEndOrBeginnnng = xRaidusIterator == -radiusX || xRaidusIterator == radiusX;

            for(int zRaidusIterator = -radiusZ; zRaidusIterator <= radiusZ; ++zRaidusIterator) {
                boolean bl2 = zRaidusIterator == -radiusZ || zRaidusIterator == radiusZ;
                boolean bl3 = reachedEndOrBeginnnng || bl2;
                boolean bl4 = reachedEndOrBeginnnng && bl2;
                boolean bl5 = bl3 && !bl4;
                if (!bl4 && (!bl5 || config.extraEdgeColumnChance != 0.0F && !(random.nextFloat() > config.extraEdgeColumnChance))) {
                    mutable.set((Vec3i)pos, xRaidusIterator, 0, zRaidusIterator);

                    int k;
                    for(k = 0; canGenerateUnderBlock(world, mutable) && k < config.verticalRange; ++k) {
                        mutable.move(direction);
                    }

                    for(k = 0; !canGenerateUnderBlock(world, mutable) && k < config.verticalRange; ++k) {
                        mutable.move(direction2);
                    }
                    System.out.println(canGenerateUnderBlock(world, mutable) + " :generate, block: " + world.getBlockState(mutable));


                    mutable2.set(mutable, (Direction)config.surface.getDirection());
                    BlockState blockState = world.getBlockState(mutable2);
                    if (
                            canGenerateUnderBlock(world, mutable) &&
                            /*blockState.isSideSolidFullSquare(world, mutable2, config.surface.getDirection().getOpposite())*/ true) {
                        int l = config.depth.get(random) + (config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance ? 1 : 0);
                        BlockPos blockPos = mutable2.toImmutable();
                        boolean bl6 = this.placeGround(world, config, replaceable, random, mutable2, l);
                        if (bl6) {
                            set.add(blockPos);
                        }
                    }
                }
            }
        }

        return set;
    }

    protected void generateVegetation(FeatureContext<VegetationPatchFeatureConfig> context, StructureWorldAccess world, VegetationPatchFeatureConfig config, Random random, Set<BlockPos> positions, int radiusX, int radiusZ) {
        Iterator var8 = positions.iterator();

        while(var8.hasNext()) {
            BlockPos blockPos = (BlockPos)var8.next();
            if (config.vegetationChance > 0.0F && random.nextFloat() < config.vegetationChance) {
                this.generateVegetationFeature(world, config, context.getGenerator(), random, blockPos);
            }
        }

    }

    protected boolean generateVegetationFeature(StructureWorldAccess world, VegetationPatchFeatureConfig config, ChunkGenerator generator, Random random, BlockPos pos) {
        if(world.getBlockState(pos.offset(config.surface.getDirection().getOpposite())).isAir()) {
            return ((PlacedFeature) config.vegetationFeature.value()).generateUnregistered(world, generator, random, pos.offset(config.surface.getDirection().getOpposite()));
        }
        return false;
    }

    protected boolean placeGround(StructureWorldAccess world, VegetationPatchFeatureConfig config, Predicate<BlockState> replaceable, Random random, BlockPos.Mutable pos, int depth) {
        for(int i = 0; i < depth; ++i) {
            BlockState blockState = config.groundState.getBlockState(random, pos);
            BlockState blockState2 = world.getBlockState(pos);
            if (!blockState.isOf(blockState2.getBlock())) {
                if (!replaceable.test(blockState2)) {
                    return i != 0;
                }

                world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
                pos.move(config.surface.getDirection());
            }
        }

        return true;
    }

    private static boolean canGenerateUnderBlock(BlockView world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).isSolidBlock(world, blockPos);
    }
}
