/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemTwigWand extends Item implements ICoordBoundItem {

	private static final String TAG_COLOR1 = "color1";
	private static final String TAG_COLOR2 = "color2";
	private static final String TAG_BOUND_TILE_X = "boundTileX";
	private static final String TAG_BOUND_TILE_Y = "boundTileY";
	private static final String TAG_BOUND_TILE_Z = "boundTileZ";
	private static final String TAG_BIND_MODE = "bindMode";
	private static final BlockPos UNBOUND_POS = new BlockPos(0, -1, 0);

	public ItemTwigWand(Item.Settings builder) {
		super(builder);
	}

	private static boolean tryCompleteBinding(BlockPos src, ItemStack stack, ItemUsageContext ctx) {
		BlockPos dest = ctx.getBlockPos();
		if (!dest.equals(src)) {
			setBindingAttempt(stack, UNBOUND_POS);

			BlockEntity srcTile = ctx.getWorld().getBlockEntity(src);
			if (srcTile instanceof IWandBindable) {
				if (((IWandBindable) srcTile).bindTo(ctx.getPlayer(), stack, dest, ctx.getSide())) {
					doParticleBeamWithOffset(ctx.getWorld(), src, dest);
					setBindingAttempt(stack, UNBOUND_POS);
				}
				return true;
			}
		}
		return false;
	}

	private static boolean tryFormEnchanter(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		Direction.Axis axis = TileEnchanter.canEnchanterExist(world, pos);

		if (axis != null) {
			if (!world.isClient) {
				world.setBlockState(pos, ModBlocks.enchanter.getDefaultState().with(BotaniaStateProps.ENCHANTER_DIRECTION, axis));
				world.playSound(null, pos, ModSounds.enchanterForm, SoundCategory.BLOCKS, 0.5F, 0.6F);
				PlayerHelper.grantCriterion((ServerPlayerEntity) ctx.getPlayer(), prefix("main/enchanter_make"), "code_triggered");
			} else {
				for (int i = 0; i < 50; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();

					double x = (Math.random() - 0.5) * 6;
					double y = (Math.random() - 0.5) * 6;
					double z = (Math.random() - 0.5) * 6;

					float velMul = 0.07F;

					float motionx = (float) -x * velMul;
					float motiony = (float) -y * velMul;
					float motionz = (float) -z * velMul;
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, red, green, blue);
					world.addParticle(data, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, motionx, motiony, motionz);
				}
			}

			return true;
		}
		return false;
	}

	private static boolean tryCompletePistonRelayBinding(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		PlayerEntity player = ctx.getPlayer();

		GlobalPos bindPos = ((BlockPistonRelay) ModBlocks.pistonRelay).activeBindingAttempts.get(player.getUuid());
		if (bindPos != null && bindPos.getDimension() == world.getRegistryKey()) {
			((BlockPistonRelay) ModBlocks.pistonRelay).activeBindingAttempts.remove(player.getUuid());
			BlockPistonRelay.WorldData data = BlockPistonRelay.WorldData.get(world);
			data.mapping.put(bindPos.getPos(), pos.toImmutable());
			data.markDirty();

			PacketBotaniaEffect.sendNearby(world, pos, PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
				bindPos.getPos().getX() + 0.5, bindPos.getPos().getY() + 0.5, bindPos.getPos().getZ() + 0.5,
				pos.getX(), pos.getY(), pos.getZ());

			world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ding, SoundCategory.PLAYERS, 1F, 1F);
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		ItemStack stack = ctx.getStack();
		World world = ctx.getWorld();
		PlayerEntity player = ctx.getPlayer();
		BlockPos pos = ctx.getBlockPos();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Direction side = ctx.getSide();
		Optional<BlockPos> boundPos = getBindingAttempt(stack);

		if (player == null) {
			return ActionResult.PASS;
		}

		if (player.isSneaking()) {
			if (boundPos.isPresent() && tryCompleteBinding(boundPos.get(), stack, ctx)) {
				return ActionResult.SUCCESS;
			}

			if (player.canPlaceOn(pos, side, stack)
					&& (!(block instanceof CommandBlock) || player.isCreativeLevelTwoOp())) {
				BlockState newState = rotate(state, side.getAxis());
				if (newState != state) {
					world.setBlockState(pos, newState);
					return ActionResult.SUCCESS;
				}
			}
		}

		if (block == Blocks.LAPIS_BLOCK && ConfigHandler.COMMON.enchanterEnabled.getValue() && tryFormEnchanter(ctx)) {
			return ActionResult.SUCCESS;
		}

		if (block instanceof IWandable) {
			BlockEntity tile = world.getBlockEntity(pos);
			boolean bindable = tile instanceof IWandBindable;

			boolean wanded;
			if (getBindMode(stack) && bindable && player.isSneaking() && ((IWandBindable) tile).canSelect(player, stack, pos, side)) {
				if (boundPos.isPresent() && boundPos.get().equals(pos)) {
					setBindingAttempt(stack, UNBOUND_POS);
				} else {
					setBindingAttempt(stack, pos);
				}

				if (world.isClient) {
					player.playSound(ModSounds.ding, 0.11F, 1F);
				}

				wanded = true;
			} else {
				wanded = ((IWandable) block).onUsedByWand(player, stack, world, pos, side);
			}

			return wanded ? ActionResult.SUCCESS : ActionResult.FAIL;
		}

		if (!world.isClient && getBindMode(stack) && tryCompletePistonRelayBinding(ctx)) {
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	private static BlockState rotate(BlockState old, Direction.Axis axis) {
		for (Property<?> prop : old.getProperties()) {
			if (prop.getName().equals("facing") && prop.getType() == Direction.class) {
				@SuppressWarnings("unchecked")
				Property<Direction> facingProp = (Property<Direction>) prop;

				Direction oldDir = old.get(facingProp);
				Direction newDir = rotateAround(oldDir, axis);
				if (oldDir != newDir && facingProp.getValues().contains(newDir)) {
					return old.with(facingProp, newDir);
				}
			}
		}

		return old.rotate(BlockRotation.CLOCKWISE_90);
	}

	private static Direction rotateAround(Direction old, Direction.Axis axis) {
		switch (axis) {
		case X: {
			switch (old) {
			case DOWN:
				return Direction.SOUTH;
			case SOUTH:
				return Direction.UP;
			case UP:
				return Direction.NORTH;
			case NORTH:
				return Direction.DOWN;
			}
			break;
		}
		case Y: {
			switch (old) {
			case NORTH:
				return Direction.EAST;
			case EAST:
				return Direction.SOUTH;
			case SOUTH:
				return Direction.WEST;
			case WEST:
				return Direction.NORTH;
			}
			break;
		}
		case Z: {
			switch (old) {
			case DOWN:
				return Direction.WEST;
			case WEST:
				return Direction.UP;
			case UP:
				return Direction.EAST;
			case EAST:
				return Direction.DOWN;
			}
			break;
		}
		}

		return old;
	}

	public static void doParticleBeamWithOffset(World world, BlockPos orig, BlockPos end) {
		Vec3d origOffset = world.getBlockState(orig).getModelOffset(world, orig);
		Vector3 vorig = new Vector3(orig.getX() + origOffset.getX() + 0.5, orig.getY() + origOffset.getY() + 0.5, orig.getZ() + origOffset.getZ() + 0.5);
		Vec3d endOffset = world.getBlockState(end).getModelOffset(world, end);
		Vector3 vend = new Vector3(end.getX() + endOffset.getX() + 0.5, end.getY() + endOffset.getY() + 0.5, end.getZ() + endOffset.getZ() + 0.5);
		doParticleBeam(world, vorig, vend);
	}

	public static void doParticleBeam(World world, Vector3 orig, Vector3 end) {
		if (!world.isClient) {
			return;
		}

		Vector3 diff = end.subtract(orig);
		Vector3 movement = diff.normalize().multiply(0.05);
		int iters = (int) (diff.mag() / movement.mag());
		float huePer = 1F / iters;
		float hueSum = (float) Math.random();

		Vector3 currentPos = orig;
		for (int i = 0; i < iters; i++) {
			float hue = i * huePer + hueSum;
			int color = MathHelper.hsvToRgb(hue, 1F, 1F);
			float r = (color >> 16 & 0xFF) / 255F;
			float g = (color >> 8 & 0xFF) / 255F;
			float b = (color & 0xFF) / 255F;

			SparkleParticleData data = SparkleParticleData.noClip(0.5F, r, g, b, 4);
			world.addParticle(data, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
			currentPos = currentPos.add(movement);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		getBindingAttempt(stack).ifPresent(coords -> {
			BlockEntity tile = world.getBlockEntity(coords);
			if (!(tile instanceof IWandBindable)) {
				setBindingAttempt(stack, UNBOUND_POS);
			}
		});
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			if (!world.isClient) {
				setBindMode(stack, !getBindMode(stack));
			} else {
				player.playSound(ModSounds.ding, 0.1F, 1F);
			}
		}

		return TypedActionResult.success(stack);
	}

	@Override
	public void appendStacks(@Nonnull ItemGroup group, @Nonnull DefaultedList<ItemStack> stacks) {
		if (isIn(group)) {
			for (int i = 0; i < 16; i++) {
				stacks.add(forColors(i, i));
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		list.add(new TranslatableText(getModeString(stack)).formatted(Formatting.GRAY));
	}

	@Override
	public Text getHighlightTip(ItemStack stack, Text displayName) {
		Text mode = new LiteralText(" (")
				.append(new TranslatableText(getModeString(stack)).formatted(Formatting.DARK_GREEN))
				.append(")");
		return displayName.shallowCopy().append(mode);
	}

	public static ItemStack forColors(int color1, int color2) {
		ItemStack stack = new ItemStack(ModItems.twigWand);
		ItemNBTHelper.setInt(stack, TAG_COLOR1, color1);
		ItemNBTHelper.setInt(stack, TAG_COLOR2, color2);

		return stack;
	}

	public static int getColor1(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR1, 0);
	}

	public static int getColor2(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR2, 0);
	}

	public static void setBindingAttempt(ItemStack stack, BlockPos pos) {
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_X, pos.getX());
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Y, pos.getY());
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Z, pos.getZ());
	}

	public static Optional<BlockPos> getBindingAttempt(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Z, 0);
		return y < 0 ? Optional.empty() : Optional.of(new BlockPos(x, y, z));
	}

	public static boolean getBindMode(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_BIND_MODE, true);
	}

	public static void setBindMode(ItemStack stack, boolean bindMode) {
		ItemNBTHelper.setBoolean(stack, TAG_BIND_MODE, bindMode);
	}

	public static String getModeString(ItemStack stack) {
		return "botaniamisc.wandMode." + (getBindMode(stack) ? "bind" : "function");
	}

	@Nullable
	@Override
	public BlockPos getBinding(World world, ItemStack stack) {
		Optional<BlockPos> bound = getBindingAttempt(stack);
		if (bound.isPresent()) {
			return bound.get();
		}

		HitResult pos = MinecraftClient.getInstance().crosshairTarget;
		if (pos != null && pos.getType() == HitResult.Type.BLOCK) {
			BlockEntity tile = world.getBlockEntity(((BlockHitResult) pos).getBlockPos());
			if (tile instanceof ITileBound) {
				return ((ITileBound) tile).getBinding();
			}
		}

		return null;
	}

}
