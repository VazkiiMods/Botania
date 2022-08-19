/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 19, 2014, 10:46:14 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILaputaImmobile;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.lib.LibItemNames;

public class ItemLaputaShard extends ItemMod implements ILensEffect, ITinyPlanetExcempt {

	private static final String TAG_BLOCK = "_block";
	private static final String TAG_META = "_meta";
	private static final String TAG_TILE = "_tile";
	private static final String TAG_X = "_x";
	private static final String TAG_Y = "_y";
	private static final String TAG_Y_START = "_yStart";
	private static final String TAG_Z = "_z";
	private static final String TAG_POINTY = "_pointy";
	private static final String TAG_HEIGHTSCALE = "_heightscale";
	private static final String TAG_ITERATION_I = "iterationI";
	private static final String TAG_ITERATION_J = "iterationJ";
	private static final String TAG_ITERATION_K = "iterationK";

	private static final int BASE_RANGE = 14;
	private static final int BASE_OFFSET = 42;

	public ItemLaputaShard() {
		setUnlocalizedName(LibItemNames.LAPUTA_SHARD);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		super.getSubItems(item, tab, list);
		for(int i = 0; i < 4; i++)
			list.add(new ItemStack(item, 1, (i + 1) * 5 - 1));
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		list.add(String.format(StatCollector.translateToLocal("botaniamisc.shardLevel"), StatCollector.translateToLocal("botania.roman" + (stack.getItemDamage() + 1))));
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if(par5 < 160 && !par3World.provider.isHellWorld) {
			par3World.playSound(par4 + 0.5D, par5 + 0.5D, par6 + 0.5D, "botania:laputaStart", 1.0F + par3World.rand.nextFloat(), par3World.rand.nextFloat() * 0.7F + 1.3F, false);
			spawnBurstFirst(par3World, par4, par5, par6, par1ItemStack);
			par1ItemStack.stackSize--;
			if(par1ItemStack.getItemDamage() == 19)
				par2EntityPlayer.addStat(ModAchievements.l20ShardUse, 1);
		}

		return true;
	}

	public void spawnBurstFirst(World world, int srcx, int srcy, int srcz, ItemStack lens) {
		int range = BASE_RANGE + lens.getItemDamage();
		boolean pointy = world.rand.nextDouble() < 0.25;
		double heightscale = (world.rand.nextDouble() + 0.5) * ((double)BASE_RANGE / (double)range);
		spawnBurst(world, srcx, srcy, srcz, lens, pointy, heightscale);
	}

	public void spawnBurst(World world, int srcx, int srcy, int srcz, ItemStack lens) {
		boolean pointy = ItemNBTHelper.getBoolean(lens, TAG_POINTY, false);
		double heightscale = ItemNBTHelper.getDouble(lens, TAG_HEIGHTSCALE, 1);

		spawnBurst(world, srcx, srcy, srcz, lens, pointy, heightscale);
	}

