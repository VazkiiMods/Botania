/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ExperienceHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubTileArcaneRose extends TileEntityGeneratingFlower {
	private static final int MANA_PER_XP = 50;
	private static final int MANA_PER_XP_ORB = 35;
	private static final int MANA_PER_XP_DISENCHANT = 40;
	private static final int RANGE = 1;

	public SubTileArcaneRose(BlockPos pos, BlockState state) {
		super(ModSubtiles.ROSA_ARCANA, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (level.isClientSide || getMana() >= getMaxMana()) {
			return;
		}

		AABB effectBounds = new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1));

		List<Player> players = getLevel().getEntitiesOfClass(Player.class, effectBounds);
		for (Player player : players) {
			if (ExperienceHelper.getPlayerXP(player) >= 1 && player.isOnGround()) {
				ExperienceHelper.drainPlayerXP(player, 1);
				addMana(MANA_PER_XP);
				sync();
				return;
			}
		}

		List<ExperienceOrb> orbs = getLevel().getEntitiesOfClass(ExperienceOrb.class, effectBounds);
		for (ExperienceOrb orb : orbs) {
			if (orb.isAlive()) {
				addMana(orb.getValue() * 35);
				orb.discard();
				float pitch = (level.random.nextFloat() - level.random.nextFloat()) * 0.35F + 0.9F;
				level.playSound(null, getEffectivePos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.07F, pitch);
				sync();
				return;
			}
		}

		List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, effectBounds, e -> e.isAlive() && !e.getItem().isEmpty());
		for (ItemEntity entity : items) {
			ItemStack stack = entity.getItem();
			if (stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted()) {
				int xp = getEnchantmentXpValue(stack);
				if (xp > 0) {
					ItemStack newStack = removeNonCurses(stack);
					newStack.setCount(1);
					stack.shrink(1);

					ItemEntity newEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), newStack);
					newEntity.setDeltaMovement(entity.getDeltaMovement());
					level.addFreshEntity(newEntity);

					level.playSound(null, getEffectivePos(), ModSounds.arcaneRoseDisenchant, SoundSource.BLOCKS, 0.3F, this.level.random.nextFloat() * 0.1F + 0.9F);
					while (xp > 0) {
						int i = ExperienceOrb.getExperienceValue(xp);
						xp -= i;
						level.addFreshEntity(new ExperienceOrb(level, getEffectivePos().getX() + 0.5D, getEffectivePos().getY() + 0.5D, getEffectivePos().getZ() + 0.5D, i));
					}
					return;
				}
			}
		}
	}

	// [VanillaCopy] GrindstoneContainer
	private static int getEnchantmentXpValue(ItemStack stack) {
		int ret = 0;
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

		for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
			Enchantment enchantment = entry.getKey();
			Integer integer = entry.getValue();
			if (!enchantment.isCurse()) {
				ret += enchantment.getMinCost(integer);
			}
		}

		return ret;
	}

	// [VanillaCopy] GrindstoneContainer, no damage and count setting
	private static ItemStack removeNonCurses(ItemStack stack) {
		ItemStack itemstack = stack.copy();
		itemstack.removeTagKey("Enchantments");
		itemstack.removeTagKey("StoredEnchantments");

		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((p_217012_0_) -> {
			return p_217012_0_.getKey().isCurse();
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		EnchantmentHelper.setEnchantments(map, itemstack);
		itemstack.setRepairCost(0);
		if (itemstack.is(Items.ENCHANTED_BOOK) && map.size() == 0) {
			itemstack = new ItemStack(Items.BOOK);
			if (stack.hasCustomHoverName()) {
				itemstack.setHoverName(stack.getHoverName());
			}
		}

		for (int i = 0; i < map.size(); ++i) {
			itemstack.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(itemstack.getBaseRepairCost()));
		}

		return itemstack;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
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
