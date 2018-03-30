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

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemOpenBucket extends ItemMod {

	public ItemOpenBucket() {
		super(LibItemNames.OPEN_BUCKET);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		RayTraceResult rtr = rayTrace(world, player, true);
		ItemStack stack = player.getHeldItem(hand);

		if(rtr == null)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		else {
			if(rtr.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
				BlockPos pos = rtr.getBlockPos();

				if(!world.isBlockModifiable(player, pos))
					return ActionResult.newResult(EnumActionResult.PASS, stack);

				if(!player.canPlayerEdit(pos, rtr.sideHit, stack))
					return ActionResult.newResult(EnumActionResult.PASS, stack);

				IBlockState state = world.getBlockState(pos);
				Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());
				boolean isFull = state.getBlock() instanceof BlockLiquid && state.getValue(BlockLiquid.LEVEL) == 0
						|| state.getBlock() instanceof IFluidBlock && Math.abs(((IFluidBlock) state.getBlock()).getFilledPercentage(world, pos)) >= 1;

				if(fluid != null && isFull) {
					player.playSound(fluid.getFillSound(world, pos), 1.0f, 1.0f);
					world.setBlockToAir(pos);

					for(int x = 0; x < 5; x++)
						world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);

					return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
				}
			}

			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
	}

}
