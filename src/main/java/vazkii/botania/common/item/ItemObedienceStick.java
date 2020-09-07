/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

import javax.annotation.Nonnull;

import java.util.function.BiFunction;

public class ItemObedienceStick extends Item {

	public ItemObedienceStick(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		return applyStick(world, pos) ? ActionResultType.SUCCESS : ActionResultType.PASS;
	}

	public static boolean applyStick(World world, BlockPos pos) {
		TileEntity tileAt = world.getTileEntity(pos);
		if (tileAt instanceof IManaPool || tileAt instanceof IManaCollector) {
			boolean pool = tileAt instanceof IManaPool;
			BiFunction<TileEntitySpecialFlower, TileEntity, Boolean> act = pool ? functionalActuator : generatingActuator;
			int range = pool ? TileEntityFunctionalFlower.LINK_RANGE : TileEntityGeneratingFlower.LINK_RANGE;

			for (BlockPos iterPos : BlockPos.getAllInBoxMutable(pos.add(-range, -range, -range),
					pos.add(range, range, range))) {
				if (iterPos.distanceSq(pos) > range * range) {
					continue;
				}

				TileEntity tile = world.getTileEntity(iterPos);
				if (tile instanceof TileEntitySpecialFlower) {
					TileEntitySpecialFlower subtile = ((TileEntitySpecialFlower) tile);
					if (act.apply(subtile, tileAt)) {
						ItemTwigWand.doParticleBeamWithOffset(world, iterPos, pos);
					}
				}
			}

			return true;
		}

		return false;
	}

	private static final BiFunction<TileEntitySpecialFlower, TileEntity, Boolean> generatingActuator = (flower, tile) -> {
		if (flower instanceof TileEntityGeneratingFlower) {
			((TileEntityGeneratingFlower) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

	private static final BiFunction<TileEntitySpecialFlower, TileEntity, Boolean> functionalActuator = (flower, tile) -> {
		if (flower instanceof TileEntityFunctionalFlower) {
			((TileEntityFunctionalFlower) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

}
