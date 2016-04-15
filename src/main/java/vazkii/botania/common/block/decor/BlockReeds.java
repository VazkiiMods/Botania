/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 28, 2014, 8:02:49 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class BlockReeds extends BlockRotatedPillar implements ILexiconable {

	public BlockReeds() {
		super(Material.wood);
		setHardness(1.0F);
		setSoundType(SoundType.WOOD);
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.REED_BLOCK));
		GameRegistry.register(new ItemBlockMod(this), getRegistryName());
		setUnlocalizedName(LibBlockNames.REED_BLOCK);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.AXIS_FACING, EnumFacing.Axis.Y));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.AXIS_FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		switch (state.getValue(BotaniaStateProps.AXIS_FACING)) {
			case Z: return 8;
			case X: return 4;
			case Y:
			default: return 0;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing.Axis axis = null;
		switch (meta) {
			case 8: axis = EnumFacing.Axis.Z; break;
			case 4: axis = EnumFacing.Axis.X; break;
			case 0:
			default: axis = EnumFacing.Axis.Y; break;
		}
		return getDefaultState().withProperty(BotaniaStateProps.AXIS_FACING, axis);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(meta).withProperty(BotaniaStateProps.AXIS_FACING, facing.getAxis());
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}
}
