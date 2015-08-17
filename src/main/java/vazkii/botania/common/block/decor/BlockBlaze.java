/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 17, 2015, 7:55:33 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockBlaze extends BlockMod implements ILexiconable, IFuelHandler {

	public BlockBlaze() {
		super(Material.iron);
		setHardness(3F);
		setResistance(10F);
		setStepSound(soundTypeMetal);
		setLightLevel(1F);
		setBlockName(LibBlockNames.BLAZE_BLOCK);
		GameRegistry.registerFuelHandler(this);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.blazeBlock;
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		return fuel.getItem() == Item.getItemFromBlock(this) ? TileEntityFurnace.getItemBurnTime(new ItemStack(Items.blaze_rod)) * (Botania.gardenOfGlassLoaded ? 5 : 10) : 0;
	}

}
