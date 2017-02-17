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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILaputaImmobile;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.client.core.handler.ModelHandler;
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
	private static final String TAG_DOWNBLOCK = "_downblock";
	private static final String TAG_UPBLOCK = "_upblock";

	private static final int BASE_RANGE = 14;
	private static final int BASE_OFFSET = 42;

	public ItemLaputaShard() {
		super(LibItemNames.LAPUTA_SHARD);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
		super.getSubItems(item, tab, list);
		for(int i = 0; i < 4; i++)
			list.add(new ItemStack(item, 1, (i + 1) * 5 - 1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
		list.add(I18n.format("botaniamisc.shardLevel", I18n.format("botania.roman" + (stack.getItemDamage() + 1))));
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		if(!world.isRemote && pos.getY() < 160 && !world.provider.doesWaterVaporize()) {
			world.playSound(null, pos, BotaniaSoundEvents.laputaStart, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat(), world.rand.nextFloat() * 0.7F + 1.3F);
			spawnBurstFirst(world, pos, par1ItemStack);
			par1ItemStack.stackSize--;
			if(par1ItemStack.getItemDamage() == 19)
				player.addStat(ModAchievements.l20ShardUse, 1);
		}

		return EnumActionResult.SUCCESS;
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
						BlockPos iterPos = pos.add(-range + i, -BASE_RANGE + j, -range + k);

						if(inRange(iterPos, pos, range, heightscale, pointy)) {
							if (spawnBurstInternal(world, pos, iterPos, lens, pointy, heightscale, i, j, k)) {
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

	private static class DelayUpdate {
		public BlockPos pos;
		public IBlockState priorState;
		public IBlockState newState;
		public DelayUpdate(BlockPos inPos, IBlockState inPriorState, IBlockState inNewState)
		{
			pos = inPos;
			priorState = inPriorState;
			newState = inNewState;
		}
	}

	final protected boolean shouldCaptureDown(World world, BlockPos pos, Block block) {
		if(block instanceof BlockBush) {
			// Bushes are special little snowflakes that pretty much always need the block below them.
			return true;
		}

		if(block instanceof BlockDoor) {
			// Yeah doors too.
			return true;
		}

		return false;
	}

	final protected boolean shouldCaptureUp(World world, BlockPos pos, Block block) {
		IBlockState upBlockState = world.getBlockState(pos.up());
		Block upBlock = upBlockState.getBlock();
		if (upBlock instanceof BlockBush) {
			return true;
		}

		if(upBlock instanceof BlockDoor) {
			return true;
		}

		return false;
	}

	final protected void captureBlock(World world, Block block, IBlockState state, BlockPos pos, NBTTagCompound nbt, List<DelayUpdate> updateList) {
		captureBlock(world, block, state, pos, nbt, updateList, true, true);
	}
	final protected void captureBlock(World world, Block block, IBlockState state, BlockPos pos, NBTTagCompound nbt, List<DelayUpdate> updateList, boolean recurseDown, boolean recurseUp) {
		TileEntity tile = world.getTileEntity(pos);

		if(tile != null) {
			TileEntity newTile = block.createTileEntity(world, state);
			world.setTileEntity(pos, newTile);
		}
		nbt.setString(TAG_BLOCK_NAME, Block.REGISTRY.getNameForObject(block).toString());
		nbt.setInteger(TAG_META, block.getMetaFromState(state));
		NBTTagCompound cmp = new NBTTagCompound();
		if(tile != null)
			tile.writeToNBT(cmp);
		nbt.setTag(TAG_TILE, cmp);

		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 0);  // 0 flags basically prevents all kinds of updates

		updateList.add(new DelayUpdate(pos, state, Blocks.AIR.getDefaultState()));

		if(recurseDown && shouldCaptureDown(world, pos, block)) {
			IBlockState downBlockState = world.getBlockState(pos.down());
			Block downBlock = downBlockState.getBlock();
			NBTTagCompound downBlockNBT = new NBTTagCompound();
			captureBlock(world, downBlock, downBlockState, pos.down(), downBlockNBT, updateList, true, false);
			nbt.setTag(TAG_DOWNBLOCK, downBlockNBT);
		}

		if(recurseUp && shouldCaptureUp(world, pos, block)) {
			IBlockState upBlockState = world.getBlockState(pos.up());
			Block upBlock = upBlockState.getBlock();
			NBTTagCompound upBlockNBT = new NBTTagCompound();
			captureBlock(world, upBlock, upBlockState, pos.up(), upBlockNBT, updateList, false, true);
			nbt.setTag(TAG_UPBLOCK, upBlockNBT);
		}
	}

	final protected void triggerDelayedUpdates(World world, List<DelayUpdate> updateList) {
		for (DelayUpdate update : updateList) {
			Chunk chunk = world.getChunkFromBlockCoords(update.pos);
			world.markAndNotifyBlock(update.pos, chunk, update.priorState, update.newState, 3);
		}
	}

	final protected boolean spawnBurstInternal(World world, BlockPos pos, BlockPos iterPos, ItemStack lens, boolean pointy, double heightscale, int i, int j, int k) {
		IBlockState state = world.getBlockState(iterPos);
		Block block = state.getBlock();
		if(!block.isAir(state, world, iterPos) && !block.isReplaceable(world, iterPos) && !(block instanceof BlockFalling) && (!(block instanceof ILaputaImmobile) || ((ILaputaImmobile) block).canMove(world, iterPos)) && state.getBlockHardness(world, iterPos) != -1) {

			world.playEvent(2001, iterPos, Block.getStateId(state));

			ItemStack copyLens = new ItemStack(this, 1, lens.getItemDamage());
			ArrayList<DelayUpdate> updates = new ArrayList<>();
			captureBlock(world, block, state, iterPos, ItemNBTHelper.getNBT(copyLens), updates);
			triggerDelayedUpdates(world, updates);

			ItemNBTHelper.setInt(copyLens, TAG_X, pos.getX());
			ItemNBTHelper.setInt(copyLens, TAG_Y, pos.getY());
			ItemNBTHelper.setInt(copyLens, TAG_Y_START, iterPos.getY());
			ItemNBTHelper.setInt(copyLens, TAG_Z, pos.getZ());
			ItemNBTHelper.setBoolean(copyLens, TAG_POINTY, pointy);
			ItemNBTHelper.setDouble(copyLens, TAG_HEIGHTSCALE, heightscale);
			ItemNBTHelper.setInt(copyLens, TAG_ITERATION_I, i);
			ItemNBTHelper.setInt(copyLens, TAG_ITERATION_J, j);
			ItemNBTHelper.setInt(copyLens, TAG_ITERATION_K, k);

			EntityManaBurst burst = getBurst(world, iterPos, copyLens);
			world.spawnEntityInWorld(burst);
			return true;
		}
		return false;
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
	public void apply(ItemStack stack, BurstProperties props) {}

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return false;
	}

	final protected void reconstituteBlock(World world, BlockPos pos, NBTTagCompound blockNBT, List<DelayUpdate> updateList) {
		if(world.isAirBlock(pos)) {
 			Block block = Blocks.AIR;
			ItemStack lens;
			if (blockNBT.hasKey(TAG_BLOCK_NAME)) {
				block = Block.getBlockFromName(blockNBT.getString(TAG_BLOCK_NAME));
			} else if (blockNBT.hasKey(TAG_BLOCK)) {
				// Attempt to read legacy tag (integer ID) if string block ID is absent
				block = Block.getBlockById(blockNBT.getInteger(TAG_BLOCK));
			}
			int meta = blockNBT.getInteger(TAG_META);

			TileEntity tile = null;
			NBTTagCompound tilecmp = blockNBT.getCompoundTag(TAG_TILE);
			if(tilecmp.hasKey("id"))
				tile = TileEntity.func_190200_a(world, tilecmp);

			IBlockState newState = block.getStateFromMeta(meta);
			world.setBlockState(pos, newState, 0);
			world.playEvent(2001, pos, Block.getStateId(newState));
			if(tile != null) {
				tile.setPos(pos);
				world.setTileEntity(pos, tile);
			}
			if(blockNBT.hasKey(TAG_DOWNBLOCK)) {
				NBTTagCompound subBlock = blockNBT.getCompoundTag(TAG_DOWNBLOCK);
				reconstituteBlock(world, pos.down(), subBlock, updateList);
			}
			if(blockNBT.hasKey(TAG_UPBLOCK)) {
				NBTTagCompound subBlock = blockNBT.getCompoundTag(TAG_UPBLOCK);
				reconstituteBlock(world, pos.up(), subBlock, updateList);
			}

			updateList.add(new DelayUpdate(pos, Blocks.AIR.getDefaultState(), newState));
		}
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
			final int placeTicks = net.minecraft.util.math.MathHelper.floor_double(targetDistance / speed);

			ItemStack lens = burst.getSourceLens();

			if(burst.getTicksExisted() == spawnTicks) {
				int x = ItemNBTHelper.getInt(lens, TAG_X, 0);
				int y = ItemNBTHelper.getInt(lens, TAG_Y, -1);
				int z = ItemNBTHelper.getInt(lens, TAG_Z, 0);

				if(y != -1)
					spawnBurst(entity.worldObj, new BlockPos(x, y, z), lens);
			} else if(burst.getTicksExisted() == placeTicks) {
				int x = net.minecraft.util.math.MathHelper.floor_double(entity.posX);
				int y = ItemNBTHelper.getInt(lens, TAG_Y_START, -1) + targetDistance;
				int z = net.minecraft.util.math.MathHelper.floor_double(entity.posZ);
				BlockPos pos = new BlockPos(x, y, z);
				NBTTagCompound itemNBT = ItemNBTHelper.getNBT(lens);
				ArrayList<DelayUpdate> updates = new ArrayList<>();
				reconstituteBlock(entity.worldObj, pos, itemNBT, updates);
				triggerDelayedUpdates(entity.worldObj, updates);
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

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAllMeta(this, 20);
	}

}
