/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 28, 2014, 6:43:33 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Random;

public class BlockBuriedPetals extends BlockModFlower {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);

	public BlockBuriedPetals() {
		super(LibBlockNames.BURIED_PETALS);
		setLightLevel(0.25F);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	@Optional.Method(modid = "easycoloredlights")
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ColoredLightHelper.getPackedColor(world.getBlockState(pos).getValue(BotaniaStateProps.COLOR), originalLight);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World par1World, BlockPos pos, Random par5Random) {
		EnumDyeColor color = state.getValue(BotaniaStateProps.COLOR);
		int hex = color.getMapColor().colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = (hex & 0xFF);

		Botania.proxy.setSparkleFXNoClip(true);
		Botania.proxy.sparkleFX(par1World, pos.getX() + 0.3 + par5Random.nextFloat() * 0.5, pos.getY() + 0.1 + par5Random.nextFloat() * 0.1, pos.getZ() + 0.3 + par5Random.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, par5Random.nextFloat(), 5);

		Botania.proxy.setSparkleFXNoClip(false);
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random p_149650_2_, int p_149650_3_) {
		return ModItems.petal;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
}
