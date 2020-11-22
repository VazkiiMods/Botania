/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.Hand;
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
		for (BlockPos iterPos : BlockPos.iterate(centerPos.add(startDelta),
				centerPos.add(endDelta))) {
			// skip original block space to avoid crash, vanilla code in the tool class will handle it
			if (iterPos.equals(centerPos)) {
				continue;
			}
			removeBlockWithDrops(player, stack, world, iterPos, filter);
		}
	}

	public static void removeBlockWithDrops(PlayerEntity player, ItemStack stack, World world, BlockPos pos,
			Predicate<BlockState> filter) {
		if (!world.isChunkLoaded(pos)) {
			return;
		}

		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();

		if (!world.isClient && filter.test(blockState) && !blockState.isAir()) {
			// [VanillaCopy] ServerPlayerInteractionManager.tryBreakBlock, removeBlock inlined
			// we could technically just call that method directly, but run into complications with infinite recursion (since this method)
			// can be invoked from a call chain beginning at tryHarvestBlock as well. It's simpler to just copy.

			if (!player.getMainHandStack().getItem().canMine(blockState, world, pos, player)
				|| player.isBlockBreakingRestricted(world, pos, ((ServerPlayerEntity) player).interactionManager.getGameMode())) {
				return;
			}

			block.onBreak(world, pos, blockState, player);
			boolean bl = world.removeBlock(pos, false);
			if (bl) {
				block.onBroken(world, pos, blockState);
			}

			if (player.isCreative()) {
				return;
			} else {
				ItemStack itemStack = stack; // player.getMainHandStack();
				ItemStack itemStack2 = itemStack.copy();
				boolean bl2 = player.isUsingEffectiveTool(blockState);
				itemStack.postMine(world, blockState, pos, player);
				if (bl && bl2) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					block.afterBreak(world, player, pos, blockState, blockEntity, itemStack2);
				}
			}
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
