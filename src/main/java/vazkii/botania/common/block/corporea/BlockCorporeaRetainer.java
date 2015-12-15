/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 28, 2015, 11:53:13 AM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCorporeaRetainer extends BlockModContainer implements ILexiconable, ICraftAchievement {

	public BlockCorporeaRetainer() {
		super(Material.iron);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
		setBlockName(LibBlockNames.CORPOREA_RETAINER);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		boolean power = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
		int meta = world.getBlockMetadata(x, y, z);
		boolean powered = (meta & 8) != 0;

		if(power && !powered) {
			((TileCorporeaRetainer) world.getTileEntity(x, y, z)).fulfilRequest();
			world.setBlockMetadataWithNotify(x, y, z, meta | 8, 4);
		} else if(!power && powered)
			world.setBlockMetadataWithNotify(x, y, z, meta & -9, 4);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int s) {
		return ((TileCorporeaRetainer) world.getTileEntity(x, y, z)).hasPendingRequest() ? 15 : 0;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileCorporeaRetainer();
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.corporeaCraft;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaRetainer;
	}

}
