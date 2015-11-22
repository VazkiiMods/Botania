/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 28, 2015, 10:46:20 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockBifrostPerm extends BlockMod implements ILexiconable {

	public BlockBifrostPerm() {
		super(Material.glass);
		setBlockName(LibBlockNames.BIFROST_PERM);
		setLightOpacity(0);
		setHardness(0.3F);
		setLightLevel(1F);
		setStepSound(soundTypeGlass);
		setTickRandomly(true);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean shouldSideBeRendered1(IBlockAccess p_149646_1_, BlockPos pos, EnumFacing side) {
		Block block = p_149646_1_.getBlockState(pos).getBlock();

		return block == this ? false : super.shouldSideBeRendered(p_149646_1_, pos, side);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, BlockPos pos, EnumFacing side) {
		return shouldSideBeRendered1(p_149646_1_, pos, 1 - p_149646_5_); //todo 1.8 wtf
	}

	@Override
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(rand.nextBoolean())
			Botania.proxy.sparkleFX(world, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.45F + 0.2F * (float) Math.random(), 6);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.rainbowRod;
	}

}
