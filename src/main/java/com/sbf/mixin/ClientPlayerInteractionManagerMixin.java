package com.sbf.mixin;

import com.sbf.feature.Lasso;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "interactItem", at = @At("HEAD"), cancellable = true)
    private void interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if (Lasso.blockClick){
            cir.cancel();
        }
        else {
            Lasso.clicked();
        }
    }
}
