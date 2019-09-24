/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2015, 12:27:31 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nonnull;

public class ItemWorldSeed extends ItemMod {

	public ItemWorldSeed(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		BlockPos coords = world.getSpawnPoint();

		if(MathHelper.pointDistanceSpace(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5, player.posX, player.posY, player.posZ) > 24) {
			player.rotationPitch = 0F;
			player.rotationYaw = 0F;
			player.setPositionAndUpdate(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5);

			while(!world.isCollisionBoxesEmpty(player, player.getBoundingBox()))
				player.setPositionAndUpdate(player.posX, player.posY + 1, player.posZ);

			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
			SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.25F, 1F, 0.25F, 10);
			for(int i = 0; i < 50; i++) {
                world.addParticle(data, player.posX + Math.random() * player.getWidth(), player.posY - 1.6 + Math.random() * player.getHeight(), player.posZ + Math.random() * player.getWidth(), 0, 0, 0);
            }

			stack.shrink(1);
			return ActionResult.newResult(ActionResultType.SUCCESS, stack);
		}

		return ActionResult.newResult(ActionResultType.PASS, stack);
	}

}
