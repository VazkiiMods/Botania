/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 4, 2014, 6:00:44 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.block.ItemBlockWithMetaNameAndColor;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUnstable extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

	public BlockUnstable() {
		super(Material.iron);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setUnlocalizedName(LibBlockNames.UNSTABLE_BLOCK);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.COLOR).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= EnumDyeColor.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetaNameAndColor.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World par1World, BlockPos pos, Random par5Random) {
		int color = Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, par1World, pos, 0);
		int colorBright = new Color(color).brighter().getRGB();
		int colorDark = new Color(color).darker().getRGB();

		Vector3 origVector = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		Vector3 endVector = origVector.copy().add(par1World.rand.nextDouble() * 2 - 1, par1World.rand.nextDouble() * 2 - 1, par1World.rand.nextDouble() * 2 - 1);
		Botania.proxy.lightningFX(par1World, origVector, endVector, 5F, colorDark, colorBright);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.unstableBlocks;
	}

}
