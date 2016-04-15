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

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand) {
		RayTraceResult RayTraceResult = getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

		if(RayTraceResult == null)
			return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
		else {
			if(RayTraceResult.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
				BlockPos pos = RayTraceResult.getBlockPos();

				if(!par2World.isBlockModifiable(par3EntityPlayer, pos))
					return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);

				if(!par3EntityPlayer.canPlayerEdit(pos, RayTraceResult.sideHit, par1ItemStack))
					return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);

				Material material = par2World.getBlockState(pos).getMaterial();
				int l = par2World.getBlockState(pos).getBlock().getMetaFromState(par2World.getBlockState(pos)); // hack to get meta so we don't have to know the level prop

				if((material == Material.lava || material == Material.water) && l == 0) {
					par2World.setBlockToAir(pos);
					
					for(int x = 0; x < 5; x++)
						par2World.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);

					return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
				}
			}

			return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
		}
	}

}
