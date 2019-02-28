/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 1, 2014, 3:49:53 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileTinyPlanet extends TileMod implements ITickable {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.TINY_PLANET)
	public static TileEntityType<TileTinyPlanet> TYPE;

	public TileTinyPlanet() {
		super(TYPE);
	}

	@Override
	public void tick() {
		ItemTinyPlanet.applyEffect(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}

}
