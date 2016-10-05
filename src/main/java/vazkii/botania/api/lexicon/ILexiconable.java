/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 20, 2014, 7:05:44 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Any block that implements this can be right clicked with
 * a Lexica Botania to open a entry page.
 */
public interface ILexiconable {

	/**
	 * Gets the lexicon entry to open at this location. null works too.
	 */
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon);

}
