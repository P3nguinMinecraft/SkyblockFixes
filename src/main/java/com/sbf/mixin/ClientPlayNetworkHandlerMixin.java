package com.sbf.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	private static final MinecraftClient client = MinecraftClient.getInstance();

	@Inject(method = "onBlockUpdate", at = @At("TAIL"))
	private void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
		if (client.world == null) return;

		for (Direction dir : Direction.Type.HORIZONTAL) {
			BlockPos neighborPos = packet.getPos().offset(dir);
			BlockState neighborState = client.world.getBlockState(neighborPos);
			if (neighborState.getBlock() instanceof net.minecraft.block.PaneBlock) {
				client.world.setBlockState(neighborPos, updateState(neighborPos, neighborState));
			}
		}
	}

	private BlockState updateState(BlockPos pos, BlockState state){
		int validConnect = 0;
		if (state.get(PaneBlock.NORTH)){
			if (client.world.getBlockState(pos.north()).isAir()){
				state = state.with(PaneBlock.NORTH, false);
			}
			else validConnect++;
		}
		if (state.get(PaneBlock.EAST)){
			if (client.world.getBlockState(pos.east()).isAir()) {
				state = state.with(PaneBlock.EAST, false);
			}
			else validConnect++;
		}
		if (state.get(PaneBlock.SOUTH)){
			if (client.world.getBlockState(pos.south()).isAir()){
				state = state.with(PaneBlock.SOUTH, false);
			}
			else validConnect++;
		}
		if (state.get(PaneBlock.WEST)){
			if (client.world.getBlockState(pos.west()).isAir()){
				state = state.with(PaneBlock.WEST, false);
			}
			else validConnect++;
		}
		if (validConnect == 0){
			state = state.with(PaneBlock.NORTH, true)
					.with(PaneBlock.EAST, true)
					.with(PaneBlock.SOUTH, true)
					.with(PaneBlock.WEST, true);
		}
		return state;
	}
}
