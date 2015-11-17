/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [17/11/2015, 18:33:30 (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockAltGrass extends BlockMod {

	private static final int SUBTYPES = 6;
	IIcon[] icons;

	public BlockAltGrass() {
		super(Material.grass);
		setHardness(0.6F);
		setStepSound(soundTypeGrass);
		setBlockName(LibBlockNames.ALT_GRASS);
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 6; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[SUBTYPES * 2];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 0 || meta >= SUBTYPES ? Blocks.dirt.getIcon(side, meta) : side == 1 ? icons[meta * 2] : icons[meta * 2 + 1];
	}

	// All from below is from BlockMycelium (adapted a bit)
	
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if(!p_149674_1_.isRemote && p_149674_1_.getBlockLightValue(p_149674_2_, p_149674_3_ + 1, p_149674_4_) >= 9) {
			for(int l = 0; l < 4; ++l) {
				int i1 = p_149674_2_ + p_149674_5_.nextInt(3) - 1;
				int j1 = p_149674_3_ + p_149674_5_.nextInt(5) - 3;
				int k1 = p_149674_4_ + p_149674_5_.nextInt(3) - 1;
				Block block = p_149674_1_.getBlock(i1, j1 + 1, k1);

				if(p_149674_1_.getBlock(i1, j1, k1) == Blocks.dirt && p_149674_1_.getBlockMetadata(i1, j1, k1) == 0 && p_149674_1_.getBlockLightValue(i1, j1 + 1, k1) >= 4 && p_149674_1_.getBlockLightOpacity(i1, j1 + 1, k1) <= 2)
					p_149674_1_.setBlock(i1, j1, k1, this);
			}
		}
	}
	
	@Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Blocks.dirt.getItemDropped(0, p_149650_2_, p_149650_3_);
    }
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random r) {
		int meta = world.getBlockMetadata(x, y, z);
		switch(meta) {
		case 0: // Dry
			break;
		case 1: // Golden
			break;
		case 2: // Vivid
			break; 
		case 3: // Scorched
			if(r.nextInt(80) == 0)
	        	world.spawnParticle("flame", x + r.nextFloat(), y + 1.1, z + r.nextFloat(), 0, 0, 0);
			break;
		case 4: // Infused
			if(r.nextInt(100) == 0)
				Botania.proxy.sparkleFX(world, x + r.nextFloat(), y + 1.05, z + r.nextFloat(), 0F, 1F, 1F, r.nextFloat() * 0.2F + 1F, 5);
			break; 
		case 5: // Mutated
			if(r.nextInt(100) == 0) {
				if(r.nextInt(100) > 25)
					Botania.proxy.sparkleFX(world, x + r.nextFloat(), y + 1.05, z + r.nextFloat(), 1F, 0F, 1F, r.nextFloat() * 0.2F + 1F, 5);
				else Botania.proxy.sparkleFX(world, x + r.nextFloat(), y + 1.05, z + r.nextFloat(), 1F, 1F, 0F, r.nextFloat() * 0.2F + 1F, 5); 
			}
			break;
		}
    }

}
