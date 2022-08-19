/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 11, 2014, 1:05:32 AM (GMT)]
 */
package vazkii.botania.common.block.decor.quartz;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialQuartz;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpecialQuartz extends BlockMod implements ILexiconable {

	private final String[] iconNames;
	public final String type;
	private IIcon[] specialQuartzIcons;
	private IIcon chiseledSpecialQuartzIcon;
	private IIcon pillarSpecialQuartzIcon;
	private IIcon specialQuartzTopIcon;

	public BlockSpecialQuartz(String type) {
		super(Material.rock);
		this.type = type;
		iconNames = new String[]{ "block" + type + "Quartz0", "chiseled" + type + "Quartz0", "pillar" + type + "Quartz0", null, null };
		setHardness(0.8F);
		setResistance(10F);
		setBlockName("quartzType" + type);
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockSpecialQuartz.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	public String[] getNames() {
		return new String[] {
				"tile.botania:block" + type + "Quartz",
				"tile.botania:chiseled" + type + "Quartz",
				"tile.botania:pillar" + type + "Quartz",
		};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		if (par2 != 2 && par2 != 3 && par2 != 4) {
			if (par1 != 1 && (par1 != 0 || par2 != 1)) {
				if (par1 == 0)
					return specialQuartzTopIcon;
				else {
					if (par2 < 0 || par2 >= specialQuartzIcons.length)
						par2 = 0;

					return specialQuartzIcons[par2];
				}
			} else return par2 == 1 ? chiseledSpecialQuartzIcon : specialQuartzTopIcon;
		} else
			return par2 == 2 && (par1 == 1 || par1 == 0) ? pillarSpecialQuartzIcon : par2 == 3 && (par1 == 5 || par1 == 4) ? pillarSpecialQuartzIcon : par2 == 4 && (par1 == 2 || par1 == 3) ? pillarSpecialQuartzIcon : specialQuartzIcons[par2];
	}

	@Override
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
		if (par9 == 2) {
			switch (par5) {
			case 0:
			case 1:
				par9 = 2;
				break;
			case 2:
			case 3:
				par9 = 4;
				break;
			case 4:
			case 5:
				par9 = 3;
			}
		}

		return par9;
	}

	@Override
	public int damageDropped(int par1) {
		return par1 != 3 && par1 != 4 ? par1 : 2;
	}

	@Override
	public ItemStack createStackedBlock(int par1) {
		return par1 != 3 && par1 != 4 ? super.createStackedBlock(par1) : new ItemStack(this, 1, 2);
	}

	@Override
	public int getRenderType() {
		return 39;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		specialQuartzIcons = new IIcon[iconNames.length];

		for (int i = 0; i < specialQuartzIcons.length; ++i) {
			if (iconNames[i] == null)
				specialQuartzIcons[i] = specialQuartzIcons[i - 1];
			else specialQuartzIcons[i] = IconHelper.forName(par1IconRegister, iconNames[i]);
		}

		specialQuartzTopIcon = IconHelper.forName(par1IconRegister, "block"  + type + "Quartz1");
		chiseledSpecialQuartzIcon = IconHelper.forName(par1IconRegister, "chiseled" + type + "Quartz1");
		pillarSpecialQuartzIcon = IconHelper.forName(par1IconRegister, "pillar" + type + "Quartz1");
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return this == ModFluffBlocks.elfQuartz ? LexiconData.elvenResources : LexiconData.decorativeBlocks;
	}
}
