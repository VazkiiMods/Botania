/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 2, 2014, 6:36:54 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A block that implements this can provide an IIcon (block icons only)
 * to be used as an overlay for the mana pool, similarly to the mana void
 * and catalysts.
 */
public interface IPoolOverlayProvider {

	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon(World world, BlockPos pos);

}
