/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.VecHelper;

import javax.annotation.Nonnull;

public class ItemSkyDirtRod extends ItemDirtRod {

	public ItemSkyDirtRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST * 2, false)) {
			Vec3 playerVec = VecHelper.fromEntityCenter(player);
			Vec3 lookVec = player.getLookAngle().scale(3);
			Vec3 placeVec = playerVec.add(lookVec);

			int x = Mth.floor(placeVec.x);
			int y = Mth.floor(placeVec.y) + 1;
			int z = Mth.floor(placeVec.z);

			int entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(x, y, z, x + 1, y + 1, z + 1)).size();

			if (entities == 0) {
				BlockHitResult hit = new BlockHitResult(Vec3.ZERO, Direction.DOWN, new BlockPos(x, y, z), false);
				InteractionResult result = PlayerHelper.substituteUse(new UseOnContext(player, hand, hit), new ItemStack(Blocks.DIRT));

				if (result.consumesAction()) {
					ManaItemHandler.instance().requestManaExactForTool(stack, player, COST * 2, true);
					SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.35F, 0.2F, 0.05F, 5);
					for (int i = 0; i < 6; i++) {
						world.addParticle(data, x + Math.random(), y + Math.random(), z + Math.random(), 0, 0, 0);
					}
				}
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
	}

}
