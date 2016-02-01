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
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameData;
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
	private static final String TAG_BLOCK_NAME = "_blockname";
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
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		super.getSubItems(item, tab, list);
		for(int i = 0; i < 4; i++)
			list.add(new ItemStack(item, 1, (i + 1) * 5 - 1));
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
		list.add(String.format(StatCollector.translateToLocal("botaniamisc.shardLevel"), StatCollector.translateToLocal("botania.roman" + (stack.getItemDamage() + 1))));
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumFacing side, float par8, float par9, float par10) {
		if(!par3World.isRemote && pos.getY() < 160 && !par3World.provider.doesWaterVaporize()) {
			par3World.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "botania:laputaStart", 1.0F + par3World.rand.nextFloat(), par3World.rand.nextFloat() * 0.7F + 1.3F, false);
			spawnBurstFirst(par3World, pos, par1ItemStack);
			par1ItemStack.stackSize--;
			if(par1ItemStack.getItemDamage() == 19)
				par2EntityPlayer.addStat(ModAchievements.l20ShardUse, 1);
		}

		return true;
	}

	public void spawnBurstFirst(World world, BlockPos pos, ItemStack lens) {
		int range = BASE_RANGE + lens.getItemDamage();
		boolean pointy = world.rand.nextDouble() < 0.25;
		double heightscale = (world.rand.nextDouble() + 0.5) * ((double)BASE_RANGE / (double)range);
		spawnBurst(world, pos, lens, pointy, heightscale);
	}

	public void spawnBurst(World world, BlockPos pos, ItemStack lens) {
		boolean pointy = ItemNBTHelper.getBoolean(lens, TAG_POINTY, false);
		double heightscale = ItemNBTHelper.getDouble(lens, TAG_HEIGHTSCALE, 1);

		spawnBurst(world, pos, lens, pointy, heightscale);
	}

	public void spawnBurst(World world, BlockPos pos, ItemStack lens, boolean pointy, double heightscale) {
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
						BlockPos pos_ = pos.add(-range + i, -BASE_RANGE + j, -range + k);

						if(inRange(pos_, pos, range, heightscale, pointy)) {
							Block block = world.getBlockState(pos_).getBlock();
							if(!block.isAir(world, pos_) && !block.isReplaceable(world, pos_) && !(block instanceof BlockFalling) && (!(block instanceof ILaputaImmobile) || ((ILaputaImmobile) block).canMove(world, pos_)) && block.getBlockHardness(world, pos_) != -1) {
								int id = Block.getIdFromBlock(block);
								IBlockState state = world.getBlockState(pos_);
								TileEntity tile = world.getTileEntity(pos_);

								if(tile != null) {
									TileEntity newTile = block.createTileEntity(world, state);
									world.setTileEntity(pos_, newTile);
								}
								world.setBlockToAir(pos_);
								world.playAuxSFX(2001, pos_, Block.getStateId(state));

								ItemStack copyLens = new ItemStack(this, 1, lens.getItemDamage());
								ItemNBTHelper.setString(copyLens, TAG_BLOCK_NAME, GameData.getBlockRegistry().getNameForObject(block).toString());
								ItemNBTHelper.setInt(copyLens, TAG_META, block.getMetaFromState(state));
								NBTTagCompound cmp = new NBTTagCompound();
								if(tile != null)
									tile.writeToNBT(cmp);
								ItemNBTHelper.setCompound(copyLens, TAG_TILE, cmp);
								ItemNBTHelper.setInt(copyLens, TAG_X, pos.getX());
								ItemNBTHelper.setInt(copyLens, TAG_Y, pos.getY());
								ItemNBTHelper.setInt(copyLens, TAG_Y_START, pos_.getY());
								ItemNBTHelper.setInt(copyLens, TAG_Z, pos.getZ());
								ItemNBTHelper.setBoolean(copyLens, TAG_POINTY, pointy);
								ItemNBTHelper.setDouble(copyLens, TAG_HEIGHTSCALE, heightscale);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_I, i);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_J, j);
								ItemNBTHelper.setInt(copyLens, TAG_ITERATION_K, k);

								EntityManaBurst burst = getBurst(world, pos_, copyLens);
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

	private boolean inRange(BlockPos pos, BlockPos srcPos, int range, double heightscale, boolean pointy) {
		if(pos.getY() >= srcPos.getY())
			return MathHelper.pointDistanceSpace(pos.getX(), 0, pos.getZ(), srcPos.getX(), 0, srcPos.getZ()) < range;
		else if(!pointy)
			return MathHelper.pointDistanceSpace(pos.getX(), pos.getY() / heightscale, pos.getZ(), srcPos.getX(), srcPos.getY() / heightscale, srcPos.getZ()) < range;
		else return MathHelper.pointDistanceSpace(pos.getX(), 0, pos.getZ(), srcPos.getX(), 0, srcPos.getZ()) < range - (srcPos.getY() - pos.getY()) / heightscale;
	}

	public EntityManaBurst getBurst(World world, BlockPos pos, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(world);
		burst.posX = pos.getX() + 0.5;
		burst.posY = pos.getY() + 0.5;
		burst.posZ = pos.getZ() + 0.5;

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
					spawnBurst(entity.worldObj, new BlockPos(x, y, z), lens);
			} else if(burst.getTicksExisted() == placeTicks) {
				int x = net.minecraft.util.MathHelper.floor_double(entity.posX);
				int y = ItemNBTHelper.getInt(lens, TAG_Y_START, -1) + targetDistance;
				int z = net.minecraft.util.MathHelper.floor_double(entity.posZ);
				BlockPos pos = new BlockPos(x, y, z);

				if(entity.worldObj.isAirBlock(pos)) {
					Block block = Blocks.air;
					if (lens.hasTagCompound()) {
						if (lens.getTagCompound().hasKey(TAG_BLOCK_NAME)) {
							block = Block.getBlockFromName(ItemNBTHelper.getString(lens, TAG_BLOCK_NAME, ""));
						} else if (lens.getTagCompound().hasKey(TAG_BLOCK)) {
							// Attempt to read legacy tag (integer ID) if string block ID is absent
							block = Block.getBlockById(ItemNBTHelper.getInt(lens, TAG_BLOCK, 0));
						}
					}
					int meta = ItemNBTHelper.getInt(lens, TAG_META, 0);

					TileEntity tile = null;
					NBTTagCompound tilecmp = ItemNBTHelper.getCompound(lens, TAG_TILE, false);
					if(tilecmp.hasKey("id"))
						tile = TileEntity.createAndLoadEntity(tilecmp);

					entity.worldObj.setBlockState(pos, block.getStateFromMeta(meta), 1 | 2);
					entity.worldObj.playAuxSFX(2001, pos, Block.getStateId(block.getStateFromMeta(meta)));
					if(tile != null) {
						tile.setPos(pos);
						entity.worldObj.setTileEntity(pos, tile);
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
		String id = ItemNBTHelper.getString(lens, TAG_BLOCK_NAME, "minecraft:air");
		Block b = Block.getBlockFromName(id);
		int meta = ItemNBTHelper.getInt(lens, TAG_META, 0);
		entity.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ, Block.getStateId(b.getStateFromMeta(meta)));

		return true;
	}

	@Override
	public boolean shouldPull(ItemStack stack) {
		return false;
	}

}
