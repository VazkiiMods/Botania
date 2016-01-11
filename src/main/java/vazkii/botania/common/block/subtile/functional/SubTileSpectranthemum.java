/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 27, 2015, 4:06:58 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lexicon.LexiconData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.lib.LibObfuscation;

public class SubTileSpectranthemum extends SubTileFunctional {

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private static final int COST = 24;
	private static final int RANGE = 2;
	private static final int BIND_RANGE = 12;

	private static final String TAG_TELEPORTED = "Botania_TPd";

	BlockPos bindPos = new BlockPos(0, -1, 0);

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal == 0 && supertile.getWorld().isBlockLoaded(bindPos)) {
			BlockPos pos = supertile.getPos();

			boolean did = false;

			List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();
			
			for(EntityItem item : items) {
				if(((Integer) ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, item, LibObfuscation.AGE)) < (60 + slowdown) || item.isDead || item.getEntityData().getBoolean(TAG_TELEPORTED))
					continue;

				ItemStack stack = item.getEntityItem();
				if(stack != null) {
					Item sitem = stack.getItem();
					if(sitem instanceof IManaItem)
						continue;

					int cost = stack.stackSize * COST;
					if(mana >= cost) {
						spawnExplosionParticles(item, 10);
						item.setPosition(bindPos.getX() + 0.5, bindPos.getY() + 1.5, bindPos.getZ() + 0.5);
						item.getEntityData().setBoolean(TAG_TELEPORTED, true);
						item.motionX = item.motionY = item.motionZ = 0;
						spawnExplosionParticles(item, 10);
						if(!supertile.getWorld().isRemote) {
							mana -= cost;
							did = true;
						}
					}
				}
			}

			if(did)
				sync();
		}
	}

	public static void spawnExplosionParticles(EntityItem item, int p) {
		for(int i = 0; i < p; i++) {
			double m = 0.01;
			double d0 = item.worldObj.rand.nextGaussian() * m;
			double d1 = item.worldObj.rand.nextGaussian() * m;
			double d2 = item.worldObj.rand.nextGaussian() * m;
			double d3 = 10.0D;
			item.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, item.posX + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d0 * d3, item.posY + item.worldObj.rand.nextFloat() * item.height - d1 * d3, item.posZ + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d2 * d3, d0, d1, d2);
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_BIND_X, bindPos.getX());
		cmp.setInteger(TAG_BIND_Y, bindPos.getY());
		cmp.setInteger(TAG_BIND_Z, bindPos.getZ());
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		bindPos = new BlockPos(
			cmp.getInteger(TAG_BIND_X),
			cmp.getInteger(TAG_BIND_Y),
			cmp.getInteger(TAG_BIND_Z)
		);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x98BCFF;
	}

	@Override
	public int getMaxMana() {
		return 16000;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		boolean bound = super.bindTo(player, wand, pos, side);

		if(!bound && !pos.equals(bindPos) && MathHelper.pointDistanceSpace(pos, supertile.getPos()) <= BIND_RANGE && !(pos.equals(supertile.getPos()))) {
			bindPos = pos;
			sync();

			return true;
		}

		return bound;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockPos getBinding() {
		return Minecraft.getMinecraft().thePlayer.isSneaking() && bindPos.getY() != -1 ? bindPos : super.getBinding();
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.spectranthemum;
	}

}
