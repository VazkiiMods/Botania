/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 1, 2015, 1:11:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockTeruTeruBozu extends BlockModContainer implements ILexiconable {

	public BlockTeruTeruBozu() {
		super(Material.cloth);
		setUnlocalizedName(LibBlockNames.TERU_TERU_BOZU);
		float f = 0.25F;
		setBlockBounds(f, 0.01F, f, 1F - f, 0.99F, 1F - f);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity e) {
		if(!world.isRemote && e instanceof EntityItem) {
			EntityItem item = (EntityItem) e;
			ItemStack stack = item.getEntityItem();
			if(isSunflower(stack) && removeRain(world)) {
				stack.stackSize--;
				if(stack.stackSize == 0)
					e.setDead();
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing s, float xs, float ys, float zs) {
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world))) {
			if(!player.capabilities.isCreativeMode)
				stack.stackSize--;
			return true;
		}
		return false;
	}

	public boolean isSunflower(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(Blocks.double_plant) && stack.getItemDamage() == 0;
	}

	public boolean isBlueOrchid(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(Blocks.red_flower) && stack.getItemDamage() == 1;
	}

	public boolean removeRain(World world) {
		if(world.isRaining()) {
			world.getWorldInfo().setRaining(false);
			return true;
		}
		return false;
	}

	public boolean startRain(World world) {
		if(!world.isRaining()) {
			if(world.rand.nextInt(10) == 0)
				world.getWorldInfo().setRaining(true);
			return true;
		}
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos) {
		return world.isRaining() ? 15 : 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 2;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileTeruTeruBozu();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.teruTeruBozu;
	}

}
