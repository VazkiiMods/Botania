/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.client.core.helper.GuiGraphicsAddon;

import java.util.function.Supplier;

/**
 * Since GuiGraphics::blit (all overloads) now applies a shader explicitly, this helper allows skipping it.
 */
@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements GuiGraphicsAddon {
	@Unique
	private boolean boolean_skipSettingShader;

	@Unique
	private GuiGraphics botania_self() {
		return (GuiGraphics) (Object) this;
	}

	@Override
	public void botania_blitWithoutShader(ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset,
			int width, int height, int textureWidth, int textureHeight) {
		try {
			boolean_skipSettingShader = true;
			botania_self().blit(atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);

		} finally {
			boolean_skipSettingShader = false;
		}
	}

	@WrapWithCondition(
		method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V")
	)
	private boolean botania_shouldSetShader(Supplier<ShaderInstance> supplier) {
		return !boolean_skipSettingShader;
	}
}
