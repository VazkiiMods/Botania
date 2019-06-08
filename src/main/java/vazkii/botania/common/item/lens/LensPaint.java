/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:44:01 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LensPaint extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		int storedColor = ItemLens.getStoredColor(stack);
		if(!entity.world.isRemote && !burst.isFake() && storedColor > -1 && storedColor < 17) {
			if(pos.entity instanceof SheepEntity) {
				int r = 20;
				DyeColor sheepColor = ((SheepEntity) pos.entity).getFleeceColor();
				List<SheepEntity> sheepList = entity.world.getEntitiesWithinAABB(SheepEntity.class, new AxisAlignedBB(pos.entity.posX - r, pos.entity.posY - r, pos.entity.posZ - r, pos.entity.posX + r, pos.entity.posY + r, pos.entity.posZ + r));
				for(SheepEntity sheep : sheepList) {
					if(sheep.getFleeceColor() == sheepColor)
						sheep.setFleeceColor(DyeColor.byId(storedColor == 16 ? sheep.world.rand.nextInt(16) : storedColor));
				}
				dead = true;
			} else {
				BlockState state = entity.world.getBlockState(pos.getBlockPos());
				Block block = state.getBlock();
				if(BotaniaAPI.paintableBlocks.containsKey(block.delegate)) {
					List<BlockPos> coordsToPaint = new ArrayList<>();
					List<BlockPos> coordsFound = new ArrayList<>();
					coordsFound.add(pos.getBlockPos());

					do {
						List<BlockPos> iterCoords = new ArrayList<>(coordsFound);
						for(BlockPos coords : iterCoords) {
							coordsFound.remove(coords);
							coordsToPaint.add(coords);

							for(Direction dir : Direction.values()) {
								BlockState state_ = entity.world.getBlockState(coords.offset(dir));
								BlockPos coords_ = new BlockPos(coords.offset(dir));
								if(state_ == state && !coordsFound.contains(coords_) && !coordsToPaint.contains(coords_))
									coordsFound.add(coords_);
							}
						}
					} while(!coordsFound.isEmpty() && coordsToPaint.size() < 1000);

					for(BlockPos coords : coordsToPaint) {
						DyeColor placeColor = DyeColor.byId(storedColor == 16 ? entity.world.rand.nextInt(16) : storedColor);
						BlockState stateThere = entity.world.getBlockState(coords);

						Function<DyeColor, Block> f = BotaniaAPI.paintableBlocks.get(block.delegate);
						Block newBlock = f.apply(placeColor);
						if(newBlock != stateThere.getBlock()) {
							entity.world.setBlockState(coords, newBlock.getDefaultState());
							PacketHandler.sendToNearby(entity.world, coords,
									new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.PAINT_LENS, coords.getX(), coords.getY(), coords.getZ(), placeColor.getId()));
						}
					}
				}
			}
		}

		return dead;
	}

}
