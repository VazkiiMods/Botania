/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 3:28:21 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.Item16Colors;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemPetal extends Item16Colors implements IFlowerComponent {

	public ItemPetal() {
		super(LibItemNames.PETAL);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// Copy of ItemBlock.onItemUse
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (!block.isReplaceable(world, pos))
		{
			pos = pos.offset(facing);
		}

		ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty() && player.canPlayerEdit(pos, facing, stack) && world.mayPlace(ModBlocks.buriedPetals, pos, false, facing, null))
		{
			int i = this.getMetadata(stack.getMetadata());
			IBlockState iblockstate1 = ModBlocks.buriedPetals.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, i, player);

			if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, iblockstate1))
			{
				SoundType soundtype = ModBlocks.buriedPetals.getSoundType();
				world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				stack.shrink(1);
			}

			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}

	// Direct copy of ItemBlock.placeBlockAt
	private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!world.setBlockState(pos, newState, 3)) return false;

		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == ModBlocks.buriedPetals)
		{
			// setTileEntityNBT(world, player, pos, stack);
			ModBlocks.buriedPetals.onBlockPlacedBy(world, pos, state, player, stack);
		}

		return true;
	}

	@Override
	public int getMetadata(int stackMeta) {
		return MathHelper.clamp(stackMeta, 0, 15);
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return EnumDyeColor.byMetadata(stack.getItemDamage()).colorValue;
	}
}
