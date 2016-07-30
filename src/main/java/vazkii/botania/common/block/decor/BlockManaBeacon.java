/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 3, 2014, 4:53:59 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileManaBeacon;
import vazkii.botania.common.item.block.ItemBlockWithMetaNameAndColor;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockManaBeacon extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(3.0/16, 3.0/16, 3.0/16, 13.0/16, 13.0/16, 13.0/16);

	public BlockManaBeacon() {
		super(Material.IRON, LibBlockNames.MANA_BEACON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.COLOR).getMetadata();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= EnumDyeColor.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, List<ItemStack> stacks) {
		for(int i = 0; i < 16; i++)
			stacks.add(new ItemStack(item, 1, i));
	}

	@Override
	public void registerItemForm() {
		GameRegistry.register(new ItemBlockWithMetaNameAndColor(this), getRegistryName());
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
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.unstableBlocks;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileManaBeacon();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, (new StateMap.Builder()).ignore(BotaniaStateProps.COLOR).build());
	}
}
