package com.sbf.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.block.ShapeContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowBlock.class)
public abstract class SnowBlockMixin {

    /**
     * In vanilla 1.21+, 1-layer snow has no collision (VoxelShapes.empty()).
     * In 1.8.9, 1-layer snow had a zero-height collision box, which still affected stacked collision merging.
     * This overwrite restores that old behavior.
     */

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    protected void getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        int layers = state.get(SnowBlock.LAYERS);
        if (layers == 1) {
            cir.setReturnValue(VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D));
        }
    }
}
