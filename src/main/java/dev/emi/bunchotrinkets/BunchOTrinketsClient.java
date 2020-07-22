package dev.emi.bunchotrinkets;

import org.lwjgl.glfw.GLFW;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public class BunchOTrinketsClient implements ClientModInitializer {
	public static KeyBinding BELT_KEY;
	public static KeyBinding PACK_KEY;
	public static boolean beltKeyDown;
	public static boolean packKeyDown;
	
	@Override
	public void onInitializeClient() {
		BELT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.bunchotrinkets.belt", GLFW.GLFW_KEY_B, "Bunch O' Trinkets"));
		PACK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.bunchotrinkets.pack", GLFW.GLFW_KEY_P, "Bunch O' Trinkets"));
		
		ClientTickCallback.EVENT.register(e -> {
			if (BELT_KEY.isPressed()) {
				if (!beltKeyDown) {
					onBelt();
				}
				beltKeyDown = true;
			} else {
				beltKeyDown = false;
			}
			if (PACK_KEY.isPressed()) {
				if (!packKeyDown) {
					onPack();
				}
				packKeyDown = true;
			} else {
				packKeyDown = false;
			}
		});
	}

	public static void onBelt() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
		ItemStack stack = comp.getStack(SlotGroups.LEGS, Slots.BELT);
		if (!stack.isEmpty() && stack.getItem() == BunchOTrinketsMain.WORK_BELT) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(BunchOTrinketsMain.BELT_SWAP_PACKET, buf);
			MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
		}
	}

	public static void onPack() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
		ItemStack stack = comp.getStack(SlotGroups.CHEST, Slots.BACKPACK);
		if (!stack.isEmpty() && stack.getItem() == BunchOTrinketsMain.WORK_PACK) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(BunchOTrinketsMain.PACK_SWAP_PACKET, buf);
			MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
		}
	}
}
