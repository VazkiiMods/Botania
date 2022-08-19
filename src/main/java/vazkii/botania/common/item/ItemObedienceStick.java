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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;

public class ItemObedienceStick extends ItemMod {

	public ItemObedienceStick() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.OBEDIENCE_STICK);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int s, float xs, float ys, float zs) {
		TileEntity tileAt = world.getTileEntity(x, y, z);
		if(tileAt != null && (tileAt instanceof IManaPool || tileAt instanceof IManaCollector)) {
			boolean pool = tileAt instanceof IManaPool;
			Actuator act = pool ? Actuator.functionalActuator : Actuator.generatingActuator;
			int range = pool ? SubTileFunctional.RANGE : SubTileGenerating.RANGE;

			for(int i = -range; i < range + 1; i++)
				for(int j = -range; j < range + 1; j++)
					for(int k = -range; k < range + 1; k++) {
						int xp = x + i;
						int yp = y + j;
						int zp = z + k;
						if(MathHelper.pointDistanceSpace(xp, yp, zp, x, y, z) > range)
							continue;

						TileEntity tile = world.getTileEntity(xp, yp, zp);
						if(tile instanceof ISubTileContainer) {
							SubTileEntity subtile = ((ISubTileContainer) tile).getSubTile();
							if(act.actuate(subtile, tileAt)) {
								Vector3 orig = new Vector3(xp + 0.5, yp + 0.5, zp + 0.5);
								Vector3 end = new Vector3(x + 0.5, y + 0.5, z + 0.5);
								ItemTwigWand.doParticleBeam(world, orig, end);
							}
						}
					}

			if(player.worldObj.isRemote)
				player.swingItem();
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
