package gamma02.mossfix.mixin;

import gamma02.mossfix.features.FixedMossVegatationPatchFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.MossBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoneMealItem.class)
public class BonemealMixin {

    @Redirect(method = "useOnFertilizable", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Fertilizable;isFertilizable(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Z"))
    private static boolean isFertializable(Fertilizable instance, BlockView blockView, BlockPos blockPos, BlockState blockState, boolean b){
        if(instance instanceof MossBlock){
            return true;
        }else{
            return instance.isFertilizable(blockView, blockPos, blockState, b);
        }
    }


}
