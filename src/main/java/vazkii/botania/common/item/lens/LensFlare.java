/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [15/11/2015, 19:13:10 (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.common.Botania;

import java.awt.Color;

public class LensFlare extends Lens {

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		return false;
	}

	@Override
	public void onControlledSpreaderTick(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		if(!redstone)
			emitParticles(stack, spreader, redstone);
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
		float mx = (float) (MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f / 2D);
		float mz = (float) (-(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D);
		float my = (float) (MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * f / 2D);

		int storedColor = ItemLens.getStoredColor(stack);
		int hex = -1;

		TileEntity tile = (TileEntity) spreader;
		if(storedColor == 16) {
			hex = Color.HSBtoRGB(tile.getWorld().getTotalWorldTime() * 2 % 360 / 360F, 1F, 1F);
		} else if(storedColor >= 0) {
			hex = EnumDyeColor.byMetadata(storedColor).colorValue;
		}

		float r = ((hex & 0xFF0000) >> 16) / 255F;
		float g = ((hex & 0xFF00) >> 8) / 255F;
		float b = (hex & 0xFF) / 255F;

		Botania.proxy.wispFX(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5, r, g, b, 0.4F, mx, my, mz);
	}

}
