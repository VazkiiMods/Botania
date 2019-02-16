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

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

	public BlockEnchanter(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.ENCHANTER_DIRECTION, EnumFacing.Axis.X));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.ENCHANTER_DIRECTION);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileEnchanter();
	}

	@Nonnull
	@Override
	public IItemProvider getItemDropped(IBlockState state, World world, BlockPos pos, int fortune) {
		return Blocks.LAPIS_BLOCK;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float par7, float par8, float par9) {
		TileEnchanter enchanter = (TileEnchanter) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty() && stack.getItem() == ModItems.twigWand)
			return false;

		boolean stackEnchantable = !stack.isEmpty()
				&& stack.getItem() != Items.BOOK
				&& stack.isEnchantable()
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
	public void onReplaced(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, boolean isMoving) {
		TileEnchanter enchanter = (TileEnchanter) world.getTileEntity(pos);

		if(!enchanter.itemToEnchant.isEmpty()) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), enchanter.itemToEnchant));
		}

		world.updateComparatorOutputLevel(pos, state.getBlock());

		super.onReplaced(state, world, pos, newState, isMoving);
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

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileEnchanter) world.getTileEntity(pos)).renderHUD();
	}
}
