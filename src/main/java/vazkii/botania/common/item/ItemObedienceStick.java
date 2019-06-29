/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 20, 2015, 7:26:14 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public class ItemObedienceStick extends ItemMod {

	public ItemObedienceStick(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		TileEntity tileAt = world.getTileEntity(pos);
		if(tileAt instanceof IManaPool || tileAt instanceof IManaCollector) {
			boolean pool = tileAt instanceof IManaPool;
			BiFunction<TileEntitySpecialFlower, TileEntity, Boolean> act = pool ? functionalActuator : generatingActuator;
			int range = pool ? TileEntityFunctionalFlower.LINK_RANGE : TileEntityGeneratingFlower.LINK_RANGE;

			for(BlockPos pos_ : BlockPos.getAllInBoxMutable(pos.add(-range, -range, -range),
					pos.add(range, range, range))) {
				if(pos_.distanceSq(pos) > range * range)
					continue;

				TileEntity tile = world.getTileEntity(pos_);
				if(tile instanceof TileEntitySpecialFlower) {
					TileEntitySpecialFlower subtile = ((TileEntitySpecialFlower) tile);
					if(act.apply(subtile, tileAt)) {
						Vector3 orig = new Vector3(pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5);
						Vector3 end = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
						ItemTwigWand.doParticleBeam(world, orig, end);
					}
				}
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	private static final BiFunction<TileEntitySpecialFlower, TileEntity, Boolean> generatingActuator = (flower, tile) -> {
		if(flower instanceof TileEntityGeneratingFlower) {
			((TileEntityGeneratingFlower) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

	private static final BiFunction<TileEntitySpecialFlower, TileEntity, Boolean> functionalActuator = (flower, tile) -> {
		if(flower instanceof TileEntityFunctionalFlower) {
			((TileEntityFunctionalFlower) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

}
