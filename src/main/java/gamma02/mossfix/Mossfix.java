package gamma02.mossfix;

import gamma02.mossfix.features.FixedMossVegatationPatchFeature;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import static net.minecraft.world.gen.feature.UndergroundConfiguredFeatures.MOSS_VEGETATION;


public class Mossfix implements ModInitializer {
    public static Feature<VegetationPatchFeatureConfig> FIXED_MOSS_PATCH_FEATURE = Registry.register(Registry.FEATURE, "fixed_moss_patch_feature", new FixedMossVegatationPatchFeature(VegetationPatchFeatureConfig.CODEC));;
    public static RegistryEntry<ConfiguredFeature<VegetationPatchFeatureConfig, ?>> FIXED_MOSS_PATCH_CONFIGURED;

    @Override
    public void onInitialize() {
        FIXED_MOSS_PATCH_CONFIGURED = ConfiguredFeatures.register("fixed_moss_patch_bonemeal", FIXED_MOSS_PATCH_FEATURE, new VegetationPatchFeatureConfig(BlockTags.MOSS_REPLACEABLE, BlockStateProvider.of(Blocks.MOSS_BLOCK), PlacedFeatures.createEntry(MOSS_VEGETATION), VerticalSurfaceType.FLOOR, ConstantIntProvider.create(1), 0.0F, 5, 0.6F, UniformIntProvider.create(1, 2), 0.75F));

    }


}
