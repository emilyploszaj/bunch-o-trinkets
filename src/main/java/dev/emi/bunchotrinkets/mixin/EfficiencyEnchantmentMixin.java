package dev.emi.bunchotrinkets.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.bunchotrinkets.BunchOTrinketsMain;
import net.minecraft.enchantment.EfficiencyEnchantment;
import net.minecraft.item.ItemStack;

/**
 * Allow the mining glove to be enchantable with efficiency
 */
@Mixin(EfficiencyEnchantment.class)
public abstract class EfficiencyEnchantmentMixin {

	@Inject(at = @At("HEAD"), method = "isAcceptableItem", cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		if(stack.getItem() == BunchOTrinketsMain.MINING_GLOVE) info.setReturnValue(true);
	}
}