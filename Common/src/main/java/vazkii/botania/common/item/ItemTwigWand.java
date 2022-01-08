/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block.ITileBound;
import vazkii.botania.api.block.IWandBindable;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.network.EffectType;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.IXplatAbstractions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemTwigWand extends Item {

	private static final String TAG_COLOR1 = "color1";
	private static final String TAG_COLOR2 = "color2";
	private static final String TAG_BOUND_TILE_X = "boundTileX";
	private static final String TAG_BOUND_TILE_Y = "boundTileY";
	private static final String TAG_BOUND_TILE_Z = "boundTileZ";
	private static final String TAG_BIND_MODE = "bindMode";
	private static final BlockPos UNBOUND_POS = new BlockPos(0, Integer.MIN_VALUE, 0);

	public ItemTwigWand(Item.Properties builder) {
		super(builder);
	}

	private static boolean tryCompleteBinding(BlockPos src, ItemStack stack, UseOnContext ctx) {
		BlockPos dest = ctx.getClickedPos();
		if (!dest.equals(src)) {
			setBindingAttempt(stack, UNBOUND_POS);

			BlockEntity srcTile = ctx.getLevel().getBlockEntity(src);
			if (srcTile instanceof IWandBindable) {
				if (((IWandBindable) srcTile).bindTo(ctx.getPlayer(), stack, dest, ctx.getClickedFace())) {
					doParticleBeamWithOffset(ctx.getLevel(), src, dest);
					setBindingAttempt(stack, UNBOUND_POS);
				}
				return true;
			}
		}
		return false;
	}

	private static boolean tryFormEnchanter(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		Direction.Axis axis = TileEnchanter.canEnchanterExist(world, pos);

		if (axis != null) {
			if (!world.isClientSide) {
				world.setBlockAndUpdate(pos, ModBlocks.enchanter.defaultBlockState().setValue(BotaniaStateProps.ENCHANTER_DIRECTION, axis));
				world.playSound(null, pos, ModSounds.enchanterForm, SoundSource.BLOCKS, 1F, 1F);
				PlayerHelper.grantCriterion((ServerPlayer) ctx.getPlayer(), prefix("main/enchanter_make"), "code_triggered");
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

	private static boolean tryCompletePistonRelayBinding(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		Player player = ctx.getPlayer();

		GlobalPos bindPos = ((BlockPistonRelay) ModBlocks.pistonRelay).activeBindingAttempts.get(player.getUUID());
		if (bindPos != null && bindPos.dimension() == world.dimension()) {
			((BlockPistonRelay) ModBlocks.pistonRelay).activeBindingAttempts.remove(player.getUUID());
			BlockPistonRelay.WorldData data = BlockPistonRelay.WorldData.get(world);
			data.mapping.put(bindPos.pos(), pos.immutable());
			data.setDirty();

			IXplatAbstractions.INSTANCE.sendEffectPacketNear(world, pos, EffectType.PARTICLE_BEAM,
					bindPos.pos().getX() + 0.5, bindPos.pos().getY() + 0.5, bindPos.pos().getZ() + 0.5,
					pos.getX(), pos.getY(), pos.getZ());

			world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ding, SoundSource.PLAYERS, 1F, 1F);
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();
		Level world = ctx.getLevel();
		Player player = ctx.getPlayer();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Direction side = ctx.getClickedFace();
		Optional<BlockPos> boundPos = getBindingAttempt(stack);

		if (player == null) {
			return InteractionResult.PASS;
		}

		if (player.isShiftKeyDown()) {
			if (boundPos.filter(loc -> tryCompleteBinding(loc, stack, ctx)).isPresent()) {
				return InteractionResult.SUCCESS;
			}

			if (player.mayUseItemAt(pos, side, stack)
					&& (!(block instanceof CommandBlock) || player.canUseGameMasterBlocks())) {
				BlockState newState = rotate(state, side.getAxis());
				if (newState != state) {
					world.setBlockAndUpdate(pos, newState);
					return InteractionResult.SUCCESS;
				}
			}
		}

		if (state.is(Blocks.LAPIS_BLOCK) && BotaniaConfig.common().enchanterEnabled() && tryFormEnchanter(ctx)) {
			return InteractionResult.SUCCESS;
		}

		BlockEntity tile = world.getBlockEntity(pos);
		boolean bindable = tile instanceof IWandBindable;

		if (getBindMode(stack) && bindable && player.isShiftKeyDown() && ((IWandBindable) tile).canSelect(player, stack, pos, side)) {
			if (boundPos.filter(pos::equals).isPresent()) {
				setBindingAttempt(stack, UNBOUND_POS);
			} else {
				setBindingAttempt(stack, pos);
			}

			if (world.isClientSide) {
				player.playSound(ModSounds.ding, 0.11F, 1F);
			}

			return InteractionResult.SUCCESS;
		} else {
			var wandable = IXplatAbstractions.INSTANCE.findWandable(world, pos, state, tile);
			if (wandable != null) {
				return wandable.onUsedByWand(player, stack, side) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
			}
		}

		if (!world.isClientSide && getBindMode(stack) && tryCompletePistonRelayBinding(ctx)) {
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	private static BlockState rotate(BlockState old, Direction.Axis axis) {
		for (Property<?> prop : old.getProperties()) {
			if (prop.getName().equals("facing") && prop.getValueClass() == Direction.class) {
				@SuppressWarnings("unchecked")
				Property<Direction> facingProp = (Property<Direction>) prop;

				Direction oldDir = old.getValue(facingProp);
				Direction newDir = rotateAround(oldDir, axis);
				if (oldDir != newDir && facingProp.getPossibleValues().contains(newDir)) {
					return old.setValue(facingProp, newDir);
				}
			}
		}

		return old.rotate(Rotation.CLOCKWISE_90);
	}

	private static Direction rotateAround(Direction old, Direction.Axis axis) {
		return switch (axis) {
			case X -> switch (old) {
					case DOWN -> Direction.SOUTH;
					case SOUTH -> Direction.UP;
					case UP -> Direction.NORTH;
					case NORTH -> Direction.DOWN;
					default -> old;
				};
			case Y -> switch (old) {
					case NORTH -> Direction.EAST;
					case EAST -> Direction.SOUTH;
					case SOUTH -> Direction.WEST;
					case WEST -> Direction.NORTH;
					default -> old;
				};
			case Z -> switch (old) {
					case DOWN -> Direction.WEST;
					case WEST -> Direction.UP;
					case UP -> Direction.EAST;
					case EAST -> Direction.DOWN;
					default -> old;
				};
		};
	}

	public static void doParticleBeamWithOffset(Level world, BlockPos orig, BlockPos end) {
		Vec3 origOffset = world.getBlockState(orig).getOffset(world, orig);
		Vec3 vorig = new Vec3(orig.getX() + origOffset.x() + 0.5, orig.getY() + origOffset.y() + 0.5, orig.getZ() + origOffset.z() + 0.5);
		Vec3 endOffset = world.getBlockState(end).getOffset(world, end);
		Vec3 vend = new Vec3(end.getX() + endOffset.x() + 0.5, end.getY() + endOffset.y() + 0.5, end.getZ() + endOffset.z() + 0.5);
		doParticleBeam(world, vorig, vend);
	}

	public static void doParticleBeam(Level world, Vec3 orig, Vec3 end) {
		if (!world.isClientSide) {
			return;
		}

		Vec3 diff = end.subtract(orig);
		Vec3 movement = diff.normalize().scale(0.05);
		int iters = (int) (diff.length() / movement.length());
		float huePer = 1F / iters;
		float hueSum = (float) Math.random();

		Vec3 currentPos = orig;
		for (int i = 0; i < iters; i++) {
			float hue = i * huePer + hueSum;
			int color = Mth.hsvToRgb(Mth.frac(hue), 1F, 1F);
			float r = (color >> 16 & 0xFF) / 255F;
			float g = (color >> 8 & 0xFF) / 255F;
			float b = (color & 0xFF) / 255F;

			SparkleParticleData data = SparkleParticleData.noClip(0.5F, r, g, b, 4);
			IProxy.INSTANCE.addParticleForceNear(world, data, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
			currentPos = currentPos.add(movement);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		getBindingAttempt(stack).ifPresent(coords -> {
			BlockEntity tile = world.getBlockEntity(coords);
			if (!(tile instanceof IWandBindable)) {
				setBindingAttempt(stack, UNBOUND_POS);
			}
		});
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			if (!world.isClientSide) {
				setBindMode(stack, !getBindMode(stack));
			} else {
				player.playSound(ModSounds.ding, 0.1F, 1F);
			}
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> stacks) {
		if (allowdedIn(group)) {
			for (int i = 0; i < 16; i++) {
				stacks.add(forColors(i, i));
			}
		}
	}

	@Override
	public Component getName(@Nonnull ItemStack stack) {
		Component mode = new TextComponent(" (")
				.append(new TranslatableComponent(getModeString(stack)).withStyle(ChatFormatting.DARK_GREEN))
				.append(")");
		return super.getName(stack).plainCopy().append(mode);
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
		int y = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Y, Integer.MIN_VALUE);
		int z = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Z, 0);
		return y == Integer.MIN_VALUE ? Optional.empty() : Optional.of(new BlockPos(x, y, z));
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

	public static class CoordBoundItem implements ICoordBoundItem {
		private final ItemStack stack;

		public CoordBoundItem(ItemStack stack) {
			this.stack = stack;
		}

		@Nullable
		@Override
		public BlockPos getBinding(Level world) {
			Optional<BlockPos> bound = getBindingAttempt(stack);
			if (bound.isPresent()) {
				return bound.get();
			}

			HitResult pos = Minecraft.getInstance().hitResult;
			if (pos != null && pos.getType() == HitResult.Type.BLOCK) {
				BlockEntity tile = world.getBlockEntity(((BlockHitResult) pos).getBlockPos());
				if (tile instanceof ITileBound) {
					return ((ITileBound) tile).getBinding();
				}
			}

			return null;
		}
	}

}
