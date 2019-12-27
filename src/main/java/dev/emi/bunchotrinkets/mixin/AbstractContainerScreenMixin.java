package dev.emi.bunchotrinkets.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.bunchotrinkets.BunchOTrinketsClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.ContainerProvider;
import net.minecraft.container.Container;
import net.minecraft.text.Text;

/**
 * Let the work pack and work belt swaps be doable inside of inventories
 */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends Container> extends Screen implements ContainerProvider<T> {

	protected AbstractContainerScreenMixin(Text text) {
		super(text);
	}

	@Inject(at = @At(value = "RETURN", ordinal = 1), method = "keyPressed", cancellable = true)
	public void keyPressed(int key, int x, int y, CallbackInfoReturnable<Boolean> info) {
		if (key == BunchOTrinketsClient.PACK_KEY.getBoundKey().getKeyCode()) {
			BunchOTrinketsClient.onPack();
		} else if (key == BunchOTrinketsClient.BELT_KEY.getBoundKey().getKeyCode()) {
			BunchOTrinketsClient.onBelt();
		}
	}
}