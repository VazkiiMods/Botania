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
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockTeruTeruBozu extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25, 0.01, 0.25, 0.75, 0.99, 0.75);

	public BlockTeruTeruBozu() {
		super(Material.CLOTH, LibBlockNames.TERU_TERU_BOZU);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing s, float xs, float ys, float zs) {
		if(stack != null && (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world))) {
			if(!player.capabilities.isCreativeMode)
				stack.stackSize--;
			return true;
		}
		return false;
	}

	private boolean isSunflower(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(Blocks.DOUBLE_PLANT) && stack.getItemDamage() == 0;
	}

	private boolean isBlueOrchid(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER) && stack.getItemDamage() == 1;
	}

	private boolean removeRain(World world) {
		if(world.isRaining()) {
			world.getWorldInfo().setRaining(false);
			return true;
		}
		return false;
	}

	private boolean startRain(World world) {
		if(!world.isRaining()) {
			if(world.rand.nextInt(10) == 0)
				world.getWorldInfo().setRaining(true);
			return true;
		}
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return world.isRaining() ? 15 : 0;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileTeruTeruBozu();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.teruTeruBozu;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerCustomItemblock(this, "teru_teru_bozu");
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(this), 0, TileTeruTeruBozu.class);
	}

}
