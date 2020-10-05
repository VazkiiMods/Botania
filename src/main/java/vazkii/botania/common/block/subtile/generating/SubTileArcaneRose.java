/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
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
				float pitch = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.35F + 0.9F;
				world.playSound(null, getEffectivePos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.07F, pitch);
				sync();
				return;
			}
		}

		List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, effectBounds, e -> e.isAlive() && !e.getItem().isEmpty());
		for (ItemEntity entity : items) {
			ItemStack stack = entity.getItem();
			if (stack.getItem() == Items.ENCHANTED_BOOK || stack.isEnchanted()) {
				int xp = getEnchantmentXpValue(stack);
				if (xp > 0) {
					ItemStack newStack = removeNonCurses(stack);
					newStack.setCount(1);
					stack.shrink(1);

					ItemEntity newEntity = new ItemEntity(world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), newStack);
					newEntity.setMotion(entity.getMotion());
					world.addEntity(newEntity);

					world.playSound(null, getEffectivePos(), ModSounds.arcaneRoseDisenchant, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F);
					while (xp > 0) {
						int i = ExperienceOrbEntity.getXPSplit(xp);
						xp -= i;
						world.addEntity(new ExperienceOrbEntity(world, getEffectivePos().getX() + 0.5D, getEffectivePos().getY() + 0.5D, getEffectivePos().getZ() + 0.5D, i));
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
				ret += enchantment.getMinEnchantability(integer);
			}
		}

		return ret;
	}

	// [VanillaCopy] GrindstoneContainer, no damage and count setting
	private static ItemStack removeNonCurses(ItemStack stack) {
		ItemStack itemstack = stack.copy();
		itemstack.removeChildTag("Enchantments");
		itemstack.removeChildTag("StoredEnchantments");

		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((p_217012_0_) -> {
			return p_217012_0_.getKey().isCurse();
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		EnchantmentHelper.setEnchantments(map, itemstack);
		itemstack.setRepairCost(0);
		if (itemstack.getItem() == Items.ENCHANTED_BOOK && map.size() == 0) {
			itemstack = new ItemStack(Items.BOOK);
			if (stack.hasDisplayName()) {
				itemstack.setDisplayName(stack.getDisplayName());
			}
		}

		for (int i = 0; i < map.size(); ++i) {
			itemstack.setRepairCost(RepairContainer.getNewRepairCost(itemstack.getRepairCost()));
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
