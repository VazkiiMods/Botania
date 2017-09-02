/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 11:43:02 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class SubTileDaffomill extends SubTileFunctional {

	private static final String TAG_ORIENTATION = "orientation";
	private static final String TAG_WIND_TICKS = "windTicks";

	private int windTicks = 0;
	private EnumFacing orientation = EnumFacing.NORTH;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().rand.nextInt(4) == 0)
			Botania.proxy.wispFX(supertile.getPos().getX() + Math.random(), supertile.getPos().getY() + Math.random(), supertile.getPos().getZ() + Math.random(), 0.05F, 0.05F, 0.05F, 0.25F + (float) Math.random() * 0.15F, orientation.getFrontOffsetX() * 0.1F, orientation.getFrontOffsetY() * 0.1F, orientation.getFrontOffsetZ() * 0.1F);

		if(windTicks == 0 && mana > 0) {
			windTicks = 20;
			mana--;
		}

		if(windTicks > 0 && redstoneSignal == 0) {
			AxisAlignedBB axis = aabbForOrientation();

			if(axis != null) {
				List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, axis);
				int slowdown = getSlowdownFactor();
				for(EntityItem item : items) {
					if(!item.isDead && item.age >= slowdown) {
						item.motionX += orientation.getFrontOffsetX() * 0.05;
						item.motionY += orientation.getFrontOffsetY() * 0.05;
						item.motionZ += orientation.getFrontOffsetZ() * 0.05;
					}
				}
			}

			windTicks--;
		}
	}

	private AxisAlignedBB aabbForOrientation() {
		int x = supertile.getPos().getX();
		int y = supertile.getPos().getY();
		int z = supertile.getPos().getZ();
		int w = 2;
		int h = 3;
		int l = 16;

		AxisAlignedBB axis = null;
		switch(orientation) {
		case NORTH :
			axis = new AxisAlignedBB(x - w, y - h, z - l, x + w + 1, y + h, z);
			break;
		case SOUTH :
			axis = new AxisAlignedBB(x - w, y - h, z + 1, x + w + 1, y + h, z + l + 1);
			break;
		case WEST :
			axis = new AxisAlignedBB(x - l, y - h, z - w, x, y + h, z + w + 1);
			break;
		case EAST :
			axis = new AxisAlignedBB(x + 1, y - h, z - w, x + l + 1, y + h, z + w + 1);
		default: break;
		}
		return axis;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return false;

		if(player.isSneaking()) {
			if(!player.world.isRemote) {
				orientation = orientation.rotateY();
				sync();
			}

			return true;
		} else return super.onWanded(player, wand);
	}

	@Override
	public RadiusDescriptor getRadius() {
		AxisAlignedBB aabb = aabbForOrientation();
		aabb = new AxisAlignedBB(aabb.minX, supertile.getPos().getY(), aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
		return new RadiusDescriptor.Rectangle(toBlockPos(), aabb);
	}

	@Override
	public int getColor() {
		return 0xD8BA00;
	}

	@Override
	public int getMaxMana() {
		return 100;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.daffomill;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_ORIENTATION, orientation.getIndex() - 2); // retain compat with 1.7 saves
		cmp.setInteger(TAG_WIND_TICKS, windTicks);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		orientation = EnumFacing.getFront(cmp.getInteger(TAG_ORIENTATION) + 2); // retain compat with 1.7 saves
		windTicks = cmp.getInteger(TAG_WIND_TICKS);
	}

}
