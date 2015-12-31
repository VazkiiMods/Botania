/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 15, 2015, 8:31:13 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLightRelay extends BlockModContainer implements IWandable, ILexiconable {

	protected BlockLightRelay() {
		super(Material.glass);
		float f = 5F / 16F;
		setBlockBounds(f, f, f, 1F - f, 1F - f, 1F - f);
		setUnlocalizedName(LibBlockNames.LIGHT_RELAY);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.LUMINIZER_VARIANT, LuminizerVariant.DEFAULT).withProperty(BotaniaStateProps.POWERED, false));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.LUMINIZER_VARIANT, BotaniaStateProps.POWERED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DETECTOR ? 1 : 0;
		if (state.getValue(BotaniaStateProps.POWERED)) {
			meta |= 8;
		} else {
			meta &= -9;
		}
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean powered = (meta & 8) != 0;
		meta &= -9;
		return getDefaultState().withProperty(BotaniaStateProps.POWERED, powered).withProperty(BotaniaStateProps.LUMINIZER_VARIANT, meta == 1 ? LuminizerVariant.DETECTOR : LuminizerVariant.DEFAULT);
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
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 2; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DEFAULT ? 0 : 1;
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess p_149655_1_, BlockPos pos) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing s, float xs, float ys, float zs) {
		((TileLightRelay) world.getTileEntity(pos)).mountEntity(player);
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World p_149668_1_, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 2;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, false), 1 | 2);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int getWeakPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing s) {
		return state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DETECTOR
				&& state.getValue(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 2;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileLightRelay();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.luminizerTransport;
	}

}
