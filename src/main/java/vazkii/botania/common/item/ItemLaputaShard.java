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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILaputaImmobile;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.entity.EntityManaBurst;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemLaputaShard extends ItemMod implements ILensEffect, ITinyPlanetExcempt {

	private static final String TAG_STATE = "_state";
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
	private static final String TAG_LEVEL = "level";

	private static final int BASE_RANGE = 14;
	private static final int BASE_OFFSET = 42;

	public ItemLaputaShard(Properties props) {
		super(props);
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInGroup(tab)) {
			for(int i = 0; i <= 20; i += 5) {
				ItemStack s = new ItemStack(this);
				s.getOrCreateTag().putInt(TAG_LEVEL, i);
				list.add(s);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		int level = getShardLevel(stack);
		ITextComponent levelLoc = new TranslationTextComponent("botania.roman" + (level + 1));
		list.add(new TranslationTextComponent("botaniamisc.shardLevel", levelLoc).applyTextStyle(TextFormatting.GRAY));
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		if(!world.isRemote && pos.getY() < 160 && !world.getDimension().isNether()) {
			world.playSound(null, pos, ModSounds.laputaStart, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat(), world.rand.nextFloat() * 0.7F + 1.3F);
			ItemStack stack = ctx.getItem();
			spawnBurstFirst(world, pos, stack);
			if(ctx.getPlayer() != null) {
				UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayerEntity) ctx.getPlayer(), stack, (ServerWorld) world, pos.getX(), pos.getY(), pos.getZ());
			}
			stack.shrink(1);
		}

		return ActionResultType.SUCCESS;
	}

	public void spawnBurstFirst(World world, BlockPos pos, ItemStack shard) {
		int range = BASE_RANGE + getShardLevel(shard);
		boolean pointy = world.rand.nextDouble() < 0.25;
		double heightscale = (world.rand.nextDouble() + 0.5) * ((double)BASE_RANGE / (double)range);
		spawnBurst(world, pos, shard, pointy, heightscale);
	}

	public void spawnBurst(World world, BlockPos pos, ItemStack lens) {
		boolean pointy = ItemNBTHelper.getBoolean(lens, TAG_POINTY, false);
		double heightscale = ItemNBTHelper.getDouble(lens, TAG_HEIGHTSCALE, 1);

		spawnBurst(world, pos, lens, pointy, heightscale);
	}

	public void spawnBurst(World world, BlockPos pos, ItemStack shard, boolean pointy, double heightscale) {
		int range = BASE_RANGE + getShardLevel(shard);

		int i = ItemNBTHelper.getInt(shard, TAG_ITERATION_I, 0);
		int j = ItemNBTHelper.getInt(shard, TAG_ITERATION_J, BASE_OFFSET - BASE_RANGE / 2);
		int k = ItemNBTHelper.getInt(shard, TAG_ITERATION_K, 0);

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
							BlockState state = world.getBlockState(pos_);
							Block block = state.getBlock();
							if(!block.isAir(state, world, pos_) && !(block instanceof FallingBlock) && (!(block instanceof ILaputaImmobile) || ((ILaputaImmobile) block).canMove(world, pos_)) && state.getBlockHardness(world, pos_) != -1) {
								TileEntity tile = world.getTileEntity(pos_);

								if(tile != null) {
									// Reset the TE so e.g. chests don't spawn their drops
									TileEntity newTile = block.createTileEntity(state, world);
									world.setTileEntity(pos_, newTile);
								}
								world.destroyBlock(pos_, false);

								ItemStack copyLens = new ItemStack(this);
								copyLens.getOrCreateTag().putInt(TAG_LEVEL, getShardLevel(shard));
								copyLens.getTag().put(TAG_STATE, NBTUtil.writeBlockState(state));
								CompoundNBT cmp = new CompoundNBT();
								if(tile != null)
									cmp = tile.write(cmp);
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
								world.addEntity(burst);
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

	public static int getShardLevel(ItemStack shard) {
		return shard.getOrCreateTag().getInt(TAG_LEVEL);
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
		burst.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

		burst.setColor(0x00EAFF);
		burst.setMana(1);
		burst.setStartingMana(1);
		burst.setMinManaLoss(0);
		burst.setManaLossPerTick(0F);
		burst.setGravity(0F);
		burst.setBurstMotion(0, 0.5, 0);

		burst.setSourceLens(stack);
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {}

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return false;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		double speed = 0.35;
		int targetDistance = BASE_OFFSET;
		ThrowableEntity entity = (ThrowableEntity) burst;
		if(!entity.world.isRemote) {
			entity.setMotion(0, speed, 0);

			final int spawnTicks = 2;
			final int placeTicks = net.minecraft.util.math.MathHelper.floor(targetDistance / speed);

			ItemStack lens = burst.getSourceLens();

			if(burst.getTicksExisted() == spawnTicks) {
				int x = ItemNBTHelper.getInt(lens, TAG_X, 0);
				int y = ItemNBTHelper.getInt(lens, TAG_Y, -1);
				int z = ItemNBTHelper.getInt(lens, TAG_Z, 0);

				if(y != -1)
					spawnBurst(entity.world, new BlockPos(x, y, z), lens);
			} else if(burst.getTicksExisted() == placeTicks) {
				int x = net.minecraft.util.math.MathHelper.floor(entity.getX());
				int y = ItemNBTHelper.getInt(lens, TAG_Y_START, -1) + targetDistance;
				int z = net.minecraft.util.math.MathHelper.floor(entity.getZ());
				BlockPos pos = new BlockPos(x, y, z);

				if(entity.world.isAirBlock(pos)) {
					BlockState state = Blocks.AIR.getDefaultState();
					if (lens.hasTag() && lens.getTag().contains(TAG_STATE)) {
						state = NBTUtil.readBlockState(lens.getTag().getCompound(TAG_STATE));
					}

					TileEntity tile = null;
					CompoundNBT tilecmp = ItemNBTHelper.getCompound(lens, TAG_TILE, false);
					if(tilecmp.contains("id"))
						tile = TileEntity.create(tilecmp);

					entity.world.setBlockState(pos, state);
					entity.world.playEvent(2001, pos, Block.getStateId(state));
					if(tile != null) {
						tile.setPos(pos);
						entity.world.setTileEntity(pos, tile);
					}
				}

				entity.remove();
			}
		}
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		ThrowableEntity entity = (ThrowableEntity) burst;
		ItemStack lens = burst.getSourceLens();
		BlockState state = NBTUtil.readBlockState(lens.getOrCreateTag().getCompound(TAG_STATE));
		entity.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), entity.getX(), entity.getY(), entity.getZ(),
				entity.getMotion().getX(), entity.getMotion().getY(), entity.getMotion().getZ());

		return true;
	}

	@Override
	public boolean shouldPull(ItemStack stack) {
		return false;
	}

}
