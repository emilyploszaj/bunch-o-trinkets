package dev.emi.bunchotrinkets.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.bunchotrinkets.BunchOTrinketsMain;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.world.World;

/**
 * Make mining glove modify breaking speed if no tool is being used
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("RETURN"), method = "getBlockBreakingSpeed", cancellable = true)
	public void getBlockBreakingSpeed(BlockState state, CallbackInfoReturnable<Float> info) {
		TrinketComponent comp = TrinketsApi.getTrinketComponent((PlayerEntity) (LivingEntity) this);
		ItemStack stack = comp.getStack(SlotGroups.HAND, Slots.GLOVES);
		ItemStack tool = getMainHandStack();
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
		if (!(tool.getItem() instanceof MiningToolItem) && stack.getItem() == BunchOTrinketsMain.MINING_GLOVE) {
			float f = info.getReturnValue();
			if (enchantments.containsKey(Enchantments.EFFICIENCY)) {
				int level = enchantments.get(Enchantments.EFFICIENCY);
				f += level * level + 1;
			} else f *= 1.5;
			info.setReturnValue(f);
		}
	}
}