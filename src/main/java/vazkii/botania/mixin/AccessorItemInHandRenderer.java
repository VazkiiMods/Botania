/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemInHandRenderer.class)
public interface AccessorItemInHandRenderer {
	@Invoker("applyItemArmTransform")
	void botania_equipOffset(PoseStack ms, HumanoidArm side, float equip);

	@Invoker("applyItemArmAttackTransform")
	void botania_swingOffset(PoseStack ms, HumanoidArm side, float swing);
}
