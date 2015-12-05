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

import java.awt.Color;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.common.Botania;

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
		float r = 1, g = 1, b = 1;

		TileEntity tile = (TileEntity) spreader;
		if(storedColor == 16) {
			Color c = Color.getHSBColor(tile.getWorldObj().getTotalWorldTime() * 2 % 360 / 360F, 1F, 1F);
			r = c.getRed() / 255F;
			g = c.getGreen() / 255F;
			b = c.getBlue() / 255F;
		} else if(storedColor >= 0) {
			float[] colortable = EntitySheep.fleeceColorTable[storedColor];
			r = colortable[0];
			g = colortable[1];
			b = colortable[2];
		}

		Botania.proxy.wispFX(tile.getWorldObj(), tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, r, g, b, 0.4F, mx, my, mz);
	}

}
