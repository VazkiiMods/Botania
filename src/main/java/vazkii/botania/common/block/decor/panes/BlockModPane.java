/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 30, 2015, 10:01:17 PM (GMT)]
 */
package vazkii.botania.common.block.decor.panes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockModPane extends BlockPane {

	Block source;
	public IIcon iconTop;

	public BlockModPane(Block source) {
		super("", "", Material.glass, false);
		this.source = source;
		setBlockName(source.getUnlocalizedName().replaceAll("tile.", "") + "Pane");
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setHardness(0.3F);
		setStepSound(soundTypeGlass);
		setLightLevel(1.0F);
		useNeighborBrightness = true;
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockMod.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		iconTop = IconHelper.forBlock(reg, this);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	@Override
	public int getRenderType() {
		return 18;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon func_150097_e() {
		return source.getIcon(0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side >= 2 ? iconTop : source.getIcon(side, meta);
	}

	@Override
	public boolean canPaneConnectTo(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
		Block block = world.getBlock(x, y, z);
		return block == ModBlocks.elfGlass || block == ModBlocks.manaGlass || block == ModBlocks.bifrostPerm || super.canPaneConnectTo(world, x, y, z, dir);
	}

}
