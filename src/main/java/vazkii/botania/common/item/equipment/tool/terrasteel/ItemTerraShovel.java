/**
 * This class was created by <ToMe25>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 30, 2019, 9:35 (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShovel;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.item.relic.ItemThorRing;

public class ItemTerraShovel extends ItemManasteelShovel implements ISequentialBreaker {

	private static final String TAG_ENABLED = "enabled";

	/**
	 * The amount of mana required to restore 1 point of damage.
	 */
	private static final int MANA_PER_DAMAGE = 100;
	
	/**
	 * The amount of mana required to fertilize 1 crop.
	 */
	private static final int MANA_PER_BONEMEAL = 300;

	public ItemTerraShovel(Properties props) {
		super(BotaniaAPI.TERRASTEEL_ITEM_TIER, props);
		addPropertyOverride(new ResourceLocation("botania", TAG_ENABLED), (itemStack, world, entityLivingBase) -> isEnabled(itemStack) ? 1 : 0);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
		RayTraceResult raycast = ToolCommons.raytraceFromEntity(player.world, player, RayTraceContext.FluidMode.NONE, 10);
		if (raycast.getType() == RayTraceResult.Type.BLOCK) {
			Direction face = ((BlockRayTraceResult) raycast).getFace();
			breakOtherBlock(player, stack, pos, pos, face);
			ItemLokiRing.breakOnAllCursors(player, this, stack, pos, face);
		}

		return false;
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(PlayerEntity player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side) {
		if(player.isSneaking() || !isEnabled(stack))
			return;

		World world = player.world;
		if (world.getBlockState(pos).getHarvestTool() != net.minecraftforge.common.ToolType.SHOVEL && !(world.getBlockState(pos).getBlock() instanceof IGrowable))
			return;

		if (world.isAirBlock(pos))
			return;

		boolean crop = world.getBlockState(pos).getBlock() instanceof IGrowable;
		boolean thor = !ItemThorRing.getThorRing(player).isEmpty();
		boolean doX = thor || side.getXOffset() == 0;
		boolean doY = (thor || side.getYOffset() == 0) && !crop;
		boolean doZ = thor || side.getZOffset() == 0;

		int range = 2 + (thor ? 1 : 0);
		if (ItemTemperanceStone.hasTemperanceActive(player))
			range = 1;

		int rangeY = Math.max(1, range);

		Vec3i beginDiff = new Vec3i(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
		Vec3i endDiff = new Vec3i(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);
		Predicate<BlockState> filter = state -> state.getHarvestTool() == net.minecraftforge.common.ToolType.SHOVEL;
		if(crop)
			filter = state -> state.getBlock() instanceof IGrowable;

		ToolCommons.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, filter, false);
	}
	
	public static boolean isEnabled(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ENABLED, false);
	}

	void setEnabled(ItemStack stack, boolean enabled) {
		ItemNBTHelper.setBoolean(stack, TAG_ENABLED, enabled);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		setEnabled(stack, !isEnabled(stack));
		if(!world.isRemote)
			world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.terraPickMode, SoundCategory.PLAYERS, 0.5F, 0.4F);
		return ActionResult.newResult(ActionResultType.SUCCESS, stack);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();
		PlayerEntity player = ctx.getPlayer();
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		if(player == null || !player.canPlayerEdit(pos, ctx.getFace(), stack))
			return ActionResultType.PASS;
		else {
			Block block = world.getBlockState(pos).getBlock();

			int range = 2 + (!ItemThorRing.getThorRing(player).isEmpty() ? 1 : 0);
			if (ItemTemperanceStone.hasTemperanceActive(player))
				range = 1;
			if(player.isSneaking() || !isEnabled(stack))
				range = 0;

			int startX = pos.getX() - range;
			int endX = pos.getX() + range;
			int endZ = pos.getZ() + range;

			if(block == Blocks.DIRT || block == Blocks.GRASS_PATH || block == Blocks.COARSE_DIRT || block == Blocks.FARMLAND) {
				BlockPos pos1 = new BlockPos(startX, pos.getY(), pos.getZ() - range);
				ActionResultType result = ActionResultType.PASS;
				while(pos1.getZ() <= endZ) {
					while(pos1.getX() <= endX) {
						if(pos1.equals(pos)) {
							result = tillBlock(pos1, ctx);
							if(world.getBlockState(pos1).getBlock() == Blocks.FARMLAND) {
								result = ActionResultType.SUCCESS;
							}
						}
						else
							tillBlock(pos1, ctx);
						pos1 = pos1.add(1, 0, 0);
					}
					pos1 = new BlockPos(startX, pos1.getY(), pos1.getZ() + 1);
				}
				return world.getBlockState(pos1).getBlock() == Blocks.FARMLAND ? ActionResultType.SUCCESS : result;
			}

			if(ctx.getFace() != Direction.DOWN && world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up()) && block == Blocks.GRASS_BLOCK) {
				Block block1 = Blocks.GRASS_PATH;

				world.playSound(null, pos, block1.getDefaultState().getSoundType().getStepSound(),
						SoundCategory.BLOCKS,
						(block1.getDefaultState().getSoundType().getVolume() + 1.0F) / 2.0F,
						block1.getDefaultState().getSoundType().getPitch() * 0.8F);

				if (world.isRemote)
					return ActionResultType.SUCCESS;
				else {
					BlockPos pos1 = new BlockPos(startX, pos.getY(), pos.getZ() - range);
					while(pos1.getZ() <= endZ) {
						while(pos1.getX() <= endX) {
							if(world.getBlockState(pos1).getBlock() == Blocks.GRASS_BLOCK && world.getBlockState(pos1.up()).getBlock().isAir(world.getBlockState(pos1.up()), world, pos1.up()) && player.canPlayerEdit(pos1, ctx.getFace(), stack))
								world.setBlockState(pos1, block1.getDefaultState());
							pos1 = pos1.add(1, 0, 0);
						}
						pos1 = new BlockPos(startX, pos1.getY(), pos1.getZ() + 1);
					}
					ToolCommons.damageItem(stack, 1, player, getManaPerDamage());
					return ActionResultType.SUCCESS;
				}
			}

			if(block instanceof IGrowable) {
				BlockPos pos1 = new BlockPos(startX, pos.getY(), pos.getZ() - range);
				boolean success = false;
				while(pos1.getZ() <= endZ) {
					while(pos1.getX() <= endX) {
						success = success || bonemealCrop(ctx, pos1) == ActionResultType.SUCCESS;
						pos1 = pos1.add(1, 0, 0);
					}
					pos1 = new BlockPos(startX, pos1.getY(), pos1.getZ() + 1);
				}
				return success ? ActionResultType.SUCCESS : ActionResultType.FAIL;
			}

			return ActionResultType.PASS;
		}
	}

	@Override
	public boolean disposeOfTrashBlocks(ItemStack stack) {
		return false;
	}

	private ActionResultType tillBlock(BlockPos pos, ItemUseContext ctx) {
		PlayerEntity player = ctx.getPlayer();
		ItemStack stack = ctx.getItem();
		World world = ctx.getWorld();
		if(player != null && player.canPlayerEdit(pos, ctx.getFace(), stack)) {
			BlockRayTraceResult trace = new BlockRayTraceResult(new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D), ctx.getFace(), pos, false);
			ItemUseContext ctx1 = new ItemUseContext(world, player, ctx.getHand(), stack, trace);
			UseHoeEvent event = new UseHoeEvent(ctx1);
			if(MinecraftForge.EVENT_BUS.post(event))
				return ActionResultType.FAIL;

			if(event.getResult() == Event.Result.ALLOW) {
				ToolCommons.damageItem(stack, 1, player, getManaPerDamage());
				return ActionResultType.SUCCESS;
			}

			Block block = world.getBlockState(pos).getBlock();

			if(ctx.getFace() != Direction.DOWN && world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up()) &&
					(block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.GRASS_PATH || block == Blocks.COARSE_DIRT)) {
				Block block1 = Blocks.FARMLAND;
				if(block == Blocks.COARSE_DIRT)
					block1 = Blocks.DIRT;

				world.playSound(null, pos, block1.getDefaultState().getSoundType().getStepSound(),
						SoundCategory.BLOCKS,
						(block1.getDefaultState().getSoundType().getVolume() + 1.0F) / 2.0F,
						block1.getDefaultState().getSoundType().getPitch() * 0.8F);

				if (world.isRemote)
					return ActionResultType.SUCCESS;
				else {
					world.setBlockState(pos, block1.getDefaultState());
					ToolCommons.damageItem(stack, 1, player, getManaPerDamage());
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack before, @Nonnull ItemStack after, boolean slotChanged) {
		return after.getItem() != this || isEnabled(before) != isEnabled(after);
	}

	private ActionResultType bonemealCrop(ItemUseContext ctx, BlockPos pos) {
		if(!ManaItemHandler.requestManaExactForTool(ctx.getItem(), (PlayerEntity) ctx.getPlayer(), MANA_PER_BONEMEAL, false))
			return ActionResultType.PASS;
		World world = ctx.getWorld();
		BlockPos pos1 = pos.offset(ctx.getFace());
		if (applyBonemeal(ctx.getItem(), world, pos, ctx.getPlayer())) {
			if (!world.isRemote) {
				world.playEvent(2005, pos, 0);
			}

			ManaItemHandler.requestManaExactForTool(ctx.getItem(), (PlayerEntity) ctx.getPlayer(), MANA_PER_BONEMEAL, true);
			return ActionResultType.SUCCESS;
		} else {
			BlockState blockstate = world.getBlockState(pos);
			boolean flag = blockstate.func_224755_d(world, pos, ctx.getFace());
			if (flag && growSeagrass(ctx.getItem(), world, pos1, ctx.getFace())) {
				if (!world.isRemote) {
					world.playEvent(2005, pos1, 0);
				}

				ManaItemHandler.requestManaExactForTool(ctx.getItem(), (PlayerEntity) ctx.getPlayer(), MANA_PER_BONEMEAL, true);
				return ActionResultType.SUCCESS;
			} else {
				return ActionResultType.PASS;
			}
		}
	}

	// [VanillaCopy] of BoneMealItem.applyBonemeal, edits noted
	public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos pos, net.minecraft.entity.player.PlayerEntity player) {
		BlockState blockstate = worldIn.getBlockState(pos);
		int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, pos, blockstate, stack);
		if (hook != 0) return hook > 0;
		if (blockstate.getBlock() instanceof IGrowable) {
			IGrowable igrowable = (IGrowable)blockstate.getBlock();
			if (igrowable.canGrow(worldIn, pos, blockstate, worldIn.isRemote)) {
				if (!worldIn.isRemote) {
					if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos, blockstate)) {
						igrowable.grow(worldIn, worldIn.rand, pos, blockstate);
					}
					//remove stack.shrink to prevent the shovel from getting deleted.
				}

				return true;
			}
		}

		return false;
	}

	// [VanillaCopy] of BoneMealItem.growSeagrass, edits noted
	public static boolean growSeagrass(ItemStack stack, World worldIn, BlockPos pos, @Nullable Direction side) {
		if (worldIn.getBlockState(pos).getBlock() == Blocks.WATER && worldIn.getFluidState(pos).getLevel() == 8) {
			if (!worldIn.isRemote) {
				label79:
					for(int i = 0; i < 128; ++i) {
						BlockPos blockpos = pos;
						Biome biome = worldIn.getBiome(pos);
						BlockState blockstate = Blocks.SEAGRASS.getDefaultState();

						for(int j = 0; j < i / 16; ++j) {
							blockpos = blockpos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
							biome = worldIn.getBiome(blockpos);
							if (worldIn.getBlockState(blockpos).func_224756_o(worldIn, blockpos)) {
								continue label79;
							}
						}

						if (net.minecraftforge.common.BiomeDictionary.hasType(biome, net.minecraftforge.common.BiomeDictionary.Type.OCEAN)
								&& net.minecraftforge.common.BiomeDictionary.hasType(biome, net.minecraftforge.common.BiomeDictionary.Type.HOT)) {
							if (i == 0 && side != null && side.getAxis().isHorizontal()) {
								blockstate = BlockTags.WALL_CORALS.getRandomElement(worldIn.rand).getDefaultState().with(DeadCoralWallFanBlock.FACING, side);
							} else if (random.nextInt(4) == 0) {
								blockstate = BlockTags.UNDERWATER_BONEMEALS.getRandomElement(random).getDefaultState();
							}
						}

						if (blockstate.getBlock().isIn(BlockTags.WALL_CORALS)) {
							for(int k = 0; !blockstate.isValidPosition(worldIn, blockpos) && k < 4; ++k) {
								blockstate = blockstate.with(DeadCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.random(random));
							}
						}

						if (blockstate.isValidPosition(worldIn, blockpos)) {
							BlockState blockstate1 = worldIn.getBlockState(blockpos);
							if (blockstate1.getBlock() == Blocks.WATER && worldIn.getFluidState(blockpos).getLevel() == 8) {
								worldIn.setBlockState(blockpos, blockstate, 3);
							} else if (blockstate1.getBlock() == Blocks.SEAGRASS && random.nextInt(10) == 0) {
								((IGrowable)Blocks.SEAGRASS).grow(worldIn, random, blockpos, blockstate1);
							}
						}
					}
				//remove stack.shrink to prevent the shovel from getting deleted.
			}

			return true;
		} else {
			return false;
		}
	}

}
