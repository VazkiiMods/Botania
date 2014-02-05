/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 12:22:58 AM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vazkii.botania.api.IWandHUD;
import vazkii.botania.api.IWandable;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TilePool;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.network.PacketDispatcher;

public class BlockPool extends BlockModContainer implements IWandHUD, IWandable {

	protected BlockPool() {
		super(LibBlockIDs.idPool, Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(LibBlockNames.POOL);
		setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePool();
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TilePool tile = (TilePool) par1World.getBlockTileEntity(par2, par3, par4);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				PacketDispatcher.sendPacketToAllInDimension(tile.getDescriptionPacket(), par1World.provider.dimensionId);
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public Icon getIcon(int par1, int par2) {
		return ModBlocks.livingrock.getIcon(par1, par2);
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idPool;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TilePool) world.getBlockTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TilePool) world.getBlockTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}
}
