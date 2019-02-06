/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 18, 2015, 8:29:16 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls.living;

import net.minecraft.block.SoundType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.walls.BlockModWall;

public class BlockDreamwoodWall extends BlockModWall {

	public BlockDreamwoodWall() {
		super(ModBlocks.dreamwood, 0);
		setHardness(2.0F);
		setSoundType(SoundType.WOOD);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerCustomItemblock(this, "dreamwood_wall");
	}

}
