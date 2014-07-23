/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 19, 2014, 10:46:14 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
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
	private static final String TAG_Z = "_z";

	public ItemLaputaShard() {
		setUnlocalizedName(LibItemNames.LAPUTA_SHARD);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if(par5 < 160 && !par3World.provider.isHellWorld) {
			par3World.playSound(par4 + 0.5D, par5 + 0.5D, par6 + 0.5D, "mob.zombie.remedy", 1.0F + par3World.rand.nextFloat(), par3World.rand.nextFloat() * 0.7F + 1.3F, false);
			spawnBurst(par3World, par4, par5, par6);
			par1ItemStack.stackSize--;
		}

		return true;
	}

	public void spawnBurst(World world, int srcx, int srcy, int srcz) {
		int range = 14;

		if(!world.isRemote) {
			for(int i = 0; i < range * 2 + 1; i++)
				for(int j = range * 2 + 1; j > 0; j--)
					for(int k = 0; k < range * 2 + 1; k++) {
						int x = srcx - range + i;
						int y = srcy - range + j;
						int z = srcz - range + k;

						if(MathHelper.pointDistanceSpace(x, y, z, srcx, srcy, srcz) < range) {
							Block block = world.getBlock(x, y, z);
							if(!block.isAir(world, x, y, z) && !block.isReplaceable(world, x, y, z) && !(block instanceof BlockFalling) && block.getBlockHardness(world, x, y, z) != -1) {
								int id = Block.getIdFromBlock(block);
								int meta = world.getBlockMetadata(x, y, z);
								TileEntity tile = world.getTileEntity(x, y, z);

								if(tile != null) {
									TileEntity newTile = block.createTileEntity(world, meta);
									world.setTileEntity(x, y, z, newTile);
								}
								world.setBlockToAir(x, y, z);
								world.playAuxSFX(2001, x, y, z, id + (meta << 12));

								ItemStack lens = new ItemStack(this);
								ItemNBTHelper.setInt(lens, TAG_BLOCK, id);
								ItemNBTHelper.setInt(lens, TAG_META, meta);
								NBTTagCompound cmp = new NBTTagCompound();
								if(tile != null)
									tile.writeToNBT(cmp);
								ItemNBTHelper.setCompound(lens, TAG_TILE, cmp);
								ItemNBTHelper.setInt(lens, TAG_X, srcx);
								ItemNBTHelper.setInt(lens, TAG_Y, srcy);
								ItemNBTHelper.setInt(lens, TAG_Z, srcz);

								EntityManaBurst burst = getBurst(world, x, y, z, lens);
								world.spawnEntityInWorld(burst);
								return;
							}
						}
					}
		}
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
		EntityThrowable entity = (EntityThrowable) burst;
		if(!entity.worldObj.isRemote) {
			entity.motionX = 0;
			entity.motionY = 0.35;
			entity.motionZ = 0;

			final int spawnTicks = 3;
			final int placeTicks = 120;

			ItemStack lens = burst.getSourceLens();

			if(burst.getTicksExisted() == spawnTicks) {
				int x = ItemNBTHelper.getInt(lens, TAG_X, 0);
				int y = ItemNBTHelper.getInt(lens, TAG_Y, -1);
				int z = ItemNBTHelper.getInt(lens, TAG_Z, 0);

				if(y != -1)
					spawnBurst(entity.worldObj, x, y, z);
			} else if(burst.getTicksExisted() == placeTicks) {
				int x = net.minecraft.util.MathHelper.floor_double(entity.posX);
				int y = net.minecraft.util.MathHelper.floor_double(entity.posY);
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
