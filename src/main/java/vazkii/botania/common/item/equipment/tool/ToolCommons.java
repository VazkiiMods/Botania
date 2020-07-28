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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick;
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
			Vec3i startDelta, Vec3i endDelta, Predicate<BlockState> filter,
			boolean dispose) {
		for (BlockPos iterPos : BlockPos.iterate(centerPos.add(startDelta),
				centerPos.add(endDelta))) {
			// skip original block space to avoid crash, vanilla code in the tool class will handle it
			if (iterPos.equals(centerPos)) {
				continue;
			}
			removeBlockWithDrops(player, stack, world, iterPos, filter, dispose);
		}
	}

	public static void removeBlockWithDrops(PlayerEntity player, ItemStack stack, World world, BlockPos pos,
			Predicate<BlockState> filter,
			boolean dispose) {
		removeBlockWithDrops(player, stack, world, pos, filter, dispose, true);
	}

	public static void removeBlockWithDrops(PlayerEntity player, ItemStack stack, World world, BlockPos pos,
			Predicate<BlockState> filter,
			boolean dispose, boolean particles) {
		if (!world.isChunkLoaded(pos)) {
			return;
		}

		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (!world.isClient && filter.test(state)
				&& !block.isAir(state, world, pos) && state.calcBlockBreakingDelta(player, world, pos) > 0
				&& state.canHarvestBlock(player.world, pos, player)) {
			int exp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameMode(), (ServerPlayerEntity) player, pos);
			if (exp == -1) {
				return;
			}

			if (!player.abilities.creativeMode) {
				BlockEntity tile = world.getBlockEntity(pos);

				if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
					block.onBroken(world, pos, state);

					if (!dispose || !ItemElementiumPick.isDisposable(block)) {
						block.afterBreak(world, player, pos, state, tile, stack);
						block.dropExperience(world, pos, exp);
					}
				}

				boolean paidWithMana = ManaItemHandler.instance().requestManaExactForTool(stack, player, 80, true);
				if (!paidWithMana) {
					stack.damage(1, player, e -> {});
				}
			} else {
				world.removeBlock(pos, false);
			}

			if (particles && ConfigHandler.COMMON.blockBreakParticles.getValue() && ConfigHandler.COMMON.blockBreakParticlesTool.getValue()) {
				world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
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
		return (BlockHitResult) e.rayTrace(distance, 1, fluids);
	}
}
