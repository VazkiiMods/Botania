/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public final class ToolCommons {

	public static final List<Material> materialsPick = Arrays.asList(Material.STONE, Material.METAL, Material.ICE, Material.GLASS, Material.PISTON, Material.REPAIR_STATION, Material.SHULKER_BOX);
	public static final List<Material> materialsShovel = Arrays.asList(Material.SOLID_ORGANIC, Material.SOIL, Material.AGGREGATE, Material.SNOW_LAYER, Material.SNOW_BLOCK, Material.ORGANIC_PRODUCT);
	public static final List<Material> materialsAxe = Arrays.asList(Material.UNUSED_PLANT, Material.LEAVES, Material.PLANT, Material.WOOD, Material.GOURD);
	private static boolean recCall = false;

	/**
	 * Consumes as much mana as possible, returning the amount of damage that couldn't be paid with mana
	 */
	public static int damageItemIfPossible(ItemStack stack, int amount, LivingEntity entity, int manaPerDamage) {
		if (!(entity instanceof PlayerEntity)) {
			return amount;
		}

		PlayerEntity player = (PlayerEntity) entity;
		while (amount > 0) {
			if (ManaItemHandler.instance().requestManaExactForTool(stack, player, manaPerDamage, true)) {
				amount--;
			} else {
				break;
			}
		}

		return amount;
	}

	public static void removeBlocksInIteration(PlayerEntity player, ItemStack stack, World world, BlockPos centerPos,
			Vec3i startDelta, Vec3i endDelta, Predicate<BlockState> filter) {
		if (recCall) {
			return;
		}

		recCall = true;
		try {
			for (BlockPos iterPos : BlockPos.iterate(centerPos.add(startDelta),
					centerPos.add(endDelta))) {
				// skip original block space, vanilla code will handle it
				if (iterPos.equals(centerPos)) {
					continue;
				}
				removeBlockWithDrops(player, stack, world, iterPos, filter);
			}
		} finally {
			recCall = false;
		}
	}

	/**
	 * NB: Cannot be called in a call chain leading from PlayerInteractionManager.tryHarvestBlock
	 * without additional protection like {@link #recCall} in {@link #removeBlocksInIteration}
	 * or {@link vazkii.botania.common.item.relic.ItemLokiRing#breakOnAllCursors},
	 * since this method calls that method also and would lead to an infinite loop.
	 */
	public static void removeBlockWithDrops(PlayerEntity player, ItemStack stack, World world, BlockPos pos,
			Predicate<BlockState> filter) {
		if (!world.isChunkLoaded(pos)) {
			return;
		}

		BlockState blockstate = world.getBlockState(pos);
		boolean unminable = blockstate.calcBlockBreakingDelta(player, world, pos) == 0;

		if (!world.isClient && !unminable && filter.test(blockstate) && !blockstate.isAir()) {
			ItemStack save = player.getMainHandStack();
			player.setStackInHand(Hand.MAIN_HAND, stack);
			((ServerPlayerEntity) player).interactionManager.tryBreakBlock(pos);
			player.setStackInHand(Hand.MAIN_HAND, save);
		}
	}

	public static int getToolPriority(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}

		Item item = stack.getItem();
		if (!(item instanceof MiningToolItem)) {
			return 0;
		}

		MiningToolItem tool = (MiningToolItem) item;
		ToolMaterial material = tool.getMaterial();
		int materialLevel = 0;
		if (material == BotaniaAPI.instance().getManasteelItemTier()) {
			materialLevel = 10;
		}
		if (material == BotaniaAPI.instance().getElementiumItemTier()) {
			materialLevel = 11;
		}
		if (material == BotaniaAPI.instance().getTerrasteelItemTier()) {
			materialLevel = 20;
		}

		int modifier = 0;
		if (item == ModItems.terraPick) {
			modifier = ItemTerraPick.getLevel(stack);
		}

		int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);
		return materialLevel * 100 + modifier * 10 + efficiency;
	}

	public static BlockHitResult raytraceFromEntity(Entity e, double distance, boolean fluids) {
		return (BlockHitResult) e.raycast(distance, 1, fluids);
	}
}
