/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		final int range = 3;
		if (!world.isRemote) {
			List<BlockPos> validCoords = new ArrayList<>();

			for (int i = -range - 1; i < range; i++) {
				for (int j = -range - 1; j < range; j++) {
					for (int k = 2; k >= -2; k--) {
						BlockPos pos_ = pos.add(i + 1, k + 1, j + 1);
						if (world.isAirBlock(pos_) && (!world.func_230315_m_().func_236040_e_() || pos_.getY() < 255)
								&& ModBlocks.whiteFlower.getDefaultState().isValidPosition(world, pos_)) {
							validCoords.add(pos_);
						}
					}
				}
			}

			int flowerCount = Math.min(validCoords.size(), world.rand.nextBoolean() ? 3 : 4);
			for (int i = 0; i < flowerCount; i++) {
				BlockPos coords = validCoords.get(world.rand.nextInt(validCoords.size()));
				validCoords.remove(coords);
				world.setBlockState(coords, ModBlocks.getFlower(DyeColor.byId(world.rand.nextInt(16))).getDefaultState());
			}
			ctx.getItem().shrink(1);
		} else {
			for (int i = 0; i < 15; i++) {
				double x = pos.getX() - range + world.rand.nextInt(range * 2 + 1) + Math.random();
				double y = pos.getY() + 1;
				double z = pos.getZ() - range + world.rand.nextInt(range * 2 + 1) + Math.random();
				float red = (float) Math.random();
				float green = (float) Math.random();
				float blue = (float) Math.random();
				WispParticleData data = WispParticleData.wisp(0.15F + (float) Math.random() * 0.25F, red, green, blue, 1);
				world.addParticle(data, x, y, z, 0, (float) Math.random() * 0.1F - 0.05F, 0);
			}
		}

		return ActionResultType.SUCCESS;
	}
}
