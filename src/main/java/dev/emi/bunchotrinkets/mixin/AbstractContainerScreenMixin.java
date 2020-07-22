package dev.emi.bunchotrinkets.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.bunchotrinkets.BunchOTrinketsClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

/**
 * Let the work pack and work belt swaps be doable inside of inventories
 */
@Mixin(HandledScreen.class)
public abstract class AbstractContainerScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {

	protected AbstractContainerScreenMixin(Text text) {
		super(text);
	}

	@Inject(at = @At(value = "RETURN", ordinal = 2), method = "keyPressed", cancellable = true)
	public void keyPressed(int key, int scan, int modifiers, CallbackInfoReturnable<Boolean> info) {
		if (key == BunchOTrinketsClient.PACK_KEY.getDefaultKey().getCode()) {
			BunchOTrinketsClient.onPack();
		} else if (key == BunchOTrinketsClient.BELT_KEY.getDefaultKey().getCode()) {
			BunchOTrinketsClient.onBelt();
		}
	}
}