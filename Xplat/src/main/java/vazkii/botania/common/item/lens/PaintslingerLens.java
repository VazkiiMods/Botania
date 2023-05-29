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
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.item.SparkEntity;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.function.Function;

public class PaintslingerLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		int storedColor = LensItem.getStoredColor(stack);
		if (!entity.getLevel().isClientSide && !burst.isFake() && storedColor > -1 && storedColor < 17) {
			if (pos.getType() == HitResult.Type.ENTITY) {
				Entity collidedWith = ((EntityHitResult) pos).getEntity();
				if (collidedWith instanceof Sheep sheep) {
					int r = 20;
					DyeColor sheepColor = sheep.getColor();
					List<Sheep> sheepList = entity.getLevel().getEntitiesOfClass(Sheep.class,
							new AABB(sheep.getX() - r, sheep.getY() - r, sheep.getZ() - r,
									sheep.getX() + r, sheep.getY() + r, sheep.getZ() + r));
					for (Sheep other : sheepList) {
						if (other.getColor() == sheepColor) {
							other.setColor(DyeColor.byId(storedColor == 16 ? other.getLevel().random.nextInt(16) : storedColor));
						}
					}
					shouldKill = true;
				} else if (collidedWith instanceof SparkEntity spark) {
					spark.setNetwork(DyeColor.byId(storedColor == 16 ? collidedWith.getLevel().random.nextInt(16) : storedColor));
				}
			} else if (pos.getType() == HitResult.Type.BLOCK) {
				BlockPos hitPos = ((BlockHitResult) pos).getBlockPos();
				Block hitBlock = entity.getLevel().getBlockState(hitPos).getBlock();
				ResourceLocation blockId = Registry.BLOCK.getKey(hitBlock);

				if (BotaniaAPI.instance().getPaintableBlocks().containsKey(blockId)) {
					List<BlockPos> coordsToPaint = new ArrayList<>();
					Queue<BlockPos> coordsToCheck = new ArrayDeque<>();
					Set<BlockPos> checkedCoords = new HashSet<>();
					coordsToCheck.add(hitPos);
					checkedCoords.add(hitPos);

					while (!coordsToCheck.isEmpty() && coordsToPaint.size() < 1000) {
						BlockPos coords = coordsToCheck.remove();
						coordsToPaint.add(coords);

						for (Direction dir : Direction.values()) {
							BlockPos candidatePos = coords.relative(dir);
							if (checkedCoords.add(candidatePos) && entity.getLevel().getBlockState(candidatePos).is(hitBlock)) {
								coordsToCheck.add(candidatePos);
							}
						}
					}
					for (BlockPos coords : coordsToPaint) {
						DyeColor placeColor = DyeColor.byId(storedColor == 16 ? entity.getLevel().random.nextInt(16) : storedColor);
						BlockState stateThere = entity.getLevel().getBlockState(coords);

						Function<DyeColor, Block> f = BotaniaAPI.instance().getPaintableBlocks().get(blockId);
						Block newBlock = f.apply(placeColor);
						if (newBlock != stateThere.getBlock()) {
							entity.getLevel().setBlockAndUpdate(coords, newBlock.withPropertiesOf(stateThere));
							XplatAbstractions.INSTANCE.sendToNear(entity.getLevel(), coords, new BotaniaEffectPacket(EffectType.PAINT_LENS, coords.getX(), coords.getY(), coords.getZ(), placeColor.getId()));
						}
					}
				}
			}
		}

		return shouldKill;
	}

}
