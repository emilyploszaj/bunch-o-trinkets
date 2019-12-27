package dev.emi.bunchotrinkets.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.bunchotrinkets.BunchOTrinketsMain;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

/**
 * Rocket aglet right click behavior
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow
	public ClientPlayerEntity player;
	
	@Inject(at = @At("TAIL"), method = "doItemUse")
	public void doItemUse(CallbackInfo info) {
		if (player.isFallFlying()) {
			TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
			ItemStack stack = comp.getStack(SlotGroups.FEET, Slots.AGLET);
			if (!stack.isEmpty() && stack.getItem() == BunchOTrinketsMain.ROCKET_AGLET && !player.getItemCooldownManager().isCoolingDown(BunchOTrinketsMain.ROCKET_AGLET)) {
				player.getItemCooldownManager().set(BunchOTrinketsMain.ROCKET_AGLET, 20);
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(BunchOTrinketsMain.ROCKET_AGLET_PACKET, buf);
				MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
			}
		}
	}
}