/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 25, 2015, 12:24:10 AM (GMT)]
 */
package vazkii.botania.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockManaBomb extends BlockMod implements IManaTrigger {

	public BlockManaBomb() {
		super(Material.wood);
		setHardness(12.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.MANA_BOMB);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		if(!burst.isFake() && !world.isRemote) {
			world.playAuxSFX(2001, x, y, z, getIdFromBlock(this));
			world.setBlockToAir(x, y, z);
			EntityManaStorm storm = new EntityManaStorm(world);
			storm.setPosition(x + 0.5, y + 0.5, z + 0.5);
			world.spawnEntityInWorld(storm);
		}
	}

}
