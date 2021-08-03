/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		return applyStick(world, pos) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	public static boolean applyStick(Level world, BlockPos pos) {
		BlockEntity tileAt = world.getBlockEntity(pos);
		if (tileAt instanceof IManaPool || tileAt instanceof IManaCollector) {
			boolean pool = tileAt instanceof IManaPool;
			BiFunction<TileEntitySpecialFlower, BlockEntity, Boolean> act = pool ? functionalActuator : generatingActuator;
			int range = pool ? TileEntityFunctionalFlower.LINK_RANGE : TileEntityGeneratingFlower.LINK_RANGE;

			for (BlockPos iterPos : BlockPos.betweenClosed(pos.offset(-range, -range, -range),
					pos.offset(range, range, range))) {
				if (iterPos.distSqr(pos) > range * range) {
					continue;
				}

				BlockEntity tile = world.getBlockEntity(iterPos);
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

	private static final BiFunction<TileEntitySpecialFlower, BlockEntity, Boolean> generatingActuator = (flower, tile) -> {
		if (flower instanceof TileEntityGeneratingFlower) {
			((TileEntityGeneratingFlower) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

	private static final BiFunction<TileEntitySpecialFlower, BlockEntity, Boolean> functionalActuator = (flower, tile) -> {
		if (flower instanceof TileEntityFunctionalFlower) {
			((TileEntityFunctionalFlower) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

}
