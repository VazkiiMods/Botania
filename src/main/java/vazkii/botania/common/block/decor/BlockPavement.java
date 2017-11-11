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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockPavement extends BlockMod {

	public static final int TYPES = 6;

	public BlockPavement() {
		super(Material.ROCK, LibBlockNames.PAVEMENT);
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.PAVEMENT_COLOR, EnumDyeColor.WHITE));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.PAVEMENT_COLOR);
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

	@Nonnull
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
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		for(int i = 0; i < TYPES; i++)
			stacks.add(new ItemStack(this, 1, i));
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos)));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, BotaniaStateProps.PAVEMENT_COLOR.getAllowedValues().size());
	}

}
