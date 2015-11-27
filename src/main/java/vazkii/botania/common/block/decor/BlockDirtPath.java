/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 18, 2015, 1:47:03 AM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockDirtPath extends BlockMod implements ILexiconable {

	public BlockDirtPath() {
		super(Material.ground);
		setBlockBounds(0F, 0F, 0F, 1F, 15F / 16F, 1F);
		setLightOpacity(255);
		setHardness(0.6F);
		setStepSound(soundTypeGravel);
		setUnlocalizedName(LibBlockNames.DIRT_PATH);
		useNeighborBrightness = true;
	}

	@Override
	public boolean isToolEffective(String type, IBlockState state) {
		return type.equals("shovel");
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity) {
		float speed = 2F;
		float max = 0.4F;

		double motionX = Math.abs(entity.motionX);
		double motionZ = Math.abs(entity.motionZ);
		if(motionX < max)
			entity.motionX *= speed;
		if(motionZ < max)
			entity.motionZ *= speed;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		Block blockAbove = world.getBlockState(pos.up()).getBlock();
		if(!blockAbove.isAir(world, pos.up()))
			setBlockBounds(0F, 0F, 0F, 1F, 1, 1F);
		else setBlockBounds(0F, 0F, 0F, 1F, 15F / 16F, 1F);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block) {
		setBlockBoundsBasedOnState(world, pos);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return new AxisAlignedBB(pos, pos.add(1, 1, 1));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return plantable.getPlantType(world, pos.down()) == EnumPlantType.Plains;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.dirtPath;
	}

}
