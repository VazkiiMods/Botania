/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 14, 2014, 8:37:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Locale;

public class BlockStorage extends BlockMod implements IElvenItem, ILexiconable {

	public enum Variant {
		MANASTEEL,
		TERRASTEEL,
		ELEMENTIUM,
		MANA_DIAMOND,
		DRAGONSTONE
	}

	private final Variant variant;

	public BlockStorage(Variant v) {
		super(Material.IRON, v.name().toLowerCase(Locale.ROOT) + LibBlockNames.STORAGE_SUFFIX);
		setHardness(3F);
		setResistance(10F);
		setSoundType(SoundType.METAL);
		this.variant = v;
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beaconPos) {
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return variant == Variant.MANASTEEL ? LexiconData.pool : LexiconData.terrasteel;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return variant == Variant.ELEMENTIUM;
	}
}
