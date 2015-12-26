/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 20, 2014, 8:23:17 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageBrew;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBifrost extends BlockModContainer implements ILexiconable {

	public BlockBifrost() {
		super(Material.glass);
		setUnlocalizedName(LibBlockNames.BIFROST);
		setLightOpacity(0);
		setLightLevel(1F);
		setBlockUnbreakable();
		setStepSound(soundTypeGlass);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(ModItems.rainbowRod);
	}

	public boolean shouldSideBeRendered1(IBlockAccess p_149646_1_, BlockPos pos, EnumFacing side) {
		Block block = p_149646_1_.getBlockState(pos).getBlock();

		return block == this ? false : super.shouldSideBeRendered(p_149646_1_, pos, side);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, BlockPos pos, EnumFacing side) {
		return shouldSideBeRendered1(p_149646_1_, pos, side.getOpposite());
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBifrost();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.rainbowRod;
	}

}
