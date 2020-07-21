/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.util.HandSide;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FirstPersonRenderer.class)
public interface AccessorFirstPersonRenderer {
	@Invoker
	void callTransformSideFirstPerson(MatrixStack ms, HandSide side, float equip);

	@Invoker
	void callTransformFirstPerson(MatrixStack ms, HandSide side, float swing);
}
