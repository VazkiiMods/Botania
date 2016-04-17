/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 28, 2014, 9:24:43 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockSeaLamp extends BlockMod implements ILexiconable {

	private int coloredLight = -1;

	public BlockSeaLamp() {
		super(Material.glass, LibBlockNames.SEA_LAMP);
		setHardness(0.3F);
		setSoundType(SoundType.GLASS);
		setLightLevel(1.0F);
	}

	@Override
	@Optional.Method(modid = "easycoloredlights")
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return coloredLight == -1 ? (coloredLight = ColoredLightHelper.makeRGBLightValue(85, 136, 125, originalLight)) : coloredLight;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prismarine;
	}

}
