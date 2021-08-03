/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class ItemFertilizer extends Item {

	public ItemFertilizer(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		final int range = 3;
		if (!world.isClientSide) {
			List<BlockPos> validCoords = new ArrayList<>();

			for (int i = -range - 1; i < range; i++) {
				for (int j = -range - 1; j < range; j++) {
					for (int k = 2; k >= -2; k--) {
						BlockPos pos_ = pos.offset(i + 1, k + 1, j + 1);
						if (world.isEmptyBlock(pos_) && (!world.dimensionType().ultraWarm() || pos_.getY() < 255)
								&& ModBlocks.whiteFlower.defaultBlockState().canSurvive(world, pos_)) {
							validCoords.add(pos_);
						}
					}
				}
			}

			int flowerCount = Math.min(validCoords.size(), world.random.nextBoolean() ? 3 : 4);
			for (int i = 0; i < flowerCount; i++) {
				BlockPos coords = validCoords.get(world.random.nextInt(validCoords.size()));
				validCoords.remove(coords);
				world.setBlockAndUpdate(coords, ModBlocks.getFlower(DyeColor.byId(world.random.nextInt(16))).defaultBlockState());
			}
			ctx.getItemInHand().shrink(1);
		} else {
			for (int i = 0; i < 15; i++) {
				double x = pos.getX() - range + world.random.nextInt(range * 2 + 1) + Math.random();
				double y = pos.getY() + 1;
				double z = pos.getZ() - range + world.random.nextInt(range * 2 + 1) + Math.random();
				float red = (float) Math.random();
				float green = (float) Math.random();
				float blue = (float) Math.random();
				WispParticleData data = WispParticleData.wisp(0.15F + (float) Math.random() * 0.25F, red, green, blue, 1);
				world.addParticle(data, x, y, z, 0, (float) Math.random() * 0.1F - 0.05F, 0);
			}
		}

		return InteractionResult.SUCCESS;
	}
}
