/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 12:12:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import javax.annotation.Nonnull;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.common.lib.LibItemNames;

public class ItemOpenBucket extends ItemMod {

	public ItemOpenBucket() {
		super(LibItemNames.OPEN_BUCKET);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		RayTraceResult RayTraceResult = rayTrace(world, player, true);

		if(RayTraceResult == null)
			return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
		else {
			if(RayTraceResult.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
				BlockPos pos = RayTraceResult.getBlockPos();

				if(!world.isBlockModifiable(player, pos))
					return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);

				if(!player.canPlayerEdit(pos, RayTraceResult.sideHit, par1ItemStack))
					return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);

				Material material = world.getBlockState(pos).getMaterial();
				int l = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)); // hack to get meta so we don't have to know the level prop

				if((material == Material.LAVA || material == Material.WATER) && l == 0) {
					world.setBlockToAir(pos);

					for(int x = 0; x < 5; x++)
						world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);

					return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
				}
			}

			return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
		}
	}

}
