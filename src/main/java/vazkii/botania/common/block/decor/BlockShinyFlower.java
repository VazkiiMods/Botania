/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 2, 2014, 8:15:49 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.Optional;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockShinyFlower extends BlockModFlower implements IInfusionStabiliser, IHornHarvestable {

	private static IIcon[] icons;
	private static IIcon[] iconsAlt;

	public BlockShinyFlower() {
		super(LibBlockNames.SHINY_FLOWER);
		setLightLevel(1F);
	}

	@Override
	@Optional.Method(modid = "easycoloredlights")
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return ColoredLightHelper.getPackedColor(world.getBlockMetadata(x, y, z), originalLight);
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons = new IIcon[16];
		iconsAlt = new IIcon[16];
		for(int i = 0; i < 16; i++) {
			icons[i] = IconHelper.forName(register, "flowerGlimmering" + i);
			iconsAlt[i] = IconHelper.forName(register, "flowerGlimmering" + i, BlockModFlower.ALT_DIR);
		}
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return (ConfigHandler.altFlowerTextures ? iconsAlt : icons)[Math.min(icons.length - 1, par2)];
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.shinyFlowers;
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean fuckifiknow) {
		return false;
	}

	@Override
	public boolean canStabaliseInfusion(World world, int x, int y, int z) {
		return ConfigHandler.enableThaumcraftStablizers;
	}

	@Override
	public boolean canHornHarvest(World world, int x, int y, int z, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public boolean hasSpecialHornHarvest(World world, int x, int y, int z, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public void harvestByHorn(World world, int x, int y, int z, ItemStack stack, EnumHornType hornType) {
		// NO-OP
	}

}
