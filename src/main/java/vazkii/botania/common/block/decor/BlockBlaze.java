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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockBlaze extends BlockMod implements ILexiconable, IFuelHandler {

	public BlockBlaze() {
		super(Material.IRON, LibBlockNames.BLAZE_BLOCK);
		setHardness(3F);
		setResistance(10F);
		setSoundType(SoundType.METAL);
		setLightLevel(1F);
		GameRegistry.registerFuelHandler(this);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.blazeBlock;
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		return fuel.getItem() == Item.getItemFromBlock(this) ? TileEntityFurnace.getItemBurnTime(new ItemStack(Items.BLAZE_ROD)) * (Botania.gardenOfGlassLoaded ? 5 : 10) : 0;
	}

}
