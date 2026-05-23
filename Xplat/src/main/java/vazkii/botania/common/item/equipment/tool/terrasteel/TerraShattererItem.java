/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.SequentialBreaker;
import vazkii.botania.api.item.SpecialBlockBreakingHandler;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.StoneOfTemperanceItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelPickaxeItem;
import vazkii.botania.common.item.relic.RingOfThorItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class TerraShattererItem extends ManasteelPickaxeItem implements SequentialBreaker, CustomCreativeTabContents,
		SpecialBlockBreakingHandler {

	public static final int MAX_MANA = Integer.MAX_VALUE;
	private static final int MANA_PER_DAMAGE = 100;

	public static final int[] LEVELS = new int[] {
			0, 10000, 1000000, 10000000, 100000000, 1000000000
	};

	private static final int[] CREATIVE_MANA = new int[] {
			10000 - 1, 1000000 - 1, 10000000 - 1, 100000000 - 1, 1000000000 - 1, MAX_MANA - 1
	};

	public TerraShattererItem(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props, -2.8F);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(me);
		for (int mana : CREATIVE_MANA) {
			ItemStack stack = new ItemStack(me);
			setMana(stack, mana);
			output.accept(stack);
		}
		ItemStack stack = new ItemStack(me);
		setMana(stack, CREATIVE_MANA[1]);
		setTipped(stack);
		output.accept(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> stacks, TooltipFlag flags) {
		Component rank = Component.translatable("botania.rank" + getLevel(stack));
		Component rankFormat = Component.translatable("botaniamisc.toolRank", rank);
		stacks.add(rankFormat);
		if (getMana_(stack) == Integer.MAX_VALUE) {
			stacks.add(Component.translatable("botaniamisc.getALife").withStyle(ChatFormatting.RED));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int tier = getLevel(stack);
		if (!player.isSecondaryUseActive() || tier == 0) {
			return InteractionResultHolder.pass(stack);
		}
		if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty()) {
			BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

			if (blockhitresult.getType() == HitResult.Type.BLOCK) {
				return InteractionResultHolder.pass(stack);
			}
		}
		setEnabled(stack, !isEnabled(stack));
		if (!level.isClientSide()) {
			level.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.terraPickMode, SoundSource.PLAYERS, 1F, 1F);
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player player = ctx.getPlayer();
		if (player == null) {
			return super.useOn(ctx);
		} else if (ctx.getHand() == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty()) {
			return InteractionResult.PASS;
		}
		return !player.isSecondaryUseActive() || getLevel(ctx.getItemInHand()) == 0
				? super.useOn(ctx)
				: InteractionResult.PASS;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (isEnabled(stack)) {
			int level = getLevel(stack);

			if (level == 0) {
				setEnabled(stack, false);
			} else if (entity instanceof Player player && !player.swinging) {
				var manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);
				manaItem.addMana(-level);
			}
		}
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		int level = getLevel(stack);
		int max = LEVELS[Math.min(LEVELS.length - 1, level + 1)];
		int curr = getMana_(stack);
		float percent = level == 0 ? 0F : (float) curr / (float) max;

		return Optional.of(new ManaBarTooltip(percent, level));
	}

	@Override
	public void onBlockStartBreak(ServerLevel level, ItemStack stack, BlockPos pos, Player player) {
		BlockHitResult raycast = ToolCommons.raytraceFromEntity(player, 10, false);
		if (!player.level().isClientSide() && raycast.getType() == HitResult.Type.BLOCK) {
			Direction face = raycast.getDirection();
			breakOtherBlock(player, stack, pos, pos, face);
			if (player.isSecondaryUseActive()) {
				BotaniaAPI.instance().breakOnAllCursors(player, stack, pos, face);
			}
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(Player player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side) {
		if (!isEnabled(stack)) {
			return;
		}

		Level world = player.level();
		Predicate<BlockState> canMine = state -> {
			boolean rightToolForDrops = !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
			boolean rightToolForSpeed = stack.getDestroySpeed(state) > 1
					|| state.is(BlockTags.MINEABLE_WITH_SHOVEL)
					|| state.is(BlockTags.MINEABLE_WITH_HOE);
			return rightToolForDrops && rightToolForSpeed;
		};

		BlockState targetState = world.getBlockState(pos);
		if (!canMine.test(targetState)) {
			return;
		}

		if (world.isEmptyBlock(pos)) {
			return;
		}

		boolean thor = !RingOfThorItem.getThorRing(player).isEmpty();
		boolean doX = thor || side.getStepX() == 0;
		boolean doY = thor || side.getStepY() == 0;
		boolean doZ = thor || side.getStepZ() == 0;

		int origLevel = getLevel(stack);
		int level = origLevel + (thor ? 1 : 0);
		if (StoneOfTemperanceItem.hasTemperanceActive(player) && level > 2) {
			level = 2;
		}

		int range = level - 1;
		int rangeY = Math.max(1, range);

		if (range == 0 && level != 1) {
			return;
		}

		Vec3i beginDiff = new Vec3i(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
		Vec3i endDiff = new Vec3i(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

		ToolCommons.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, canMine);

		if (origLevel == 5) {
			PlayerHelper.grantCriterion((ServerPlayer) player, botaniaRL("challenge/rank_ss_pick"), "code_triggered");
		}
	}

	public static boolean isTipped(ItemStack stack) {
		return stack.has(BotaniaDataComponents.ELEMENTIUM_TIPPED);
	}

	public static void setTipped(ItemStack stack) {
		stack.set(BotaniaDataComponents.ELEMENTIUM_TIPPED, Unit.INSTANCE);
	}

	public static boolean isEnabled(ItemStack stack) {
		return stack.has(BotaniaDataComponents.ACTIVE);
	}

	void setEnabled(ItemStack stack, boolean enabled) {
		DataComponentHelper.setFlag(stack, BotaniaDataComponents.ACTIVE, enabled);
	}

	protected static void setMana(ItemStack stack, int mana) {
		stack.set(BotaniaDataComponents.MANA, mana);
		int level = getLevel(stack);
		Rarity targetRarity = level >= 5 ? Rarity.EPIC : level >= 3 ? Rarity.RARE : Rarity.UNCOMMON;
		if (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON) != targetRarity) {
			stack.set(DataComponents.RARITY, targetRarity);
		}
	}

	public static int getMana_(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.MANA, 0);
	}

	public static int getLevel(ItemStack stack) {
		int mana = getMana_(stack);
		for (int i = LEVELS.length - 1; i > 0; i--) {
			if (mana >= LEVELS[i]) {
				return i;
			}
		}

		return 0;
	}

	@SoftImplement("IItemExtension")
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || reequipAnimation(oldStack, newStack);
	}

	@SoftImplement("FabricItem")
	public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return reequipAnimation(oldStack, newStack);
	}

	private boolean reequipAnimation(ItemStack before, ItemStack after) {
		return isEnabled(before) != isEnabled(after)
				|| !Objects.equals(before.get(DataComponents.DAMAGE), after.get(DataComponents.DAMAGE));
	}
}
