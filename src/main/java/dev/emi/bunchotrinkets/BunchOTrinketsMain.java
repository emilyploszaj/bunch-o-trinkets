package dev.emi.bunchotrinkets;

import dev.emi.bunchotrinkets.items.AgletOfTheTraveller;
import dev.emi.bunchotrinkets.items.BuildersBelt;
import dev.emi.bunchotrinkets.items.GluttonyAmulet;
import dev.emi.bunchotrinkets.items.MiningGlove;
import dev.emi.bunchotrinkets.items.RocketAglet;
import dev.emi.bunchotrinkets.items.WorkBelt;
import dev.emi.bunchotrinkets.items.WorkPack;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketSlots;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BunchOTrinketsMain implements ModInitializer {
	public static Item ROCKET_AGLET = registerItem("rocket_aglet", new RocketAglet());
	public static Item AGLET_OF_THE_TRAVELLER = registerItem("aglet_of_the_traveller", new AgletOfTheTraveller());
	public static Item MINING_GLOVE = registerItem("mining_glove", new MiningGlove());
	public static Item WORK_BELT = registerItem("work_belt", new WorkBelt());
	public static Item WORK_PACK = registerItem("work_pack", new WorkPack());
	public static Item GLUTTONY_AMULET = registerItem("gluttony_amulet", new GluttonyAmulet());
	public static Item BUILDERS_BELT = registerItem("builders_belt", new BuildersBelt());
	public static Identifier ROCKET_AGLET_PACKET = new Identifier("bunchotrinkets", "rocket_aglet");
	public static Identifier BELT_SWAP_PACKET = new Identifier("bunchotrinkets", "belt_swap");
	public static Identifier PACK_SWAP_PACKET = new Identifier("bunchotrinkets", "pack_swap");

	@Override
	public void onInitialize() {
		TrinketSlots.addSlot(SlotGroups.CHEST, Slots.NECKLACE, new Identifier("trinkets", "textures/item/empty_trinket_slot_necklace.png"));
		TrinketSlots.addSlot(SlotGroups.CHEST, Slots.BACKPACK, new Identifier("trinkets", "textures/item/empty_trinket_slot_backpack.png"));
		TrinketSlots.addSlot(SlotGroups.LEGS, Slots.BELT, new Identifier("trinkets", "textures/item/empty_trinket_slot_belt.png"));
		TrinketSlots.addSlot(SlotGroups.FEET, Slots.AGLET, new Identifier("trinkets", "textures/item/empty_trinket_slot_aglet.png"));
		TrinketSlots.addSlot(SlotGroups.HAND, Slots.GLOVES, new Identifier("trinkets", "textures/item/empty_trinket_slot_gloves.png"));
		ServerSidePacketRegistry.INSTANCE.register(ROCKET_AGLET_PACKET, (context, buffer) -> {
			PlayerEntity player = context.getPlayer();
			TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
			ItemStack stack = comp.getStack(SlotGroups.FEET, Slots.AGLET);
			if (player.isFallFlying() && !stack.isEmpty() && stack.getItem() == ROCKET_AGLET && !player.getItemCooldownManager().isCoolingDown(ROCKET_AGLET)) {
				player.getItemCooldownManager().set(stack.getItem(), 20);
				stack.damage(1, player, p ->{});
				player.getEntityWorld().spawnEntity(new FireworkRocketEntity(player.getEntityWorld(), ItemStack.EMPTY, player));
			}
		});
		ServerSidePacketRegistry.INSTANCE.register(BELT_SWAP_PACKET, (context, buffer) -> {
			PlayerEntity player = context.getPlayer();
			TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
			ItemStack stack = comp.getStack(SlotGroups.LEGS, Slots.BELT);
			if (!stack.isEmpty() && stack.getItem() == WORK_BELT) {
				WorkBelt.swapBelt(player, stack);
			}
		});
		ServerSidePacketRegistry.INSTANCE.register(PACK_SWAP_PACKET, (context, buffer) -> {
			PlayerEntity player = context.getPlayer();
			TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
			ItemStack stack = comp.getStack(SlotGroups.CHEST, Slots.BACKPACK);
			if (!stack.isEmpty() && stack.getItem() == WORK_PACK) {
				WorkPack.swapPack(player, stack);
			}
		});
	}

	private static <T extends Item> T registerItem(String name, T item){
		return Registry.register(Registry.ITEM, new Identifier("bunchotrinkets", name), item);
	}
}
