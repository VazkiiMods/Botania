/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ColorHelper;

public class LensFlare extends Lens {

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		return false;
	}

	@Override
	public void onControlledSpreaderTick(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		if (!redstone) {
			emitParticles(stack, spreader, redstone);
		}
	}

	@Override
	public void onControlledSpreaderPulse(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		emitParticles(stack, spreader, redstone);
	}

	private void emitParticles(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		float rotationYaw = -(spreader.getRotationX() + 90F);
		float rotationPitch = spreader.getRotationY();

		// Lots of EntityThrowable copypasta
		float f = 0.3F;
		float mx = (float) (Mth.sin(rotationYaw / 180.0F * (float) Math.PI) * Mth.cos(rotationPitch / 180.0F * (float) Math.PI) * f / 2D);
		float mz = (float) (-(Mth.cos(rotationYaw / 180.0F * (float) Math.PI) * Mth.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D);
		float my = (float) (Mth.sin(rotationPitch / 180.0F * (float) Math.PI) * f / 2D);

		int storedColor = ItemLens.getStoredColor(stack);
		int hex = -1;

		BlockEntity tile = spreader.tileEntity();
		if (storedColor == 16) {
			hex = Mth.hsvToRgb(tile.getLevel().getGameTime() * 2 % 360 / 360F, 1F, 1F);
		} else if (storedColor >= 0) {
			hex = ColorHelper.getColorValue(DyeColor.byId(storedColor));
		}

		float r = ((hex & 0xFF0000) >> 16) / 255F;
		float g = ((hex & 0xFF00) >> 8) / 255F;
		float b = (hex & 0xFF) / 255F;

		WispParticleData data = WispParticleData.wisp(0.4F, r, g, b);
		tile.getLevel().addParticle(data, tile.getBlockPos().getX() + 0.5, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5, mx, my, mz);
	}

}
