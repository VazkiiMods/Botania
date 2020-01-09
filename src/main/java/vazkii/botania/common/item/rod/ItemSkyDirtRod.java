/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 23, 2014, 1:06:51 AM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;

public class ItemSkyDirtRod extends ItemDirtRod {

	public ItemSkyDirtRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, COST * 2, false)) {
			Vector3 playerVec = Vector3.fromEntityCenter(player);
			Vector3 lookVec = new Vector3(player.getLookVec()).multiply(3);
			Vector3 placeVec = playerVec.add(lookVec);

			int x = MathHelper.floor(placeVec.x);
			int y = MathHelper.floor(placeVec.y) + 1;
			int z = MathHelper.floor(placeVec.z);

			int entities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)).size();

			if(entities == 0) {
				ItemStack stackToPlace = new ItemStack(Blocks.DIRT);
				BlockRayTraceResult hit = new BlockRayTraceResult(Vec3d.ZERO, Direction.DOWN, new BlockPos(x, y, z), false);
				PlayerHelper.substituteUse(new ItemUseContext(player, hand, hit), stackToPlace);

				if(stackToPlace.isEmpty()) {
					ManaItemHandler.requestManaExactForTool(stack, player, COST * 2, true);
					SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.35F, 0.2F, 0.05F, 5);
					for(int i = 0; i < 6; i++) {
						world.addParticle(data, x + Math.random(), y + Math.random(), z + Math.random(), 0, 0, 0);
					}
				}
			}
		}
		if(world.isRemote)
			player.swingArm(hand);

		return ActionResult.success(stack);
	}

}
