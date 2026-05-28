package vazkii.botania.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.handler.DogEnabler;
import vazkii.botania.network.serverbound.IndexStringRequestPacket;
import vazkii.botania.xplat.ClientXplatAbstractions;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.button.GuiButtonEntry;

@Mixin(GuiButtonEntry.class)
public abstract class GuiButtonEntryMixin extends Button implements DogEnabler {
	@Unique
	private static final ResourceLocation BOTANIA_DOG_TEXTURE = ResourceLocation.parse(ResourcesLib.GUI_DOG);

	@Unique
	private float botania_dogPos;

	@Unique
	private boolean botania_enabledDog;

	protected GuiButtonEntryMixin(int x, int y, int width, int height, Component message,
			OnPress onPress, CreateNarration createNarration) {
		super(x, y, width, height, message, onPress, createNarration);
	}

	@Inject(method = "renderWidget", at = @At("HEAD"))
	private void updateDogPos(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (botania_enabledDog) {
			botania_dogPos += Minecraft.getInstance().getTimer().getRealtimeDeltaTicks() * 10;
			setX(Math.max(getX(), (int) botania_dogPos + 10));
		}
	}

	@Inject(method = "renderWidget", at = @At("TAIL"))
	private void renderDog(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (botania_enabledDog) {
			graphics.blit(BOTANIA_DOG_TEXTURE, (int) botania_dogPos, getY(), botania_dogPos % 100 < 50 ? 23 : 0, 0, 23, 19, 64, 64);
		}
	}

	@WrapOperation(
		method = "renderWidget",
		at = @At(value = "INVOKE", target = "Lvazkii/patchouli/client/book/BookEntry;isLocked()Z")
	)
	private boolean pretendDogIsNotLocked(BookEntry instance, Operation<Boolean> original) {
		return !botania_enabledDog && original.call(instance);
	}

	@WrapOperation(
		method = "getColor",
		at = @At(value = "INVOKE", target = "Lvazkii/patchouli/client/book/BookEntry;isSecret()Z")
	)
	private boolean pretendDogIsNotSecret(BookEntry instance, Operation<Boolean> original) {
		return original.call(instance) && !DogEnabler.isDog(instance.getId());
	}

	@Override
	public void botania_enableDog() {
		if (!botania_enabledDog) {
			botania_enabledDog = true;
			// suppress the advancement toast for this
			ClientAdvancementsAccessor.botania_setGotFirstAdvPacket(false);
			ClientXplatAbstractions.INSTANCE.sendToServer(
					new IndexStringRequestPacket(IndexStringRequestPacket.DOG_CODE));
		}
	}
}
