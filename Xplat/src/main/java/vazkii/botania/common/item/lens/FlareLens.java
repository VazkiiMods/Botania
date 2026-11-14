/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.mana.ManaSpreader;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.entity.ManaBurstEntity;

public class FlareLens extends Lens {

	@Override
	public boolean allowBurstShooting(ItemStack stack, ManaSpreader spreader, boolean redstone) {
		return false;
	}

	@Override
	public void onControlledSpreaderTick(ItemStack stack, ManaSpreader spreader, boolean redstone) {
		if (!redstone) {
			emitParticles(stack, spreader);
		}
	}

	@Override
	public void onControlledSpreaderPulse(ItemStack stack, ManaSpreader spreader) {
		emitParticles(stack, spreader);
	}

	private void emitParticles(ItemStack stack, ManaSpreader spreader) {
		Vec3 v = ManaBurstEntity.calculateBurstVelocity(spreader.getYRot(), spreader.getXRot(), 0.15f);
		double mx = v.x;
		double mz = v.z;
		double my = v.y;

		int color;

		var level = spreader.getManaReceiverLevel();
		if (LensItem.isLensRainbow(stack)) {
			color = Mth.hsvToRgb(level.getGameTime() * 2 % 360 / 360F, 1F, 1F);
		} else {
			DyeColor storedColor = LensItem.getLensColor(stack);
			if (storedColor != null) {
				color = storedColor.getTextureDiffuseColor();
			} else {
				color = 0xFFFFFF;
			}
		}

		float r = FastColor.ARGB32.red(color) / 255F;
		float g = FastColor.ARGB32.green(color) / 255F;
		float b = FastColor.ARGB32.blue(color) / 255F;

		WispParticleData data = WispParticleData.wisp(0.4F, r, g, b);
		// The start position is set a bit away from the spreader (along the burst's motion vector), as to not
		// collide with the spreader itself.
		var pos = spreader.getManaReceiverPos();
		level.addParticle(data, pos.getX() + 0.5 + mx * 4.5, pos.getY() + 0.5 + my * 4.5, pos.getZ() + 0.5 + mz * 4.5, mx, my, mz);
	}

}
