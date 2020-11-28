/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

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

	public SubTileArcaneRose() {
		super(ModSubtiles.ROSA_ARCANA);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (world.isClient || getMana() >= getMaxMana()) {
			return;
		}

		Box effectBounds = new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1));

		List<PlayerEntity> players = getWorld().getNonSpectatingEntities(PlayerEntity.class, effectBounds);
		for (PlayerEntity player : players) {
			if (ExperienceHelper.getPlayerXP(player) >= 1 && player.isOnGround()) {
				ExperienceHelper.drainPlayerXP(player, 1);
				addMana(MANA_PER_XP);
				sync();
				return;
			}
		}

		List<ExperienceOrbEntity> orbs = getWorld().getNonSpectatingEntities(ExperienceOrbEntity.class, effectBounds);
		for (ExperienceOrbEntity orb : orbs) {
			if (orb.isAlive()) {
				addMana(orb.getExperienceAmount() * 35);
				orb.remove();
				float pitch = (world.random.nextFloat() - world.random.nextFloat()) * 0.35F + 0.9F;
				world.playSound(null, getEffectivePos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.07F, pitch);
				sync();
				return;
			}
		}

		List<ItemEntity> items = getWorld().getEntitiesByClass(ItemEntity.class, effectBounds, e -> e.isAlive() && !e.getStack().isEmpty());
		for (ItemEntity entity : items) {
			ItemStack stack = entity.getStack();
			if (stack.getItem() == Items.ENCHANTED_BOOK || stack.hasEnchantments()) {
				int xp = getEnchantmentXpValue(stack);
				if (xp > 0) {
					ItemStack newStack = removeNonCurses(stack);
					newStack.setCount(1);
					stack.decrement(1);

					ItemEntity newEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), newStack);
					newEntity.setVelocity(entity.getVelocity());
					world.spawnEntity(newEntity);

					world.playSound(null, getEffectivePos(), ModSounds.arcaneRoseDisenchant, SoundCategory.BLOCKS, 0.3F, this.world.random.nextFloat() * 0.1F + 0.9F);
					while (xp > 0) {
						int i = ExperienceOrbEntity.roundToOrbSize(xp);
						xp -= i;
						world.spawnEntity(new ExperienceOrbEntity(world, getEffectivePos().getX() + 0.5D, getEffectivePos().getY() + 0.5D, getEffectivePos().getZ() + 0.5D, i));
					}
					return;
				}
			}
		}
	}

	// [VanillaCopy] GrindstoneContainer
	private static int getEnchantmentXpValue(ItemStack stack) {
		int ret = 0;
		Map<Enchantment, Integer> map = EnchantmentHelper.get(stack);

		for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
			Enchantment enchantment = entry.getKey();
			Integer integer = entry.getValue();
			if (!enchantment.isCursed()) {
				ret += enchantment.getMinPower(integer);
			}
		}

		return ret;
	}

	// [VanillaCopy] GrindstoneContainer, no damage and count setting
	private static ItemStack removeNonCurses(ItemStack stack) {
		ItemStack itemstack = stack.copy();
		itemstack.removeSubTag("Enchantments");
		itemstack.removeSubTag("StoredEnchantments");

		Map<Enchantment, Integer> map = EnchantmentHelper.get(stack).entrySet().stream().filter((p_217012_0_) -> {
			return p_217012_0_.getKey().isCursed();
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		EnchantmentHelper.set(map, itemstack);
		itemstack.setRepairCost(0);
		if (itemstack.getItem() == Items.ENCHANTED_BOOK && map.size() == 0) {
			itemstack = new ItemStack(Items.BOOK);
			if (stack.hasCustomName()) {
				itemstack.setCustomName(stack.getName());
			}
		}

		for (int i = 0; i < map.size(); ++i) {
			itemstack.setRepairCost(AnvilScreenHandler.getNextCost(itemstack.getRepairCost()));
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
