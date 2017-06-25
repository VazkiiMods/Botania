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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemWorldSeed extends ItemMod {

	public ItemWorldSeed() {
		super(LibItemNames.WORLD_SEED);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		BlockPos coords = world.getSpawnPoint();

		if(MathHelper.pointDistanceSpace(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5, player.posX, player.posY, player.posZ) > 24) {
			player.rotationPitch = 0F;
			player.rotationYaw = 0F;
			player.setPositionAndUpdate(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5);

			while(!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty())
				player.setPositionAndUpdate(player.posX, player.posY + 1, player.posZ);

			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
			for(int i = 0; i < 50; i++)
				Botania.proxy.sparkleFX(player.posX + Math.random() * player.width, player.posY - 1.6 + Math.random() * player.height, player.posZ + Math.random() * player.width, 0.25F, 1F, 0.25F, 1F, 10);

			stack.shrink(1);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

}
