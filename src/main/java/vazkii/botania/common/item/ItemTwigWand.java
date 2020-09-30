/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
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

	public ItemTwigWand(Item.Properties builder) {
		super(builder);
	}

	private static boolean tryCompleteBinding(BlockPos src, ItemStack stack, ItemUseContext ctx) {
		BlockPos dest = ctx.getPos();
		if (!dest.equals(src)) {
			setBindingAttempt(stack, UNBOUND_POS);

			TileEntity srcTile = ctx.getWorld().getTileEntity(src);
			if (srcTile instanceof IWandBindable) {
				if (((IWandBindable) srcTile).bindTo(ctx.getPlayer(), stack, dest, ctx.getFace())) {
					doParticleBeamWithOffset(ctx.getWorld(), src, dest);
					setBindingAttempt(stack, UNBOUND_POS);
				}
				return true;
			}
		}
		return false;
	}

	private static boolean tryFormEnchanter(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		Direction.Axis axis = TileEnchanter.canEnchanterExist(world, pos);

		if (axis != null) {
			if (!world.isRemote) {
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

	private static boolean tryCompletePistonRelayBinding(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		PlayerEntity player = ctx.getPlayer();

		GlobalPos bindPos = ((BlockPistonRelay) ModBlocks.pistonRelay).activeBindingAttempts.get(player.getUniqueID());
		if (bindPos != null && bindPos.getDimension() == world.getDimensionKey()) {
			((BlockPistonRelay) ModBlocks.pistonRelay).activeBindingAttempts.remove(player.getUniqueID());
			BlockPistonRelay.WorldData data = BlockPistonRelay.WorldData.get(world);
			data.mapping.put(bindPos.getPos(), pos.toImmutable());
			data.markDirty();

			PacketHandler.sendToNearby(world, pos,
					new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
							bindPos.getPos().getX() + 0.5, bindPos.getPos().getY() + 0.5, bindPos.getPos().getZ() + 0.5,
							pos.getX(), pos.getY(), pos.getZ()));

			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ding, SoundCategory.PLAYERS, 1F, 1F);
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();
		World world = ctx.getWorld();
		PlayerEntity player = ctx.getPlayer();
		BlockPos pos = ctx.getPos();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Direction side = ctx.getFace();
		Optional<BlockPos> boundPos = getBindingAttempt(stack);

		if (player == null) {
			return ActionResultType.PASS;
		}

		if (player.isSneaking()) {
			if (boundPos.isPresent() && tryCompleteBinding(boundPos.get(), stack, ctx)) {
				return ActionResultType.SUCCESS;
			}

			if (player.canPlayerEdit(pos, side, stack)
					&& (!(block instanceof CommandBlockBlock) || player.canUseCommandBlock())) {
				BlockState newState = rotate(state, side.getAxis());
				if (newState != state) {
					world.setBlockState(pos, newState);
					return ActionResultType.SUCCESS;
				}
			}
		}

		if (block == Blocks.LAPIS_BLOCK && ConfigHandler.COMMON.enchanterEnabled.get() && tryFormEnchanter(ctx)) {
			return ActionResultType.SUCCESS;
		}

		if (block instanceof IWandable) {
			TileEntity tile = world.getTileEntity(pos);
			boolean bindable = tile instanceof IWandBindable;

			boolean wanded;
			if (getBindMode(stack) && bindable && player.isSneaking() && ((IWandBindable) tile).canSelect(player, stack, pos, side)) {
				if (boundPos.isPresent() && boundPos.get().equals(pos)) {
					setBindingAttempt(stack, UNBOUND_POS);
				} else {
					setBindingAttempt(stack, pos);
				}

				if (world.isRemote) {
					player.playSound(ModSounds.ding, 0.11F, 1F);
				}

				wanded = true;
			} else {
				wanded = ((IWandable) block).onUsedByWand(player, stack, world, pos, side);
			}

			return wanded ? ActionResultType.SUCCESS : ActionResultType.FAIL;
		}

		if (!world.isRemote && getBindMode(stack) && tryCompletePistonRelayBinding(ctx)) {
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	private static BlockState rotate(BlockState old, Direction.Axis axis) {
		for (Property<?> prop : old.getProperties()) {
			if (prop.getName().equals("facing") && prop.getValueClass() == Direction.class) {
				@SuppressWarnings("unchecked")
				Property<Direction> facingProp = (Property<Direction>) prop;

				Direction oldDir = old.get(facingProp);
				Direction newDir = rotateAround(oldDir, axis);
				if (oldDir != newDir && facingProp.getAllowedValues().contains(newDir)) {
					return old.with(facingProp, newDir);
				}
			}
		}

		return old.rotate(Rotation.CLOCKWISE_90);
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
		Vector3d origOffset = world.getBlockState(orig).getOffset(world, orig);
		Vector3 vorig = new Vector3(orig.getX() + origOffset.getX() + 0.5, orig.getY() + origOffset.getY() + 0.5, orig.getZ() + origOffset.getZ() + 0.5);
		Vector3d endOffset = world.getBlockState(end).getOffset(world, end);
		Vector3 vend = new Vector3(end.getX() + endOffset.getX() + 0.5, end.getY() + endOffset.getY() + 0.5, end.getZ() + endOffset.getZ() + 0.5);
		doParticleBeam(world, vorig, vend);
	}

	public static void doParticleBeam(World world, Vector3 orig, Vector3 end) {
		if (!world.isRemote) {
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
			int color = MathHelper.hsvToRGB(hue, 1F, 1F);
			float r = (color >> 16 & 0xFF) / 255F;
			float g = (color >> 8 & 0xFF) / 255F;
			float b = (color & 0xFF) / 255F;

			SparkleParticleData data = SparkleParticleData.noClip(0.5F, r, g, b, 4);
			Botania.proxy.addParticleForceNear(world, data, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
			currentPos = currentPos.add(movement);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		getBindingAttempt(stack).ifPresent(coords -> {
			TileEntity tile = world.getTileEntity(coords);
			if (!(tile instanceof IWandBindable)) {
				setBindingAttempt(stack, UNBOUND_POS);
			}
		});
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			if (!world.isRemote) {
				setBindMode(stack, !getBindMode(stack));
			} else {
				player.playSound(ModSounds.ding, 0.1F, 1F);
			}
		}

		return ActionResult.resultSuccess(stack);
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> stacks) {
		if (isInGroup(group)) {
			for (int i = 0; i < 16; i++) {
				stacks.add(forColors(i, i));
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		list.add(new TranslationTextComponent(getModeString(stack)).mergeStyle(TextFormatting.GRAY));
	}

	@Override
	public ITextComponent getHighlightTip(ItemStack stack, ITextComponent displayName) {
		ITextComponent mode = new StringTextComponent(" (")
				.append(new TranslationTextComponent(getModeString(stack)).mergeStyle(TextFormatting.DARK_GREEN))
				.appendString(")");
		return displayName.deepCopy().append(mode);
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

		RayTraceResult pos = Minecraft.getInstance().objectMouseOver;
		if (pos != null && pos.getType() == RayTraceResult.Type.BLOCK) {
			TileEntity tile = world.getTileEntity(((BlockRayTraceResult) pos).getPos());
			if (tile instanceof ITileBound) {
				return ((ITileBound) tile).getBinding();
			}
		}

		return null;
	}

}
