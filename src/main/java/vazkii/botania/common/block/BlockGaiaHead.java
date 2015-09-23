/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 23, 2015, 11:44:35 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGaiaHead extends BlockSkull {

	public BlockGaiaHead() {
		setBlockName(LibBlockNames.GAIA_HEAD);
	}
	
	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockMod.class, par1Str);
		return super.setBlockName(par1Str);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return ModItems.gaiaHead;
    }
    
    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
    	// NO-OP
    }

    @Override
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)  {
    	return 0;
    }

    @Override
    public int damageDropped(int p_149692_1_) {
        return 0;
    }
    
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileGaiaHead();
    }
	
}
