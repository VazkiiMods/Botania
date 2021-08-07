/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;
import vazkii.botania.common.item.relic.ItemThorRing;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemTerraPick extends ItemManasteelPick implements IManaItem, ISequentialBreaker {

	private static final String TAG_ENABLED = "enabled";
	private static final String TAG_MANA = "mana";
	private static final String TAG_TIPPED = "tipped";

	private static final int MAX_MANA = Integer.MAX_VALUE;
	private static final int MANA_PER_DAMAGE = 100;

	private static final Set<Material> MATERIALS = ImmutableSet.of(Material.STONE, Material.METAL, Material.ICE,
			Material.GLASS, Material.PISTON, Material.HEAVY_METAL, Material.GRASS, Material.DIRT, Material.SAND,
			Material.TOP_SNOW, Material.SNOW, Material.CLAY);

	public static final int[] LEVELS = new int[] {
			0, 10000, 1000000, 10000000, 100000000, 1000000000
	};

	private static final int[] CREATIVE_MANA = new int[] {
			10000 - 1, 1000000 - 1, 10000000 - 1, 100000000 - 1, 1000000000 - 1, MAX_MANA - 1
	};

	public ItemTerraPick(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props, -2.8F);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			for (int mana : CREATIVE_MANA) {
				ItemStack stack = new ItemStack(this);
				setMana(stack, mana);
				list.add(stack);
			}
			ItemStack stack = new ItemStack(this);
			setMana(stack, CREATIVE_MANA[1]);
			setTipped(stack);
			list.add(stack);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> stacks, TooltipFlag flags) {
		Component rank = new TranslatableComponent("botania.rank" + getLevel(stack));
		Component rankFormat = new TranslatableComponent("botaniamisc.toolRank", rank);
		stacks.add(rankFormat);
		if (getMana(stack) == Integer.MAX_VALUE) {
			stacks.add(new TranslatableComponent("botaniamisc.getALife").withStyle(ChatFormatting.RED));
		}
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		getMana(stack);
		int level = getLevel(stack);

		if (level != 0) {
			setEnabled(stack, !isEnabled(stack));
			if (!world.isClientSide) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.terraPickMode, SoundSource.PLAYERS, 0.5F, 0.4F);
			}
		}

		return InteractionResultHolder.success(stack);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return ctx.getPlayer() == null || ctx.getPlayer().isShiftKeyDown() ? super.useOn(ctx)
				: InteractionResult.PASS;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (isEnabled(stack)) {
			int level = getLevel(stack);

			if (level == 0) {
				setEnabled(stack, false);
			} else if (entity instanceof Player && !((Player) entity).swinging) {
				addMana(stack, -level);
			}
		}
	}

	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
		BlockHitResult raycast = ToolCommons.raytraceFromEntity(player, 10, false);
		if (!player.level.isClientSide && raycast.getType() == HitResult.Type.BLOCK) {
			Direction face = raycast.getDirection();
			breakOtherBlock(player, stack, pos, pos, face);
			if (player.isSecondaryUseActive()) {
				BotaniaAPI.instance().breakOnAllCursors(player, stack, pos, face);
			}
		}

		return false;
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

		Level world = player.level;
		BlockState targetState = world.getBlockState(pos);
		if (stack.getDestroySpeed(targetState) <= 1.0F && !MATERIALS.contains(targetState.getMaterial())) {
			return;
		}

		if (world.isEmptyBlock(pos)) {
			return;
		}

		boolean thor = !ItemThorRing.getThorRing(player).isEmpty();
		boolean doX = thor || side.getStepX() == 0;
		boolean doY = thor || side.getStepY() == 0;
		boolean doZ = thor || side.getStepZ() == 0;

		int origLevel = getLevel(stack);
		int level = origLevel + (thor ? 1 : 0);
		if (ItemTemperanceStone.hasTemperanceActive(player) && level > 2) {
			level = 2;
		}

		int range = level - 1;
		int rangeY = Math.max(1, range);

		if (range == 0 && level != 1) {
			return;
		}

		Vec3i beginDiff = new Vec3i(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
		Vec3i endDiff = new Vec3i(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

		ToolCommons.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff,
				state -> stack.getDestroySpeed(state) > 1.0F || MATERIALS.contains(state.getMaterial()));

		if (origLevel == 5) {
			PlayerHelper.grantCriterion((ServerPlayer) player, prefix("challenge/rank_ss_pick"), "code_triggered");
		}
	}

	public static boolean isTipped(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_TIPPED, false);
	}

	public static void setTipped(ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_TIPPED, true);
	}

	public static boolean isEnabled(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ENABLED, false);
	}

	void setEnabled(ItemStack stack, boolean enabled) {
		ItemNBTHelper.setBoolean(stack, TAG_ENABLED, enabled);
	}

	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return getMana_(stack) * stack.getCount();
	}

	public static int getMana_(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
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

	@Override
	public int getMaxMana(ItemStack stack) {
		return MAX_MANA * stack.getCount();
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)) / stack.getCount());
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, BlockEntity pool) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return !otherStack.is(ModTags.Items.TERRA_PICK_BLACKLIST);
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, BlockEntity pool) {
		return false;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return true;
	}

	/* todo 1.16-fabric
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack before, @Nonnull ItemStack after, boolean slotChanged) {
		return after.getItem() != this || isEnabled(before) != isEnabled(after);
	}
	*/

	@Nonnull
	@Override
	public Rarity getRarity(@Nonnull ItemStack stack) {
		int level = getLevel(stack);
		if (stack.isEnchanted()) {
			level++;
		}
		if (level >= 5) { // SS rank/enchanted S rank
			return Rarity.EPIC;
		}
		if (level >= 3) { // A rank/enchanted B rank
			return Rarity.RARE;
		}
		return Rarity.UNCOMMON;
	}
}