	public void spawnBurst(World world, int srcx, int srcy, int srcz, ItemStack lens, boolean pointy, double heightscale) {
		int range = BASE_RANGE + lens.getItemDamage();

		int i = ItemNBTHelper.getInt(lens, TAG_ITERATION_I, 0);
		int j = ItemNBTHelper.getInt(lens, TAG_ITERATION_J, BASE_OFFSET - BASE_RANGE / 2);
		int k = ItemNBTHelper.getInt(lens, TAG_ITERATION_K, 0);

		if(j <= -BASE_RANGE * 2)
			j = BASE_OFFSET - BASE_RANGE / 2;
		if(k >= range * 2 + 1)
			k = 0;

		if(!world.isRemote) {
			for(; i < range * 2 + 1; i++) {
				for(; j > -BASE_RANGE * 2; j--) {
					for(; k < range * 2 + 1; k++) {
						int x = srcx - range + i;
						int y = srcy - BASE_RANGE + j;
						int z = srcz - range + k;

						if(inRange(x, y, z, srcx, srcy, srcz, range, heightscale, pointy)) {
							Block block = world.getBlock(x, y, z);
							if(!block.isAir(world, x, y, z) && !block.isReplaceable(world, x, y, z) && !(block instanceof BlockFalling) && (!(block instanceof ILaputaImmobile) || ((ILaputaImmobile) block).canMove(world, x, y, z)) && block.getBlockHardness(world, x, y, z) != -1) {
								int id = Block.getIdFromBlock(block);
								int meta = world.getBlockMetadata(x, y, z);
								TileEntity tile = world.getTileEntity(x, y, z);

								if(tile != null) {
									TileEntity newTile = block.createTileEntity(world, meta);
									world.setTileEntity(x, y, z, newTile);
								}
								world.setBlockToAir(x, y, z);
								world.playAuxSFX(2001, x, y, z, id + (meta << 12));

								ItemStack copyLens = new ItemStack(this, 1, lens.getItemDamage());
								ItemNBTHelper.setInt(copyLens, TAG_BLOCK, id);
								ItemNBTHelper.setInt(copyLens, TAG_META, meta);
								NBTTagCompound cmp = new NBTTagCompound();
								if(tile != null)
									tile.writeToNBT(cmp);
								ItemNBTHelper.setCompound(copyLens, TAG_TILE, cmp);
								ItemNBTHelper.setInt(copyLens, TAG_X, srcx);
								ItemNBTHelper.setInt(copyLens, TAG_Y, srcy);
								ItemNBTHelper.setInt(copyLens, TAG_Y_START, y);
								ItemNBTHelper.setInt(copyLens, TAG_Z, srcz);
								ItemNBTHelper.setBoolean(copyLens, TAG_POINTY, pointy);
								ItemNBTHelper.setDouble(copyLens, TAG_HEIGHTSCALE, heightscale);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_I, i);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_J, j);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_K, k);

								EntityManaBurst burst = getBurst(world, x, y, z, copyLens);
								world.spawnEntityInWorld(burst);
								return;
							}
						}
					}
					k = 0;
				}
				j = BASE_OFFSET - BASE_RANGE / 2;
			}
		}
	}

	private boolean inRange(int x, int y, int z, int srcx, int srcy, int srcz, int range, double heightscale, boolean pointy) {
		if(y >= srcy)
			return MathHelper.pointDistanceSpace(x, 0, z, srcx, 0, srcz) < range;
		else if(!pointy)
			return MathHelper.pointDistanceSpace(x, y / heightscale, z, srcx, srcy / heightscale, srcz) < range;
		else return MathHelper.pointDistanceSpace(x, 0, z, srcx, 0, srcz) < range - (srcy - y) / heightscale;
	}

	public EntityManaBurst getBurst(World world, int x, int y, int z, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(world);
		burst.posX = x + 0.5;
		burst.posY = y + 0.5;
		burst.posZ = z + 0.5;

		burst.setColor(0x00EAFF);
		burst.setMana(1);
		burst.setStartingMana(1);
		burst.setMinManaLoss(0);
		burst.setManaLossPerTick(0F);
		burst.setGravity(0F);
		burst.setMotion(0, 0.5, 0);

		burst.setSourceLens(stack);
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		// NO-OP
	}

	@Override
	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return false;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		double speed = 0.35;
		int targetDistance = BASE_OFFSET;
		EntityThrowable entity = (EntityThrowable) burst;
		if(!entity.worldObj.isRemote) {
			entity.motionX = 0;
			entity.motionY = speed;
			entity.motionZ = 0;

			final int spawnTicks = 2;
			final int placeTicks = net.minecraft.util.MathHelper.floor_double(targetDistance / speed);

			ItemStack lens = burst.getSourceLens();

			if(burst.getTicksExisted() == spawnTicks) {
				int x = ItemNBTHelper.getInt(lens, TAG_X, 0);
				int y = ItemNBTHelper.getInt(lens, TAG_Y, -1);
				int z = ItemNBTHelper.getInt(lens, TAG_Z, 0);

				if(y != -1)
					spawnBurst(entity.worldObj, x, y, z, lens);
			} else if(burst.getTicksExisted() == placeTicks) {
				int x = net.minecraft.util.MathHelper.floor_double(entity.posX);
				int y = ItemNBTHelper.getInt(lens, TAG_Y_START, -1) + targetDistance;
				int z = net.minecraft.util.MathHelper.floor_double(entity.posZ);

				if(entity.worldObj.isAirBlock(x, y, z)) {
					int id = ItemNBTHelper.getInt(lens, TAG_BLOCK, 0);
					Block block = Block.getBlockById(id);
					int meta = ItemNBTHelper.getInt(lens, TAG_META, 0);

					TileEntity tile = null;
					NBTTagCompound tilecmp = ItemNBTHelper.getCompound(lens, TAG_TILE, false);
					if(tilecmp.hasKey("id"))
						tile = TileEntity.createAndLoadEntity(tilecmp);

					entity.worldObj.setBlock(x, y, z, block, meta, 1 | 2);
					entity.worldObj.playAuxSFX(2001, x, y, z, id + (meta << 12));
					if(tile != null) {
						tile.xCoord = x;
						tile.yCoord = y;
						tile.zCoord = z;
						entity.worldObj.setTileEntity(x, y, z, tile);
					}
				}

				entity.setDead();
			}
		}
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;
		ItemStack lens = burst.getSourceLens();
		int id = ItemNBTHelper.getInt(lens, TAG_BLOCK, 0);
		Block.getBlockById(id);
		int meta = ItemNBTHelper.getInt(lens, TAG_META, 0);
		entity.worldObj.spawnParticle("blockcrack_" + id + "_" + meta, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ);

		return true;
	}

	@Override
	public boolean shouldPull(ItemStack stack) {
		return false;
	}

}
