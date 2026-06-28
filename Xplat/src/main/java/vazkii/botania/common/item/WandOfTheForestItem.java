/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.Bound;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.helper.WrenchingHelper;
import vazkii.botania.common.proxy.Proxy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WandOfTheForestItem extends Item implements CustomCreativeTabContents {

	public final ChatFormatting modeChatFormatting;

	public WandOfTheForestItem(ChatFormatting formatting, Item.Properties builder) {
		super(builder);
		this.modeChatFormatting = formatting;
	}

	private static boolean tryCompleteBinding(Level level, Player player, ItemStack wand, GlobalPos src, BlockPos target, Direction targetSide) {
		if (!target.equals(src.pos()) && src.dimension().equals(level.dimension())) {
			Optional<Direction> srcSide = getBindingSide(wand);
			setBindingAttempt(wand, null, null);

			WandBindable bindable = WandBindable.LOOKUP.find(level, src.pos(), srcSide.orElse(null));
			if (bindable != null) {
				if (bindable.bindTo(player, wand, target, targetSide)) {
					doParticleBeamWithOffset(level, src.pos(), target);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		BotaniaAPI.LOGGER.info("Wand used on {}", ctx.getClickedPos());
		Player player = ctx.getPlayer();

		if (player == null) {
			return InteractionResult.PASS;
		}

		ItemStack stack = ctx.getItemInHand();
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = world.getBlockState(pos);
		BlockEntity tile = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Direction side = ctx.getClickedFace();

		if (player.isSecondaryUseActive()) {
			Optional<GlobalPos> boundPos = getBindingAttempt(stack);
			if (boundPos.filter(loc -> tryCompleteBinding(world, player, stack, loc, pos, side)).isPresent()) {
				return InteractionResult.SUCCESS;
			}

			if (getBindMode(stack)) {
				WandBindable bindable = WandBindable.LOOKUP.find(world, pos, state, tile, side);
				if (bindable != null && bindable.canSelect(player, stack, side)) {
					GlobalPos globalPos = GlobalPos.of(world.dimension(), pos);
					if (boundPos.filter(globalPos::equals).isPresent()) {
						setBindingAttempt(stack, null, null);
					} else {
						setBindingAttempt(stack, globalPos, side);
					}

					if (world.isClientSide) {
						player.playSound(BotaniaSounds.ding, 0.11F, 1F);
					}

					return InteractionResult.SUCCESS;
				}
			}
		}

		var wandable = Wandable.LOOKUP.find(world, pos, state, tile, side);
		if (player.isSecondaryUseActive() && (wandable == null || getBindMode(stack))
				&& (!(state.getBlock() instanceof GameMasterBlock) || player.canUseGameMasterBlocks())) {
			Vec3 relativePos = ctx.getClickLocation().subtract(pos.getBottomCenter());
			BlockState newState = WrenchingHelper.manipulateBlockState(world, pos, state, side, relativePos);
			if (newState != null) {
				if (newState != state) {
					world.setBlockAndUpdate(pos, newState);
					world.playSound(player, pos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1F, 1F);
				}
				return InteractionResult.SUCCESS;
			}
		}

		if (wandable != null && wandable.onUsedByWand(player, stack, side)) {
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
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
			Proxy.INSTANCE.addParticleForceNear(world, data, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
			currentPos = currentPos.add(movement);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		getBindingAttempt(stack).ifPresent(pos -> {
			if (!pos.dimension().equals(world.dimension())
					|| WandBindable.LOOKUP.find(world, pos.pos(), getBindingSide(stack).orElse(null)) == null) {
				setBindingAttempt(stack, null, null);
			}
		});
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isSecondaryUseActive()) {
			if (!world.isClientSide) {
				setBindMode(stack, !getBindMode(stack));
			} else {
				player.playSound(BotaniaSounds.ding, 0.1F, 1F);
			}
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(me);
		List<Pair<DyeColor, DyeColor>> colorPairs = Arrays.asList(
				new Pair<>(DyeColor.WHITE, DyeColor.LIGHT_BLUE),
				new Pair<>(DyeColor.WHITE, DyeColor.PINK),
				new Pair<>(DyeColor.LIGHT_BLUE, DyeColor.PINK),
				new Pair<>(DyeColor.PURPLE, DyeColor.BLUE),
				new Pair<>(DyeColor.RED, DyeColor.RED),
				new Pair<>(DyeColor.BLUE, DyeColor.BLUE),
				new Pair<>(DyeColor.ORANGE, DyeColor.ORANGE),
				new Pair<>(DyeColor.BLACK, DyeColor.BLACK),
				new Pair<>(DyeColor.GRAY, DyeColor.LIGHT_GRAY),
				new Pair<>(DyeColor.PINK, DyeColor.PINK),
				new Pair<>(DyeColor.YELLOW, DyeColor.LIME),
				new Pair<>(DyeColor.WHITE, DyeColor.BLACK)
		);
		Collections.shuffle(colorPairs);
		for (int i = 0; i < 7; i++) {
			Pair<DyeColor, DyeColor> pair = colorPairs.get(i);
			if (Math.random() < 0.5) {
				pair = new Pair<>(pair.getSecond(), pair.getFirst());
			}
			output.accept(setColors(new ItemStack(me), pair.getFirst(), pair.getSecond()));
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		Component mode = Component.translatable(getModeString(stack)).withStyle(modeChatFormatting);
		Component name = super.getName(stack);
		return Component.translatable("botaniamisc.template.parenthesis_suffix", name, mode).withStyle(name.getStyle());
	}

	public static ItemStack setColors(ItemStack wand, DyeColor color1, DyeColor color2) {
		wand.set(BotaniaDataComponents.WAND_COLOR1, color1);
		wand.set(BotaniaDataComponents.WAND_COLOR2, color2);

		return wand;
	}

	public static DyeColor getColor1(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.WAND_COLOR1, DyeColor.WHITE);
	}

	public static DyeColor getColor2(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.WAND_COLOR2, DyeColor.WHITE);
	}

	public static void setBindingAttempt(ItemStack stack, @Nullable GlobalPos pos, @Nullable Direction side) {
		DataComponentHelper.setOptional(stack, BotaniaDataComponents.BINDING_POS, pos);
		DataComponentHelper.setOptional(stack, BotaniaDataComponents.BINDING_SIDE, side);
	}

	public static Optional<GlobalPos> getBindingAttempt(ItemStack stack) {
		return Optional.ofNullable(stack.get(BotaniaDataComponents.BINDING_POS));
	}

	public static Optional<Direction> getBindingSide(ItemStack stack) {
		return Optional.ofNullable(stack.get(BotaniaDataComponents.BINDING_SIDE));
	}

	public static boolean getBindMode(ItemStack stack) {
		return stack.has(BotaniaDataComponents.WAND_BIND_MODE);
	}

	public static void setBindMode(ItemStack stack, boolean bindMode) {
		DataComponentHelper.setFlag(stack, BotaniaDataComponents.WAND_BIND_MODE, bindMode);
	}

	public static String getModeString(ItemStack stack) {
		return "botaniamisc.wandMode." + (getBindMode(stack) ? "bind" : "function");
	}

	public static class CoordBoundItemImpl implements CoordBoundItem {
		private final ItemStack stack;

		public CoordBoundItemImpl(ItemStack stack) {
			this.stack = stack;
		}

		@Nullable
		@Override
		public BlockPos getBinding(Level world) {
			Optional<GlobalPos> bound = getBindingAttempt(stack);
			if (bound.isPresent() && bound.get().dimension().equals(world.dimension())) {
				return bound.get().pos();
			}

			var pos = ClientProxy.INSTANCE.getClientHit();
			if (pos instanceof BlockHitResult bHit && pos.getType() == HitResult.Type.BLOCK
					&& world.getBlockEntity(bHit.getBlockPos()) instanceof Bound boundTile) {
				return boundTile.getBinding();
			}

			return null;
		}
	}

}
