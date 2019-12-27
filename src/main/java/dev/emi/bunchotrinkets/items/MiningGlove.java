package dev.emi.bunchotrinkets.items;

import java.util.List;

import dev.emi.trinkets.api.ITrinket;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MiningGlove extends Item implements ITrinket {

	public MiningGlove() {
		super(new Settings().group(ItemGroup.TOOLS).maxCount(1));
		DispenserBlock.registerBehavior(this, TRINKET_DISPENSER_BEHAVIOR);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return ITrinket.equipTrinket(player, hand);
	}

	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext context) {
		list.add(new LiteralText("Break blocks without a tool a bit quicker").formatted(Formatting.GOLD));
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.HAND) && slot.equals(Slots.GLOVES);
	}
}