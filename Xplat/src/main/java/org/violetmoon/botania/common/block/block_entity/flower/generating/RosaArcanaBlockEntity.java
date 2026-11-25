/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block.block_entity.flower.generating;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.block_entity.GeneratingFlowerBlockEntity;
import org.violetmoon.botania.api.block_entity.RadiusDescriptor;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;
import org.violetmoon.botania.common.handler.BotaniaSounds;
import org.violetmoon.botania.common.helper.EntityHelper;
import org.violetmoon.botania.common.helper.MathHelper;
import org.violetmoon.botania.mixin.ExperienceOrbAccessor;
import org.violetmoon.botania.network.clientbound.FlowerTakeItemEffectPacket;
import org.violetmoon.botania.xplat.BotaniaConfig;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RosaArcanaBlockEntity extends GeneratingFlowerBlockEntity {
	private static final int MANA_PER_XP = 50;
	private static final int RANGE = 1;
	public static final int PLAYER_TAKE_XP_DELAY = 10;

	/**
	 * The Rosa Arcana block entities currently in a loaded chunk, so experience orbs can look
	 * for one without going through every block entity of up to 4 chunks (each orb did that every 20 ticks while no
	 * flower was near). Separate sets per side, like Solegnolia, since an integrated server has two threads.
	 */
	private static final Set<RosaArcanaBlockEntity> clientFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Set<RosaArcanaBlockEntity> serverFlowers = Collections.newSetFromMap(new WeakHashMap<>());

	public RosaArcanaBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.ROSA_ARCANA, pos, state);
	}

	@Override
	public void clearRemoved() {
		super.clearRemoved();
		if (level != null) {
			(level.isClientSide() ? clientFlowers : serverFlowers).add(this);
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (level != null) {
			(level.isClientSide() ? clientFlowers : serverFlowers).remove(this);
		}
	}

	/**
	 * The position of the closest loaded Rosa Arcana within {@code rangeLimit} blocks of {@code center}, or null.
	 * Same result as
	 * {@code getClosestMatchingBlockEntity(level, center, rangeLimit, RosaArcanaBlockEntity.class::isInstance)},
	 * except which flower wins between two at exactly the same distance.
	 */
	@Nullable
	public static BlockPos getClosestLoadedFlower(Level level, BlockPos center, int rangeLimit) {
		long minDist = Long.MAX_VALUE;
		long limitSquared = (long) rangeLimit * rangeLimit;
		BlockPos closestPos = null;
		for (RosaArcanaBlockEntity flower : level.isClientSide() ? clientFlowers : serverFlowers) {
			if (flower.getLevel() != level || flower.isRemoved()) {
				continue;
			}
			BlockPos pos = flower.getBlockPos();
			long dist = MathHelper.distSqr(center, pos);
			if (dist <= limitSquared && dist <= minDist) {
				minDist = dist;
				closestPos = pos;
			}
		}
		return closestPos;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!(level instanceof ServerLevel serverLevel) || getMana() >= getMaxMana()) {
			return;
		}

		AABB effectBounds = MathHelper.inflateBoxAround(getEffectivePos(), RANGE);

		if (consumeXpOrb(serverLevel, effectBounds)) {
			return;
		}
		if (disenchantAnItem(serverLevel, effectBounds)) {
			return;
		}
		drainPlayerXp(serverLevel, effectBounds);
	}

	/**
	 * "Plucks" XP orbs from players to be consumed by the flower.
	 * If the player has more than one level, orb size is randomly selected between 1 and 3, otherwise if the player
	 * still has any XP left, orb size 1 is extracted. Since the flower only extracts player XP when it can't already
	 * consume an XP orb this tick, the average XP orb size of 2 means the flower extracts an average amount of 1 XP per
	 * tick from the player. (This is the same speed as the direct drain in earlier versions.)
	 * 
	 * @see org.violetmoon.botania.mixin.PlayerMixin#shouldPickupXp(Player, Entity)
	 */
	@SuppressWarnings("JavadocReference")
	private static void drainPlayerXp(ServerLevel serverLevel, AABB effectBounds) {
		List<Player> players = new ArrayList<>();
		// entity search selects subchunks by assuming all entities are ghast-sized, so this is likely faster:
		serverLevel.players().stream()
				.filter(player -> player.isAlive() && !player.isSpectator()
						&& player.getBoundingBox().intersects(effectBounds))
				.forEach(players::add);
		// prevent players from picking up XP orbs
		for (Player player : players) {
			player.takeXpDelay = Math.max(PLAYER_TAKE_XP_DELAY, player.takeXpDelay);
		}
		// You would think checking totalExperience is right, but it seems to be
		// possibly equal to zero even when the level is > 0.
		// Instead, check the level and intra-level progress separately.
		players.removeIf(player -> player.experienceProgress == 0 && player.experienceLevel == 0);
		if (players.isEmpty()) {
			return;
		}
		// pull XP from one random player
		Player player = players.get(serverLevel.random.nextInt(players.size()));
		// this effectively only happens every other tick, so average out the XP drain to 1 per tick
		int drainAmount = player.experienceLevel > 0 && serverLevel.random.nextBoolean() ? 3 : 1;
		player.giveExperiencePoints(-drainAmount);
		ExperienceOrb.award(serverLevel, player.position().add(0, 0.5 * player.getEyeHeight(), 0), drainAmount);
	}

	private boolean consumeXpOrb(ServerLevel serverLevel, AABB effectBounds) {
		List<ExperienceOrb> orbs = serverLevel.getEntitiesOfClass(ExperienceOrb.class, effectBounds, ExperienceOrb::isAlive);
		for (ExperienceOrb orb : orbs) {
			int count = ((ExperienceOrbAccessor) orb).botania_getCount();
			if (count > 0) {
				addMana(orb.getValue() * MANA_PER_XP);
				((ExperienceOrbAccessor) orb).botania_setCount(count - 1);
				if (BotaniaConfig.common().flowerItemPickupAnimations()) {
					XplatAbstractions.instance().sendToTracking(orb,
							FlowerTakeItemEffectPacket.create(orb.getId(), getEffectivePos(), 1));
				}
				if (count == 1) {
					orb.discard();
				}
				return true;
			}
		}
		return false;
	}

	private boolean disenchantAnItem(ServerLevel serverLevel, AABB effectBounds) {
		List<ItemEntity> items = serverLevel.getEntitiesOfClass(ItemEntity.class, effectBounds,
				e -> e.isAlive() && !e.getItem().isEmpty() && EnchantmentHelper.hasAnyEnchantments(e.getItem())
		);
		for (ItemEntity entity : items) {
			ItemStack stack = entity.getItem();
			int xp = getEnchantmentXpValue(stack);
			if (xp <= 0) {
				continue;
			}
			ItemStack newStack = removeNonCurses(stack.copyWithCount(1));
			EntityHelper.shrinkItem(entity);

			ItemEntity newEntity = new ItemEntity(serverLevel, entity.getX(), entity.getY(), entity.getZ(), newStack);
			newEntity.setDeltaMovement(entity.getDeltaMovement());
			serverLevel.addFreshEntity(newEntity);

			serverLevel.playSound(null, getEffectivePos(), BotaniaSounds.ARCANE_ROSE_DISENCHANT,
					SoundSource.BLOCKS, 1F, serverLevel.random.nextFloat() * 0.1F + 0.9F);
			ExperienceOrb.award(serverLevel, entity.getEyePosition(), xp);
			return true;
		}
		return false;
	}

	// [VanillaCopy] GrindstoneMenu::<init> -> [anonymous class for resultSlot]::getExperienceFromItem
	private static int getEnchantmentXpValue(ItemStack item) {
		int amount = 0;
		ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(item);

		for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
			Holder<Enchantment> enchant = entry.getKey();
			int lvl = entry.getIntValue();
			if (!enchant.is(EnchantmentTags.CURSE)) {
				amount += enchant.value().getMinCost(lvl);
			}
		}

		return amount;
	}

	// [VanillaCopy] GrindstoneMenu::removeNonCursesFrom
	private static ItemStack removeNonCurses(ItemStack item) {
		ItemEnchantments newEnchantments = EnchantmentHelper.updateEnchantments(
				item, enchantments -> enchantments.removeIf(enchantment -> !enchantment.is(EnchantmentTags.CURSE))
		);
		if (item.is(Items.ENCHANTED_BOOK) && newEnchantments.isEmpty()) {
			item = item.transmuteCopy(Items.BOOK);
		}

		int repairCost = 0;

		for (int i = 0; i < newEnchantments.size(); i++) {
			repairCost = AnvilMenu.calculateIncreasedRepairCost(repairCost);
		}

		item.set(DataComponents.REPAIR_COST, repairCost);
		return item;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFF8EF8;
	}

	@Override
	public int getMaxMana() {
		return 6000;
	}

}
