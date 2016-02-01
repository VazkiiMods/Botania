/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2014, 10:04:25 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLivingwood extends BlockMod implements ILexiconable {

	public BlockLivingwood() {
		this(LibBlockNames.LIVING_WOOD);
	}

	public BlockLivingwood(String name) {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setUnlocalizedName(name);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.DEFAULT));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.LIVINGWOOD_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.LIVINGWOOD_VARIANT).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= LivingWoodVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.values()[meta]);
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
		register(par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, name);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < LivingWoodVariant.values().length; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos)));
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getValue(BotaniaStateProps.LIVINGWOOD_VARIANT) == LivingWoodVariant.GLIMMERING ? 12 : 0;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		boolean defaultVariant = world.getBlockState(pos).getValue(BotaniaStateProps.LIVINGWOOD_VARIANT) == LivingWoodVariant.DEFAULT;
		return defaultVariant ? LexiconData.pureDaisy : LexiconData.decorativeBlocks;
	}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getValue(BotaniaStateProps.LIVINGWOOD_VARIANT) == LivingWoodVariant.DEFAULT;
	}

}
