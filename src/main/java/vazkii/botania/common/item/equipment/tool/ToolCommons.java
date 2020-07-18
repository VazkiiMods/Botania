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
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

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

	public static final List<Material> materialsPick = Arrays.asList(Material.ROCK, Material.IRON, Material.ICE, Material.GLASS, Material.PISTON, Material.ANVIL, Material.SHULKER);
	public static final List<Material> materialsShovel = Arrays.asList(Material.ORGANIC, Material.EARTH, Material.SAND, Material.SNOW, Material.SNOW_BLOCK, Material.CLAY);
	public static final List<Material> materialsAxe = Arrays.asList(Material.CORAL, Material.LEAVES, Material.PLANTS, Material.WOOD, Material.GOURD);

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

	public static void damageItem(ItemStack stack, int dmg, LivingEntity entity, int manaPerDamage) {
		int manaToRequest = dmg * manaPerDamage;
		boolean manaRequested = entity instanceof PlayerEntity && ManaItemHandler.instance().requestManaExactForTool(stack, (PlayerEntity) entity, manaToRequest, true);

		if (!manaRequested) {
			stack.damageItem(dmg, entity, e -> {});
		}
	}

	public static void removeBlocksInIteration(PlayerEntity player, ItemStack stack, World world, BlockPos centerPos,
			Vector3i startDelta, Vector3i endDelta, Predicate<BlockState> filter,
			boolean dispose) {
		for (BlockPos iterPos : BlockPos.getAllInBoxMutable(centerPos.add(startDelta),
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
		if (!world.isBlockLoaded(pos)) {
			return;
		}

		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (!world.isRemote && filter.test(state)
				&& !block.isAir(state, world, pos) && state.getPlayerRelativeBlockHardness(player, world, pos) > 0
				&& state.canHarvestBlock(player.world, pos, player)) {
			int exp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, pos);
			if (exp == -1) {
				return;
			}

			if (!player.abilities.isCreativeMode) {
				TileEntity tile = world.getTileEntity(pos);

				if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
					block.onPlayerDestroy(world, pos, state);

					if (!dispose || !ItemElementiumPick.isDisposable(block)) {
						block.harvestBlock(world, player, pos, state, tile, stack);
						block.dropXpOnBlockBreak(world, pos, exp);
					}
				}

				damageItem(stack, 1, player, 80);
			} else {
				world.removeBlock(pos, false);
			}

			if (particles && ConfigHandler.COMMON.blockBreakParticles.get() && ConfigHandler.COMMON.blockBreakParticlesTool.get()) {
				world.playEvent(2001, pos, Block.getStateId(state));
			}
		}
	}

	public static int getToolPriority(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}

		Item item = stack.getItem();
		if (!(item instanceof ToolItem)) {
			return 0;
		}

		ToolItem tool = (ToolItem) item;
		IItemTier material = tool.getTier();
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

		int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		return materialLevel * 100 + modifier * 10 + efficiency;
	}

	public static BlockRayTraceResult raytraceFromEntity(Entity e, double distance, boolean fluids) {
		return (BlockRayTraceResult) e.pick(distance, 1, fluids);
	}
}
