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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public class ItemObedienceStick extends ItemMod {

	public ItemObedienceStick(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		TileEntity tileAt = world.getTileEntity(pos);
		if(tileAt instanceof IManaPool || tileAt instanceof IManaCollector) {
			boolean pool = tileAt instanceof IManaPool;
			BiFunction<SubTileEntity, TileEntity, Boolean> act = pool ? functionalActuator : generatingActuator;
			int range = pool ? SubTileFunctional.LINK_RANGE : SubTileGenerating.LINK_RANGE;

			for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range))) {
				if(pos_.distanceSq(pos) > range * range)
					continue;

				TileEntity tile = world.getTileEntity(pos_);
				if(tile instanceof ISubTileContainer) {
					SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
					if(act.apply(subtile, tileAt)) {
						Vector3 orig = new Vector3(pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5);
						Vector3 end = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
						ItemTwigWand.doParticleBeam(world, orig, end);
					}
				}
			}

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	private static final BiFunction<SubTileEntity, TileEntity, Boolean> generatingActuator = (flower, tile) -> {
		if(flower instanceof SubTileGenerating) {
			((SubTileGenerating) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

	private static final BiFunction<SubTileEntity, TileEntity, Boolean> functionalActuator = (flower, tile) -> {
		if(flower instanceof SubTileFunctional) {
			((SubTileFunctional) flower).linkToForcefully(tile);
			return true;
		}
		return false;
	};

}
