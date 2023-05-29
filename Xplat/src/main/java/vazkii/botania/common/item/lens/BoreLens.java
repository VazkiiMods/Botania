/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.List;
import java.util.stream.Stream;

public class BoreLens extends Lens {
	@Override
	public boolean collideBurst(ManaBurst burst, HitResult rtr, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		Level world = entity.getLevel();

		if (world.isClientSide || rtr.getType() != HitResult.Type.BLOCK) {
			return false;
		}

		BlockPos collidePos = ((BlockHitResult) rtr).getBlockPos();
		BlockState state = world.getBlockState(collidePos);

		ItemStack composite = ((LensItem) stack.getItem()).getCompositeLens(stack);
		boolean warp = !composite.isEmpty() && composite.is(BotaniaItems.lensWarp);

		if (warp && (state.is(BotaniaBlocks.pistonRelay) || state.is(Blocks.PISTON) || state.is(Blocks.MOVING_PISTON) || state.is(Blocks.PISTON_HEAD))) {
			return false;
		}

		int harvestLevel = BotaniaConfig.common().harvestLevelBore();

		BlockEntity tile = world.getBlockEntity(collidePos);

		float hardness = state.getDestroySpeed(world, collidePos);
		int mana = burst.getMana();

		BlockPos source = burst.getBurstSourceBlockPos();
		if (!isManaBlock
				&& canHarvest(harvestLevel, state)
				&& hardness != -1
				&& (burst.isFake() || mana >= 24)) {
			if (!burst.hasAlreadyCollidedAt(collidePos)) {
				if (!burst.isFake()) {
					List<ItemStack> items = Block.getDrops(state, (ServerLevel) world, collidePos, tile);

					world.removeBlock(collidePos, false);
					if (BotaniaConfig.common().blockBreakParticles()) {
						world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, collidePos, Block.getId(state));
					}

					boolean sourceless = source.equals(ManaBurst.NO_SOURCE);
					boolean doWarp = warp && !sourceless;
					Vec3 dropPosition;
					if (doWarp && world.getBlockEntity(source) instanceof ManaSpreaderBlockEntity spreader) {
						Vec3 sourceVec = Vec3.atCenterOf(source);
						/* NB: this looks backwards but it's right. spreaders take rotX/rotY to respectively mean
						* "rotation *parallel* to the X and Y axes", while vanilla's methods take XRot/YRot
						* to respectively mean "rotation *around* the X and Y axes".
						* See also the ManaBurstEntity constructor.
						* TODO consider renaming our versions to match vanilla
						*/
						float xRot = spreader.getRotationY();
						float yRot = -(spreader.getRotationX() + 90F);
						Vec3 inverseSpreaderDirection = ManaBurstEntity.calculateBurstVelocity(xRot, yRot).normalize().reverse();
						dropPosition = sourceVec.add(inverseSpreaderDirection);
					} else {
						dropPosition = Vec3.atCenterOf(collidePos);
					}

					if (world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
						for (ItemStack stack_ : items) {
							ItemEntity itemEntity = new ItemEntity(world, dropPosition.x, dropPosition.y, dropPosition.z, stack_);
							itemEntity.setDefaultPickUpDelay();
							world.addFreshEntity(itemEntity);
						}
					}

					burst.setMana(mana - 24);
				}
			}

			shouldKill = false;
		}

		return shouldKill;
	}

	private static List<ItemStack> stacks(Item... items) {
		return Stream.of(items).map(ItemStack::new).toList();
	}

	private static final List<List<ItemStack>> HARVEST_TOOLS_BY_LEVEL = List.of(
			stacks(Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SHOVEL),
			stacks(Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SHOVEL),
			stacks(Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SHOVEL),
			stacks(Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SHOVEL),
			stacks(Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SHOVEL)
	);

	public static boolean canHarvest(int harvestLevel, BlockState state) {
		return !getTool(harvestLevel, state).isEmpty();
	}

	public static ItemStack getHarvestToolStack(int harvestLevel, BlockState state) {
		return getTool(harvestLevel, state).copy();
	}

	private static ItemStack getTool(int harvestLevel, BlockState state) {
		if (!state.requiresCorrectToolForDrops()) {
			return HARVEST_TOOLS_BY_LEVEL.get(0).get(0);
		}

		int idx = Math.min(harvestLevel, HARVEST_TOOLS_BY_LEVEL.size() - 1);
		for (var tool : HARVEST_TOOLS_BY_LEVEL.get(idx)) {
			if (tool.isCorrectToolForDrops(state)) {
				return tool;
			}
		}

		return ItemStack.EMPTY;
	}
}
