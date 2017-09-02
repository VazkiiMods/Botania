/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 4, 2014, 12:29:56 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.api.state.enums.CrateVariant;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockOpenCrate extends BlockMod implements ILexiconable, IWandable, IWandHUD {

	public BlockOpenCrate() {
		super(Material.WOOD, LibBlockNames.OPEN_CRATE);
		setHardness(2.0F);
		setSoundType(SoundType.WOOD);
		setDefaultState(blockState.getBaseState()
				.withProperty(BotaniaStateProps.CRATE_VARIANT, CrateVariant.OPEN)
				.withProperty(BotaniaStateProps.CRATE_PATTERN, CratePattern.NONE));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.CRATE_VARIANT, BotaniaStateProps.CRATE_PATTERN);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.CRATE_VARIANT).ordinal();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= CrateVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.CRATE_VARIANT, CrateVariant.values()[meta]);
	}

	@Nonnull
	@Override
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if (te instanceof TileCraftCrate) {
			TileCraftCrate tile = (TileCraftCrate) te;
			state = state.withProperty(BotaniaStateProps.CRATE_PATTERN, CratePattern.values()[tile.pattern + 1]);
		} else {
			state = state.withProperty(BotaniaStateProps.CRATE_PATTERN, CratePattern.NONE);
		}

		return state;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		for(int i = 0; i < CrateVariant.values().length; i++)
			stacks.add(new ItemStack(this, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}


	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileOpenCrate crate = (TileOpenCrate) world.getTileEntity(pos);
		return crate.getSignal();
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		if (!world.isRemote) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return state.getValue(BotaniaStateProps.CRATE_VARIANT) == CrateVariant.OPEN ? new TileOpenCrate() : new TileCraftCrate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return world.getBlockState(pos).getValue(BotaniaStateProps.CRATE_VARIANT) == CrateVariant.OPEN ? LexiconData.openCrate : LexiconData.craftCrate;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		TileOpenCrate crate = (TileOpenCrate) world.getTileEntity(pos);
		return crate.onWanded(world, player, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileCraftCrate) {
			TileCraftCrate craft = (TileCraftCrate) tile;

			int width = 52;
			int height = 52;
			int xc = res.getScaledWidth() / 2 + 20;
			int yc = res.getScaledHeight() / 2 - height / 2;

			Gui.drawRect(xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x22000000);
			Gui.drawRect(xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x22000000);

			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++) {
					int index = i * 3 + j;
					int xp = xc + j * 18;
					int yp = yc + i * 18;

					boolean enabled = true;
					if(craft.pattern > -1)
						enabled = TileCraftCrate.PATTERNS[craft.pattern][index];

					Gui.drawRect(xp, yp, xp + 16, yp + 16, enabled ? 0x22FFFFFF : 0x22FF0000);

					ItemStack item = craft.getItemHandler().getStackInSlot(index);
					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					GlStateManager.enableRescaleNormal();
					mc.getRenderItem().renderItemAndEffectIntoGUI(item, xp, yp);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
				}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, 0, getDefaultState());
		ModelHandler.registerBlockToState(this, 1, getDefaultState().withProperty(BotaniaStateProps.CRATE_VARIANT, CrateVariant.CRAFTY));
	}

}
