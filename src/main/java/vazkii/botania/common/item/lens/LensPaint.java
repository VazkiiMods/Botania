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
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.item.ISparkEntity;
import vazkii.botania.common.network.PacketBotaniaEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LensPaint extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		int storedColor = ItemLens.getStoredColor(stack);
		if (!entity.level.isClientSide && !burst.isFake() && storedColor > -1 && storedColor < 17) {
			if (pos.getType() == HitResult.Type.ENTITY) {
				Entity collidedWith = ((EntityHitResult) pos).getEntity();
				if (collidedWith instanceof Sheep) {
					int r = 20;
					Sheep sheep = (Sheep) collidedWith;
					DyeColor sheepColor = sheep.getColor();
					List<Sheep> sheepList = entity.level.getEntitiesOfClass(Sheep.class,
							new AABB(sheep.getX() - r, sheep.getY() - r, sheep.getZ() - r,
									sheep.getX() + r, sheep.getY() + r, sheep.getZ() + r));
					for (Sheep other : sheepList) {
						if (other.getColor() == sheepColor) {
							other.setColor(DyeColor.byId(storedColor == 16 ? other.level.random.nextInt(16) : storedColor));
						}
					}
					dead = true;
				} else if (collidedWith instanceof ISparkEntity) {
					((ISparkEntity) collidedWith).setNetwork(DyeColor.byId(storedColor == 16 ? collidedWith.level.random.nextInt(16) : storedColor));
				}
			} else if (pos.getType() == HitResult.Type.BLOCK) {
				BlockPos hit = ((BlockHitResult) pos).getBlockPos();
				BlockState state = entity.level.getBlockState(hit);
				ResourceLocation blockId = Registry.BLOCK.getKey(state.getBlock());

				if (BotaniaAPI.instance().getPaintableBlocks().containsKey(blockId)) {
					List<BlockPos> coordsToPaint = new ArrayList<>();
					List<BlockPos> coordsFound = new ArrayList<>();
					coordsFound.add(hit);

					do {
						List<BlockPos> iterCoords = new ArrayList<>(coordsFound);
						for (BlockPos coords : iterCoords) {
							coordsFound.remove(coords);
							coordsToPaint.add(coords);

							for (Direction dir : Direction.values()) {
								BlockPos coords_ = coords.relative(dir);
								BlockState state_ = entity.level.getBlockState(coords_);
								if (state_ == state && !coordsFound.contains(coords_) && !coordsToPaint.contains(coords_)) {
									coordsFound.add(coords_);
								}
							}
						}
					} while (!coordsFound.isEmpty() && coordsToPaint.size() < 1000);

					for (BlockPos coords : coordsToPaint) {
						DyeColor placeColor = DyeColor.byId(storedColor == 16 ? entity.level.random.nextInt(16) : storedColor);
						BlockState stateThere = entity.level.getBlockState(coords);

						Function<DyeColor, Block> f = BotaniaAPI.instance().getPaintableBlocks().get(blockId);
						Block newBlock = f.apply(placeColor);
						if (newBlock != stateThere.getBlock()) {
							entity.level.setBlockAndUpdate(coords, newBlock.defaultBlockState());
							PacketBotaniaEffect.sendNearby(entity.level, coords, PacketBotaniaEffect.EffectType.PAINT_LENS, coords.getX(), coords.getY(), coords.getZ(), placeColor.getId());
						}
					}
				}
			}
		}

		return dead;
	}

}
