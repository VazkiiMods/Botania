/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 1, 2015, 6:35:06 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPavement extends BlockMod {

	public static final int TYPES = 6;

	public BlockPavement() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setUnlocalizedName(LibBlockNames.PAVEMENT);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.PAVEMENT_COLOR, EnumDyeColor.WHITE));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.PAVEMENT_COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		switch (state.getValue(BotaniaStateProps.PAVEMENT_COLOR)) {
			case GREEN: return 5;
			case YELLOW: return 4;
			case RED: return 3;
			case BLUE: return 2;
			case BLACK: return 1;
			case WHITE:
			default: return 0;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= TYPES) {
			meta = 0;
		}

		EnumDyeColor color;
		switch (meta) {
			case 5: color = EnumDyeColor.GREEN; break;
			case 4: color = EnumDyeColor.YELLOW; break;
			case 3: color = EnumDyeColor.RED; break;
			case 2: color = EnumDyeColor.BLUE; break;
			case 1: color = EnumDyeColor.BLACK; break;
			case 0:
			default: color = EnumDyeColor.WHITE; break;
		}
		return getDefaultState().withProperty(BotaniaStateProps.PAVEMENT_COLOR, color);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < TYPES; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos)));
	}

}
