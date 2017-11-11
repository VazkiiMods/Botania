/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 15, 2014, 4:08:26 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockEnchanter extends BlockMod implements IWandable, ILexiconable, IWandHUD {

	public BlockEnchanter() {
		super(Material.ROCK, LibBlockNames.ENCHANTER);
		setHardness(3.0F);
		setResistance(5.0F);
		setLightLevel(1.0F);
		setSoundType(SoundType.STONE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.ENCHANTER_DIRECTION, EnumFacing.Axis.X));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.ENCHANTER_DIRECTION);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		switch (state.getValue(BotaniaStateProps.ENCHANTER_DIRECTION)) {
		case Z: return 1;
		case X:
		default: return 0;
		}
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.ENCHANTER_DIRECTION, meta == 1 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEnchanter();
	}

	@Nonnull
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.LAPIS_BLOCK);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float par7, float par8, float par9) {
		TileEnchanter enchanter = (TileEnchanter) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty() && stack.getItem() == ModItems.twigWand)
			return false;

		boolean stackEnchantable = !stack.isEmpty()
				&& stack.getItem() != Items.BOOK
				&& stack.isItemEnchantable()
				&& stack.getCount() == 1;

		if(enchanter.itemToEnchant.isEmpty()) {
			if(stackEnchantable) {
				enchanter.itemToEnchant = stack.copy();
				player.setHeldItem(hand, ItemStack.EMPTY);
				enchanter.sync();
			} else {
				return false;
			}
		} else if(enchanter.stage == TileEnchanter.State.IDLE) {
			ItemHandlerHelper.giveItemToPlayer(player, enchanter.itemToEnchant.copy());
			enchanter.itemToEnchant = ItemStack.EMPTY;
			enchanter.sync();
		}

		return true;
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileEnchanter enchanter = (TileEnchanter) world.getTileEntity(pos);

		if(!enchanter.itemToEnchant.isEmpty()) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), enchanter.itemToEnchant));
		}

		world.updateComparatorOutputLevel(pos, state.getBlock());

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TileEnchanter) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.manaEnchanting;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		((TileEnchanter) world.getTileEntity(pos)).renderHUD(res);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, 0, getDefaultState().withProperty(BotaniaStateProps.ENCHANTER_DIRECTION, EnumFacing.Axis.X));
	}

}
