/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 28, 2014, 8:25:39 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.render.block.InterpolatedIcon;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPrismarine extends BlockMod implements ILexiconable {

	private static final int TYPES = 3;
	IIcon[] icons;

	public BlockPrismarine() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setBlockName(LibBlockNames.PRISMARINE);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < TYPES; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[TYPES];
		for(int i = 1; i < TYPES; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void loadTextures(TextureStitchEvent.Pre event) {
		if(event.map.getTextureType() == 0) {
			TextureAtlasSprite icon = new InterpolatedIcon("botania:prismarine0");
			if(event.map.setTextureEntry("botania:prismarine0", icon))
				icons[0] = icon;
		}
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return icons[Math.min(TYPES - 1, par2)];
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return new ItemStack(this, 1, meta);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prismarine;
	}
}