/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 23, 2015, 11:47:40 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class TileGaiaHead extends SkullTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.GAIA_HEAD)
	public static TileEntityType<TileGaiaHead> TYPE;

	@Nonnull
	@Override
	public TileEntityType<TileGaiaHead> getType() {
		return TYPE;
	}

}
