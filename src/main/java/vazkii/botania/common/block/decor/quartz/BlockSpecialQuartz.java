/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 11, 2014, 1:05:32 AM (GMT)]
 */
package vazkii.botania.common.block.decor.quartz;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.QuartzVariant;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockSpecialQuartz extends BlockMod implements ILexiconable {

	public final String type;

	public BlockSpecialQuartz(String type) {
		super(Material.ROCK, "quartzType" + type);
		this.type = type;
		setHardness(0.8F);
		setResistance(10F);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.NORMAL));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.QUARTZ_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.QUARTZ_VARIANT).ordinal();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= QuartzVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.values()[meta]);
	}

	public String[] getNames() {
		return new String[] {
				"tile.botania:block" + type + "Quartz",
				"tile.botania:chiseled" + type + "Quartz",
				"tile.botania:pillar" + type + "Quartz",
		};
	}

	@Nonnull
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float par6, float par7, float par8, int meta, EntityLivingBase placer) {
		if (meta == 2) { // Pillar quartz variant
			switch (side.getAxis()) {
			case Y:
				return getDefaultState().withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.PILLAR_Y);
			case Z:
				return getDefaultState().withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.PILLAR_Z);
			case X:
				return getDefaultState().withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.PILLAR_X);
			}
		}

		return getStateFromMeta(meta);
	}

	@Override
	public int damageDropped(IBlockState state) {
		QuartzVariant variant = state.getValue(BotaniaStateProps.QUARTZ_VARIANT);
		if (variant == QuartzVariant.PILLAR_X || variant == QuartzVariant.PILLAR_Z) {
			state = state.withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.PILLAR_Y);
		}
		return getMetaFromState(state);
	}

	@Nonnull
	@Override
	public ItemStack getSilkTouchDrop(@Nonnull IBlockState state) {
		return new ItemStack(this, 1, damageDropped(state));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		stacks.add(new ItemStack(this, 1, 0));
		stacks.add(new ItemStack(this, 1, 1));
		stacks.add(new ItemStack(this, 1, 2));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return this == ModFluffBlocks.elfQuartz ? LexiconData.elvenResources : LexiconData.decorativeBlocks;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, 0, getDefaultState());
		ModelHandler.registerBlockToState(this, 1, getDefaultState().withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.CHISELED));
		ModelHandler.registerBlockToState(this, 2, getDefaultState().withProperty(BotaniaStateProps.QUARTZ_VARIANT, QuartzVariant.PILLAR_Y));
	}

}
