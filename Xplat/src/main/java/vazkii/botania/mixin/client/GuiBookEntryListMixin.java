package vazkii.botania.mixin.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.handler.DogEnabler;
import vazkii.patchouli.client.book.gui.GuiBookEntryList;
import vazkii.patchouli.client.book.gui.button.GuiButtonEntry;

@Mixin(GuiBookEntryList.class)
public class GuiBookEntryListMixin {
	@Inject(method = "handleButtonEntry", at = @At("HEAD"), cancellable = true)
	private void handleDogClick(Button button, CallbackInfo ci) {
		if (!Screen.hasShiftDown() && button instanceof GuiButtonEntry entryButton
				&& DogEnabler.isDog(entryButton.getEntry().getId())
				&& entryButton instanceof DogEnabler dogEnabler) {
			dogEnabler.botania_enableDog();
			ci.cancel();
		}
	}
}
