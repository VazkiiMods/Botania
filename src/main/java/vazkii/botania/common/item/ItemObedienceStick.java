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

public class ItemObedienceStick extends ItemMod {

	public ItemObedienceStick() {
		super(LibItemNames.OBEDIENCE_STICK);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xs, float ys, float zs) {
		EnumActionResult result = applyStick(world, pos) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		if (result == EnumActionResult.SUCCESS && player.world.isRemote)
			player.swingArm(hand);
		return result;
	}

	public static boolean applyStick(World world, BlockPos pos) {
		TileEntity tileAt = world.getTileEntity(pos);
		if(tileAt != null && (tileAt instanceof IManaPool || tileAt instanceof IManaCollector)) {
			boolean pool = tileAt instanceof IManaPool;
			Actuator act = pool ? Actuator.functionalActuator : Actuator.generatingActuator;
			int range = pool ? SubTileFunctional.LINK_RANGE : SubTileGenerating.LINK_RANGE;

			for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range))) {
				if(pos_.distanceSq(pos) > range * range)
					continue;

				TileEntity tile = world.getTileEntity(pos_);
				if(tile instanceof ISubTileContainer) {
					SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
					if(act.actuate(subtile, tileAt)) {
						Vector3 orig = new Vector3(pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5);
						Vector3 end = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
						ItemTwigWand.doParticleBeam(world, orig, end);
					}
				}
			}

			return true;
		}

		return false;
	}

	public static abstract class Actuator {
		public static final Actuator generatingActuator = new Actuator() {

			@Override
			public boolean actuate(SubTileEntity flower, TileEntity tile) {
				if(flower instanceof SubTileGenerating) {
					((SubTileGenerating) flower).linkToForcefully(tile);
					return true;
				}
				return false;
			}

		};

		public static final Actuator functionalActuator = new Actuator() {

			@Override
			public boolean actuate(SubTileEntity flower, TileEntity tile) {
				if(flower instanceof SubTileFunctional) {
					((SubTileFunctional) flower).linkToForcefully(tile);
					return true;
				}
				return false;
			}

		};

		public abstract boolean actuate(SubTileEntity flower, TileEntity tile);

	}

}
