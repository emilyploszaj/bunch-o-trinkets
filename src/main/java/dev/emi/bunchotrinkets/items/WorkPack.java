package dev.emi.bunchotrinkets.items;

import java.util.List;

import dev.emi.bunchotrinkets.BunchOTrinketsClient;
import dev.emi.bunchotrinkets.BunchOTrinketsMain;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.KeybindText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WorkPack extends Item implements ITrinket {

	public WorkPack() {
		super(new Settings().group(ItemGroup.TOOLS).maxCount(1));
		DispenserBlock.registerBehavior(this, TRINKET_DISPENSER_BEHAVIOR);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return ITrinket.equipTrinket(player, hand);
	}

	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext context) {
		list.add(new LiteralText("Swap your inventory by pressing ").formatted(Formatting.GOLD).append(new KeybindText(BunchOTrinketsClient.PACK_KEY.getLocalizedName()).formatted(Formatting.RED)));
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.CHEST) && slot.equals(Slots.BACKPACK);
	}

	public static void swapPack(PlayerEntity player, ItemStack stack) {
		if (stack.getItem() != BunchOTrinketsMain.WORK_PACK) return;
		CompoundTag tag = stack.getTag();
		CompoundTag newTag = new CompoundTag();
		if (tag != null && tag.contains("slots")) {
			tag = tag.getCompound("slots");
			for (int i = 0; i < 27; i++) {
				ItemStack beltStack = ItemStack.EMPTY;
				if (tag.contains("" + i)) {
					beltStack = ItemStack.fromTag(tag.getCompound("" + i));
				}
				newTag.put("" + i, player.inventory.getInvStack(9 + i).toTag(new CompoundTag()));
				player.inventory.setInvStack(9 + i, beltStack);
			}
		} else {
			for(int i = 0; i < 27; i++){
				newTag.put("" + i, player.inventory.getInvStack(9 + i).toTag(new CompoundTag()));
				player.inventory.setInvStack(9 + i, ItemStack.EMPTY);
			}
		}
		CompoundTag writeTag = new CompoundTag();
		writeTag.put("slots", newTag);
		stack.setTag(writeTag);
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}
}