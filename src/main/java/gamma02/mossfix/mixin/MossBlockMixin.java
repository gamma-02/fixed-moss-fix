package gamma02.mossfix.mixin;

import gamma02.mossfix.Mossfix;
import net.minecraft.block.BlockState;
import net.minecraft.block.MossBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures;
import net.minecraft.world.gen.feature.VegetationPatchFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MossBlock.class)
public class MossBlockMixin {

    /**
     * @author gamma_02
     * @reason Redirect wasn't working
     */
    @Overwrite
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        ( Mossfix.FIXED_MOSS_PATCH_CONFIGURED.value()).generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
    }
}
